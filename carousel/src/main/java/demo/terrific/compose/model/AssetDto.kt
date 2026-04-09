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
    val media: MediaDto?
)

@Serializable
data class MediaDto(
    val coverUrl: String?,
    val desktopUrl: String?,
    val mobileUrl: String?,
    val srcUrl: String?,
    val videoPreviewUrl: String?
)