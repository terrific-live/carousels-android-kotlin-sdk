package demo.terrific.repository

import demo.terrific.model.PollOption
import demo.terrific.model.VideoItem
import javax.inject.Inject

class VideoRepository @Inject constructor() {

    fun getVideos(): List<VideoItem> {
        return listOf(
            VideoItem(
                0,
                "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4",
                thumbnail = "http://i3.ytimg.com/vi/erLk59H86ww/hqdefault.jpg",
                question = null,
                options = null
            ),
            VideoItem(
                1,
                "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/Sintel.mp4",
                thumbnail = "http://i3.ytimg.com/vi/erLk59H86ww/hqdefault.jpg",
                question = null,
                options = null
            ),
            VideoItem(
                2,
                "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ElephantsDream.mp4",
                thumbnail = "http://i3.ytimg.com/vi/erLk59H86ww/hqdefault.jpg",
                question = null,
                options = null
            ),
            VideoItem(
                3,
                "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerBlazes.mp4",
                thumbnail = "http://i3.ytimg.com/vi/erLk59H86ww/hqdefault.jpg",
                question = null,
                options = null
            ),
            VideoItem(
                4,
                "",
                thumbnail = "",
                question = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.",
                options = listOf(
                    PollOption(answerText = "Yes", numberOfVotes = 3, answerIndex = 0),
                    PollOption(answerText = "No", numberOfVotes = 4, answerIndex = 1),
                    PollOption(answerText = "Maybe", numberOfVotes = 4, answerIndex = 2)
                )
            )
        )
    }
}