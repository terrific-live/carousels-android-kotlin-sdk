package demo.terrific.compose.model

import kotlinx.serialization.Serializable

@Serializable
data class AssetsResponse(
    val assets: List<AssetDto>
)

@Serializable
data class AssetDto(
    val id: String,
    val name: String?,
    val type: String,
    val title: String?,
    val description: String?,
    val timestamp: String?,
    val media: MediaDto?,
    val pollData: PollDataDto?,
)

@Serializable
data class PollDataDto(
    val id: String,
    val question: String,
    val questionId: String,
    val options: List<PollOptionDto>
)

@Serializable
data class PollOptionDto(
    val text: String,
    val numberOfVotes: Int
)

@Serializable
data class MediaDto(
    val coverUrl: String?,
    val desktopUrl: String?,
    val mobileUrl: String?,
    val srcUrl: String?,
    val videoPreviewUrl: String?
)