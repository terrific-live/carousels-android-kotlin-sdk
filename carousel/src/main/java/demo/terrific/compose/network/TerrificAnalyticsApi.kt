package demo.terrific.compose.network

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface TerrificAnalyticsApi {


    @POST("userEvents")
    suspend fun sendEvent(
        @Header("terrific-store-id") storeId: String,
        @Body event: Any,
    ): Response<Unit>
}