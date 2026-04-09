package demo.terrific.api

import demo.terrific.model.AssetsResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface VideosApi {

    @GET("api/v1/stores/{storeId}/carousel/{carouselId}")
    suspend fun getAssets(
        @Path("storeId") storeId: String,
        @Path("carouselId") carouselId: String,
        @Query("number-of-items") numberOfItems: Int,
        @Query("is-redirect") isRedirect: Boolean,
//        @Query("shopPageUrl") shopPageUrl: String,
        @Query("terrificUserId") userId: String
    ): AssetsResponse

    @GET("api/v1/stores/{storeId}/display/{displayId}")
    suspend fun getVerticalAssets(
        @Path("storeId") storeId: String,
        @Path("displayId") displayId: String,
        @Query("number-of-items") numberOfItems: Int,
        @Query("is-redirect") isRedirect: Boolean,
//        @Query("shopPageUrl") shopPageUrl: String,
        @Query("terrificUserId") userId: String
    ): AssetsResponse
}