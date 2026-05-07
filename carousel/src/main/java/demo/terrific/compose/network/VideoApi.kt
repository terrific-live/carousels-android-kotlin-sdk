package demo.terrific.compose.network

import demo.terrific.compose.model.AssetsResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface VideoApi {

    @GET("api/v1/stores/{storeId}/carousel/{carouselId}")
    suspend fun getCarouselAssets(
        @Path("storeId") storeId: String,
        @Path("carouselId") carouselId: String
    ): AssetsResponse

    @GET("api/v1/stores/{storeId}/carousel/{displayId}")
    suspend fun getVerticalAssets(
        @Path("storeId") storeId: String,
        @Path("displayId") displayId: String
    ): AssetsResponse
}