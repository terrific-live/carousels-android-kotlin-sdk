package demo.terrific.compose.analytics

import AnalyticsEventMapper
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class AnalyticsEventMapperTest {
    private fun mapper() = AnalyticsEventMapper(
        externalUserId = "external-1",
        userId = { "user-1" },
        carouselId = "carousel-1",
        storeIdProvider = { "store-1" }
    )

    @Test
    fun `asset event builds session id with asset id`() {
        val request = mapper().map(
            TimelineEvent.TimelineAssetLikedEvent(
                assetId = "asset-7",
                parentUrl = "https://example.com"
            )
        )
        assertEquals("carousel-1~asset-7", request.sessionId)
    }

    @Test
    fun `non asset event uses carousel id as session id`() {
        val request = mapper().map(
            TimelineEvent.TimelineOpenedEvent(parentUrl = "https://example.com")
        )
        assertEquals("carousel-1", request.sessionId)
    }

    @Test
    fun `external user id is added to every aux data`() {
        val events = listOf(
            TimelineEvent.TimelineOpenedEvent("https://example.com"),
            TimelineEvent.TimelineClosedEvent("https://example.com"),
            TimelineEvent.TimelineAssetLikedEvent("asset-1", "https://example.com"),
            TimelineEvent.TimelineCarouselViewedEvent(
                assetIds = listOf("a1"),
                assetTimestamps = listOf("t1"),
                parentUrl = "https://example.com",
                totalAssets = 1
            )
        )

        events.forEach { event ->
            assertEquals("external-1", mapper().map(event).auxData["externalUserId"])
        }
    }

    @Test
    fun `carousel clicked maps expected fields`() {
        val request = mapper().map(
            TimelineEvent.TimelineCarouselClickedEvent(
                assetId = "asset-1",
                assetIds = listOf("asset-1", "asset-2"),
                assetTimestamps = listOf("t1", "t2"),
                parentUrl = "parent",
                totalAssets = 2,
                position = 3
            )
        )

        assertEquals("TimelineCarouselClicked", request.name)
        assertEquals("user-1", request.userId)
        assertEquals("store-1", request.storeId)
        assertEquals("asset-1", request.auxData["assetId"])
        assertEquals(listOf("asset-1", "asset-2"), request.auxData["assetIds"])
        assertEquals(2, request.auxData["totalAssets"])
        assertEquals(3, request.auxData["position"])
        assertTrue(request.eventId.isNotBlank())
        assertTrue(request.timeStamp.isNotBlank())
    }

    @Test
    fun `poll voted maps top level poll fields`() {
        val request = mapper().map(
            TimelineEvent.TimelinePollVotedEvent(
                assetId = "asset-1",
                pollId = "poll-1",
                pollAnswer = "yes",
                parentUrl = "parent",
                questionId = "q1"
            )
        )
        assertEquals("poll-1", request.pollId)
        assertEquals("yes", request.pollAnswer)
        assertEquals("q1", request.auxData["questionId"])
    }

    @Test
    fun `custom event preserves custom aux data and enriches it`() {
        val request = mapper().map(
            TimelineEvent.CustomAnalyticsEvent(
                name = "CustomEvent",
                assetId = "asset-9",
                auxData = mapOf("foo" to "bar", "count" to 2)
            )
        )
        assertEquals("CustomEvent", request.name)
        assertEquals("carousel-1~asset-9", request.sessionId)
        assertEquals("bar", request.auxData["foo"])
        assertEquals(2, request.auxData["count"])
        assertEquals("external-1", request.auxData["externalUserId"])
    }
}
