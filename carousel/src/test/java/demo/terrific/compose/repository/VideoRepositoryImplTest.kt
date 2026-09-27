package demo.terrific.compose.repository

import demo.terrific.compose.model.AssetDto
import demo.terrific.compose.model.AssetsResponse
import demo.terrific.compose.model.CarouselConfigDto
import demo.terrific.compose.network.VideoApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class VideoRepositoryImplTest {
    @Test
    fun `getFeedCarousel returns only assets from api response`() = runTest {
        val response = AssetsResponse(
            assets = listOf(AssetDto(id = "a1", position = 0), AssetDto(id = "a2", position = 1)),
            carouselConfig = CarouselConfigDto(name = "carousel")
        )
        val repository = VideoRepositoryImpl(FakeVideoApi(carouselResponse = response))

        val result = repository.getFeedCarousel("store", "carousel")

        assertEquals(listOf("a1", "a2"), result.map { it.id })
    }

    @Test
    fun `getFeed returns vertical api response`() = runTest {
        val response = AssetsResponse(
            assets = listOf(AssetDto(id = "vertical", position = 0)),
            carouselConfig = CarouselConfigDto(timestampFormat = "dd-MM")
        )
        val api = FakeVideoApi(verticalResponse = response)
        val repository = VideoRepositoryImpl(api)

        val result = repository.getFeed("store-1", "display-1")

        assertEquals(response, result)
        assertEquals("store-1", api.lastStoreId)
        assertEquals("display-1", api.lastDisplayId)
    }

    private class FakeVideoApi(
        private val carouselResponse: AssetsResponse = AssetsResponse(emptyList(), CarouselConfigDto()),
        private val verticalResponse: AssetsResponse = AssetsResponse(emptyList(), CarouselConfigDto())
    ) : VideoApi {
        var lastStoreId: String? = null
        var lastDisplayId: String? = null

        override suspend fun getCarouselAssets(storeId: String, carouselId: String): AssetsResponse = carouselResponse

        override suspend fun getVerticalAssets(storeId: String, displayId: String, numberOfItems: Int): AssetsResponse {
            lastStoreId = storeId
            lastDisplayId = displayId
            return verticalResponse
        }
    }
}
