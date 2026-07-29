package demo.terrific.compose.analytics

import AnalyticsEventMapper
import android.util.Log
import demo.terrific.compose.network.TerrificAnalyticsApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class VideoAnalytics internal constructor(
    private val storeId: String,
    private val api: TerrificAnalyticsApi,
    private val mapper: AnalyticsEventMapper,
    private val scope: CoroutineScope
) {

    fun sendEvent(event: AnalyticsEvent) {
        scope.launch {
            runCatching {
                api.sendEvent(storeId, mapper.map(event))
            }.onFailure { error ->
                Log.e(TAG, "Failed to send ${event.name}", error)
            }
        }
    }

    private companion object {
        const val TAG = "VideoAnalytics"
    }
}