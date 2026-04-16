package demo.terrific.compose.analytics

import demo.terrific.compose.model.analytics.AuxData
import demo.terrific.compose.model.analytics.UserEventRequest
import demo.terrific.compose.repository.analytics.TerrificAnalyticsRepository
import demo.terrific.compose.storage.analytics.AnalyticsSessionStorage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

internal class TerrificAnalyticsManager(
    private val storeId: String,
    private val parentUrl: String,
    private val sessionStorage: AnalyticsSessionStorage,
    private val repository: TerrificAnalyticsRepository,
    private val analyticsListenerProvider: () -> VideoSdkAnalyticsListener?
) {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    fun trackEvent(
        event: AnalyticsEvent,
        auxData: AuxData
    ) {
        val request = UserEventRequest(
            userId = sessionStorage.getOrCreateUserId(),
            sessionId = sessionStorage.getOrCreateSessionId(storeId),
            name = event.name,
            auxData = auxData
        )

        analyticsListenerProvider()?.onAnalyticsEventTracked(request)

//        scope.launch {
//            repository.sendUserEvent(storeId, request)
//        }
    }

    fun trackTimelineAssetViewStarted(
        assetType: String,
        position: Int,
        fixedPosition: Int = position,
        products: List<String> = emptyList(),
        customProducts: List<String> = emptyList()
    ) {
        val request = UserEventRequest(
            userId = sessionStorage.getOrCreateUserId(),
            sessionId = sessionStorage.getOrCreateSessionId(storeId),
            auxData = AuxData(
                assetType = assetType,
                customProducts = customProducts,
                parentUrl = parentUrl,
                fixedPosition = fixedPosition,
                products = products,
                position = position
            ),
            name = AnalyticsEvent.TimelineAssetViewStarted.name
        )

        scope.launch {
            repository.sendUserEvent(
                storeId = storeId,
                request = request
            )
        }
    }
}