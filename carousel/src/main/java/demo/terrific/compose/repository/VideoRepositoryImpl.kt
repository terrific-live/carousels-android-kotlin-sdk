package demo.terrific.compose.repository

import demo.terrific.compose.model.AssetDto
import demo.terrific.compose.network.VideoApi

class VideoRepositoryImpl(
    private val api: VideoApi
) : VideoRepository {

    override suspend fun getFeedCarousel(storeId: String, carouselId: String): List<AssetDto> {
        return api.getCarouselAssets(
            storeId = storeId,
            carouselId = carouselId
        ).assets
    }

    override suspend fun getFeed(storeId: String, carouselId: String): List<AssetDto> {
        return api.getVerticalAssets(
            storeId = storeId,
            displayId = carouselId
        ).assets
    }

}