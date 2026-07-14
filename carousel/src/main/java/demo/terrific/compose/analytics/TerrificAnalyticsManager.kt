package demo.terrific.compose.analytics

import android.util.Log
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

        scope.launch {
//            try {
//                val response = repository.send(storeId, request)
//
//                Log.d("Analytics", "Response code: ${response.code()}")
//
//            } catch (e: Exception) {
//                Log.e("Analytics", "Failed to send analytics", e)
//            }
        }
    }
}