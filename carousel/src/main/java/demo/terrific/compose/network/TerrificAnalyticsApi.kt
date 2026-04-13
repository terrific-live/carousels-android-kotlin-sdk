package demo.terrific.compose.network

import demo.terrific.compose.model.analytics.UserEventRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Url

interface TerrificAnalyticsApi {
    @POST("userEvents")
    suspend fun sendUserEvent(
        @Url url: String = "https://us-central1-terrific-live.cloudfunctions.net/userEvents",
        @Header("terrific-store-id") storeId: String,
        @Header("Accept") accept: String = "application/json",
        @Body body: UserEventRequest
    ): Response<Unit>
}