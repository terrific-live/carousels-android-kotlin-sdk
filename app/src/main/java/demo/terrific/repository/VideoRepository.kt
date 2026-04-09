package demo.terrific.repository

import demo.terrific.api.VideosApi
import demo.terrific.model.AssetDto
import javax.inject.Inject

class VideoRepository @Inject constructor(
    private val api: VideosApi
) {
    suspend fun loadAssets(): List<AssetDto> {
        return api.getAssets(
            storeId = "0bor4CHMEbm3M4Dluput",
            carouselId = "HmUOF0rG4fO1v9U63t7Z",
            numberOfItems = 20,
            isRedirect = false,
//            shopPageUrl = "https://www.france.tv/jeux-et-divertissements/",
            userId = "019c239e-914c-70f9-b4b5-cc90575480cb"
        ).assets
    }

    suspend fun loadVerticalAssets(): List<AssetDto> {
        return api.getVerticalAssets(
            storeId = "0bor4CHMEbm3M4Dluput",
            displayId = "HmUOF0rG4fO1v9U63t7Z",
            numberOfItems = 20,
            isRedirect = false,
//            shopPageUrl = "https://www.france.tv/jeux-et-divertissements/",
            userId = "019c239e-914c-70f9-b4b5-cc90575480cb"
        ).assets
    }


//            VideoItem(
//                4,
//                "",
//                thumbnail = "",
//                question = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.",
//                options = listOf(
//                    PollOption(answerText = "Yes", numberOfVotes = 3, answerIndex = 0),
//                    PollOption(answerText = "No", numberOfVotes = 4, answerIndex = 1),
//                    PollOption(answerText = "Maybe", numberOfVotes = 4, answerIndex = 2)
//                )

}