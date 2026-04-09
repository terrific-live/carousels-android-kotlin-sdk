package demo.terrific.compose.model

data class VideoItem(
    val id: Int,
    val videoUrl: String,
    val thumbnail: String,
    val question: String?,
    val options: List<PollOption>?
)
data class PollOption(
    val answerText: String,
    val numberOfVotes: Int,
    val answerIndex: Int
)