package demo.terrific.compose.controller

import demo.terrific.compose.model.*
import demo.terrific.compose.repository.VideoRepository
import demo.terrific.compose.storage.likes.LikesStorage
import demo.terrific.compose.storage.storage.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceUntilIdle
import org.junit.Assert.*
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class VideoFeatureControllerTest {
    @Test
    fun `load restores liked videos and poll state`() {
        val dispatcher = StandardTestDispatcher()
        val scope = TestScope(dispatcher)
        val poll = PollDataDto(
            id = "poll",
            question = "Question?",
            questionId = "q1",
            options = listOf(PollOptionDto("A", 1), PollOptionDto("B", 2))
        )
        val repository = FakeRepository(
            AssetsResponse(
                assets = listOf(AssetDto(id = "asset-1", pollData = poll, position = 0)),
                carouselConfig = CarouselConfigDto(timestampFormat = "format")
            )
        )
        val likes = FakeLikesStorage(setOf("asset-1"))
        val polls = FakePollStorage(
            SavedPollState("B", listOf(SavedPollOption("A", 5), SavedPollOption("B", 9)))
        )
        val controller = VideoFeatureController(repository, likes, polls, scope)

        controller.load("store", "carousel")
        scope.advanceUntilIdle()

        val state = controller.state.value
        assertFalse(state.isLoading)
        assertNull(state.error)
        assertEquals(setOf("asset-1"), state.likedVideoIds)
        assertEquals("B", state.selectedPollAnswers["q1"])
        assertEquals(listOf(5, 9), state.assets.first().pollData!!.options.map { it.numberOfVotes })
        assertEquals("format", state.timestampFormat)
    }

    @Test
    fun `load exposes repository error`() {
        val dispatcher = StandardTestDispatcher()
        val scope = TestScope(dispatcher)
        val controller = VideoFeatureController(FailingRepository(), FakeLikesStorage(), FakePollStorage(), scope)

        controller.load("store", "carousel")
        scope.advanceUntilIdle()

        assertFalse(controller.state.value.isLoading)
        assertEquals("boom", controller.state.value.error)
    }

    @Test
    fun `like click updates state from storage`() {
        val dispatcher = StandardTestDispatcher()
        val scope = TestScope(dispatcher)
        val likes = FakeLikesStorage()
        val controller = VideoFeatureController(FakeRepository(), likes, FakePollStorage(), scope)
        controller.load("store", "carousel")
        scope.advanceUntilIdle()

        controller.onLikeClick("video-1")

        assertEquals(setOf("video-1"), controller.state.value.likedVideoIds)
    }

    @Test
    fun `changing poll answer moves vote and saves state`() {
        val dispatcher = StandardTestDispatcher()
        val scope = TestScope(dispatcher)
        val poll = PollDataDto("poll", "Q", "q1", listOf(PollOptionDto("A", 2), PollOptionDto("B", 3)))
        val storage = FakePollStorage()
        val controller = VideoFeatureController(
            FakeRepository(AssetsResponse(listOf(AssetDto("asset", pollData = poll, position = 0)), CarouselConfigDto())),
            FakeLikesStorage(), storage, scope
        )
        controller.load("store", "carousel")
        scope.advanceUntilIdle()

        controller.onPollOptionClick("asset", "q1", "A")
        controller.onPollOptionClick("asset", "q1", "B")

        val options = controller.state.value.assets.first().pollData!!.options.associate { it.text to it.numberOfVotes }
        assertEquals(2, options["A"])
        assertEquals(4, options["B"])
        assertEquals("B", controller.state.value.selectedPollAnswers["q1"])
        assertEquals("B", storage.lastSelectedOption)
    }

    private class FakeRepository(private val response: AssetsResponse = AssetsResponse(emptyList(), CarouselConfigDto())) : VideoRepository {
        override suspend fun getFeedCarousel(storeId: String, carouselId: String): List<AssetDto> = response.assets
        override suspend fun getFeed(storeId: String, carouselId: String): AssetsResponse = response
    }

    private class FailingRepository : VideoRepository {
        override suspend fun getFeedCarousel(storeId: String, carouselId: String): List<AssetDto> = error("boom")
        override suspend fun getFeed(storeId: String, carouselId: String): AssetsResponse = error("boom")
    }

    private class FakeLikesStorage(initial: Set<String> = emptySet()) : LikesStorage {
        private var likes = initial
        override fun getLikedVideoIds(carouselId: String): Set<String> = likes
        override fun toggleLike(carouselId: String, videoId: String): Set<String> {
            likes = if (videoId in likes) likes - videoId else likes + videoId
            return likes
        }
        override fun isLiked(carouselId: String, videoId: String): Boolean = videoId in likes
    }

    private class FakePollStorage(private var saved: SavedPollState? = null) : PollStorage {
        var lastSelectedOption: String? = null
        override fun savePollState(questionId: String, selectedOptionText: String, options: List<PollOptionDto>) {
            lastSelectedOption = selectedOptionText
            saved = SavedPollState(selectedOptionText, options.map { SavedPollOption(it.text, it.numberOfVotes) })
        }
        override fun getSavedPollState(questionId: String): SavedPollState? = saved
    }
}
