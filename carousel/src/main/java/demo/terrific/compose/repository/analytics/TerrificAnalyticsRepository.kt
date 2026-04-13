package demo.terrific.compose.repository.analytics

import demo.terrific.compose.analytics.AnalyticsDebugStore
import demo.terrific.compose.model.analytics.UserEventRequest
import demo.terrific.compose.network.TerrificAnalyticsApi

internal class TerrificAnalyticsRepository(
    private val api: TerrificAnalyticsApi
) {

    suspend fun sendUserEvent(
        storeId: String,
        request: UserEventRequest
    ): Result<Unit> {
        return try {

            AnalyticsDebugStore.add(
                name = request.name,
                details = buildString {
                    append("assetType: ${request.auxData.assetType}")
                    append(", position: ${request.auxData.position}")
                    append(", fixedPosition: ${request.auxData.fixedPosition}")
                }
            )

            val response = api.sendUserEvent(
                storeId = storeId,
                body = request
            )

            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(
                    IllegalStateException(
                        "Analytics request failed: code=${response.code()}"
                    )
                )
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}