package demo.terrific.compose.repository

import demo.terrific.compose.model.AssetDto
import demo.terrific.compose.model.AssetsResponse

interface VideoRepository {

    suspend fun getFeedCarousel(storeId: String, carouselId: String): List<AssetDto>

    suspend fun getFeed(storeId: String, carouselId: String): AssetsResponse

}