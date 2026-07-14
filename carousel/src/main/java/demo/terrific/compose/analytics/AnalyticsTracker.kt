package demo.terrific.compose.analytics

import demo.terrific.compose.repository.analytics.TerrificAnalyticsRepository

class AnalyticsTracker(
    private val repository: TerrificAnalyticsRepository,
    private val userIdProvider: () -> String,
    private val sessionIdProvider: () -> String,
) {

    suspend fun trackTimelineOpened(
        parentUrl: String?,
    ) {
        repository.send(
            TimelineOpenedRequest(
                userId = userIdProvider(),
                sessionId = sessionIdProvider(),
                auxData = TimelineOpenedAuxData(
                    parentUrl = parentUrl
                )
            )
        )
    }

    suspend fun trackCarouselClicked(
        assetId: String,
        assetIds: List<String>,
        assetTimestamps: List<String>,
        parentUrl: String?,
        position: Int?,
    ) {
        repository.send(
            TimelineCarouselClickedRequest(
                userId = userIdProvider(),
                sessionId = sessionIdProvider(),
                auxData = TimelineCarouselClickedAuxData(
                    assetId = assetId,
                    assetIds = assetIds,
                    assetTimestamps = assetTimestamps,
                    parentUrl = parentUrl,
                    totalAssets = assetIds.size,
                    position = position
                )
            )
        )
    }
}