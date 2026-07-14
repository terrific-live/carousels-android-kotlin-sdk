package demo.terrific.compose.repository.analytics

import demo.terrific.compose.analytics.AnalyticsEventRequest
import demo.terrific.compose.network.TerrificAnalyticsApi

class TerrificAnalyticsRepository(
    private val api: TerrificAnalyticsApi,
    private val storeId: String,
) {

    suspend fun send(event: AnalyticsEventRequest): Result<Unit> {
        return runCatching {
            val response = api.sendEvent(
                storeId = storeId,
                event = event
            )

            if (!response.isSuccessful) {
                error(
                    "Analytics error ${response.code()}: " +
                            response.errorBody()?.string()
                )
            }
        }
    }
}