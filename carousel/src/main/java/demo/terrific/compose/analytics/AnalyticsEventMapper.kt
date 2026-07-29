import demo.terrific.compose.analytics.AnalyticsEvent
import demo.terrific.compose.analytics.AnalyticsRequest
import demo.terrific.compose.analytics.TimelineEvent
import java.util.UUID
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

internal class AnalyticsEventMapper(
    private val externalUserId: () -> String,
    private val sessionIdProvider: () -> String,
    private val storeIdProvider: () -> String
) {

    @OptIn(ExperimentalTime::class)
    fun map(event: AnalyticsEvent): AnalyticsRequest {
        val common = CommonFields(
            name = event.name,
            userId = externalUserId(),
            sessionId = sessionIdProvider(),
            storeId = storeIdProvider(),
            eventId = UUID.randomUUID().toString(),
            timeStamp = Clock.System.now().toString()
        )

        return when (event) {
            is TimelineEvent.TimelineAssetViewStartedEvent -> {
                common.toRequest(
                    auxData = mapOfNotNull(
                        "assetType" to event.assetType,
                        "parentUrl" to event.parentUrl,
                        "fixedPosition" to event.fixedPosition,
                        "position" to event.position,
                        "products" to event.products,
                        "customProducts" to event.customProducts,
                        "sponsorshipPlacement" to event.sponsorshipPlacement?.name,
                        "sponsorshipPosition" to event.sponsorshipPosition?.name,
                        "sponsorshipUrl" to event.sponsorshipUrl
                    )
                )
            }

            is TimelineEvent.TimelineAssetViewEndedEvent -> {
                common.toRequest(
                    auxData = mapOfNotNull(
                        "assetType" to event.assetType,
                        "parentUrl" to event.parentUrl,
                        "viewDurationMs" to event.viewDurationMs,
                        "drawerOpenDurationMs" to event.drawerOpenDurationMs,
                        "netoAssetWatchTimeMs" to event.netoAssetWatchTimeMs,
                        "position" to event.position
                    )
                )
            }

            is TimelineEvent.TimelineOpenedEvent -> {
                common.toRequest(
                    auxData = mapOfNotNull(
                        "parentUrl" to event.parentUrl,
                        "sponsorshipPlacement" to event.sponsorshipPlacement?.name,
                        "sponsorshipPosition" to event.sponsorshipPosition?.name,
                        "sponsorshipUrl" to event.sponsorshipUrl
                    )
                )
            }

            is TimelineEvent.TimelineClosedEvent -> {
                common.toRequest(
                    auxData = mapOfNotNull(
                        "parentUrl" to event.parentUrl,
                        "totalOpenDurationMs" to event.totalOpenDurationMs,
                        "activeViewDurationMs" to event.activeViewDurationMs
                    )
                )
            }

            is TimelineEvent.TimelinePollVotedEvent -> {
                common.toRequest(
                    pollId = event.pollId,
                    pollAnswer = event.pollAnswer,
                    auxData = mapOfNotNull(
                        "parentUrl" to event.parentUrl,
                        "questionId" to event.questionId,
                        "position" to event.position
                    )
                )
            }

            is TimelineEvent.TimelineCarouselClickedEvent -> {
                common.toRequest(
                    auxData = mapOfNotNull(
                        "assetId" to event.assetId,
                        "assetIds" to event.assetIds,
                        "assetTimestamps" to event.assetTimestamps,
                        "totalAssets" to event.totalAssets,
                        "parentUrl" to event.parentUrl,
                        "position" to event.position
                    )
                )
            }
            is TimelineEvent.TimelineAssetLikedEvent -> {
                common.toRequest(
                    auxData = mapOfNotNull(
                        "parentUrl" to event.parentUrl,
                        "customProducts" to event.customProducts,
                        "position" to event.position
                    )
                )
            }

            is TimelineEvent.TimelineAssetSharedEvent -> {
                common.toRequest(
                    auxData = mapOfNotNull(
                        "parentUrl" to event.parentUrl,
                        "customProducts" to event.customProducts,
                        "position" to event.position
                    )
                )
            }

            is TimelineEvent.TimelineCarouselViewedEvent -> {
                common.toRequest(
                    auxData = mapOfNotNull(
                        "assetIds" to event.assetIds,
                        "assetTimestamps" to event.assetTimestamps,
                        "parentUrl" to event.parentUrl,
                        "totalAssets" to event.totalAssets,
                        "position" to event.position
                    )
                )
            }

            is TimelineEvent.TimelineCarouselHoveredEvent -> {
                common.toRequest(
                    auxData = mapOfNotNull(
                        "parentUrl" to event.parentUrl,
                        "totalAssets" to event.totalAssets,
                        "assetId" to event.assetId,
                        "assetIds" to event.assetIds,
                        "assetTimestamps" to event.assetTimestamps,
                        "customProducts" to event.customProducts,
                        "position" to event.position
                    )
                )
            }

            is TimelineEvent.TimelineCarouselLoadedEvent -> {
                common.toRequest(
                    auxData = mapOfNotNull(
                        "assetIds" to event.assetIds,
                        "assetTimestamps" to event.assetTimestamps,
                        "parentUrl" to event.parentUrl,
                        "totalAssets" to event.totalAssets,
                        "position" to event.position
                    )
                )
            }

            is TimelineEvent.TimelineCarouselAssetViewedEvent -> {
                common.toRequest(
                    auxData = mapOfNotNull(
                        "assetTimestamp" to event.assetTimestamp,
                        "fixedPosition" to event.fixedPosition,
                        "isInitialView" to event.isInitialView,
                        "parentUrl" to event.parentUrl,
                        "customProducts" to event.customProducts,
                        "position" to event.position
                    )
                )
            }

            is TimelineEvent.TimelineCTAButtonClickedEvent -> {
                common.toRequest(
                    auxData = mapOfNotNull(
                        "parentUrl" to event.parentUrl,
                        "terrificClickId" to event.terrificClickId,
                        "customProducts" to event.customProducts,
                        "position" to event.position,
                        "url" to event.url,
                        "targetUrl" to event.targetUrl
                    )
                )
            }

            is TimelineEvent.TimelineProductClickedEvent -> {
                common.toRequest(
                    auxData = mapOfNotNull(
                        "parentUrl" to event.parentUrl,
                        "customProducts" to event.customProducts,
                        "position" to event.position
                    )
                )
            }

            is TimelineEvent.TimelineCarouselSponsorshipClickedEvent -> {
                common.toRequest(
                    auxData = mapOfNotNull(
                        "parentUrl" to event.parentUrl,
                        "sponsorshipPlacement" to event.sponsorshipPlacement.name,
                        "sponsorshipUrl" to event.sponsorshipUrl
                    )
                )
            }

            is TimelineEvent.TimelineAssetSponsorshipClickedEvent -> {
                common.toRequest(
                    auxData = mapOfNotNull(
                        "parentUrl" to event.parentUrl,
                        "sponsorshipPlacement" to event.sponsorshipPlacement.name,
                        "sponsorshipPosition" to event.sponsorshipPosition?.name,
                        "clickPosition" to event.clickPosition,
                        "sponsorshipUrl" to event.sponsorshipUrl
                    )
                )
            }

            else -> {

            }
        } as AnalyticsRequest
    }
}

private data class CommonFields(
    val name: String,
    val userId: String,
    val sessionId: String,
    val storeId: String,
    val eventId: String,
    val timeStamp: String
) {
    fun toRequest(
        auxData: Map<String, Any?>,
        pollId: String? = null,
        pollAnswer: String? = null
    ) = AnalyticsRequest(
        name = name,
        userId = userId,
        sessionId = sessionId,
        storeId = storeId,
        eventId = eventId,
        timeStamp = timeStamp,
        auxData = auxData,
        pollId = pollId,
        pollAnswer = pollAnswer
    )
}

private fun mapOfNotNull(
    vararg values: Pair<String, Any?>
): Map<String, Any> {
    return values
        .filter { it.second != null }
        .associate { it.first to requireNotNull(it.second) }
}