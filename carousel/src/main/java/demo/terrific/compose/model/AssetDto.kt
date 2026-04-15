package demo.terrific.compose.model

import kotlinx.serialization.Serializable

@Serializable
data class AssetsResponse(
    val assets: List<AssetDto>,
    val carouselConfig: CarouselConfigDto
)

@Serializable
data class CarouselConfigDto (
    val carouselAutoPlay: String?,
    val carouselAutoPlayInterval: String?,
    val name: String?,
    val showName: String?,
    val showTimestamps: String?,
    val timestampFormat: String?
)

@Serializable
data class AssetDto(
    val id: String,
    val name: String?,
    val type: String?,
    val title: String?,
    val description: String?,
    val timestamp: String?,
    val media: MediaDto?,
    val pollData: PollDataDto?,
    val products: List<ProductDto>?
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
@Serializable
data class ProductDto(
    val id: String,
    val name: String,
    val description: String?,
    val imageUrl: String?,
    val externalUrl: String?,
    val type: String?,
    val price: Double?,
    val formattedPrice: String?,
    val currency: String?,
    val badge: ProductBadgeDto?,
    val ctaButton: ProductButtonDto?,
    val background: ProductBackgroundDto?
)

@Serializable
data class ProductBadgeDto(
    val text: String?,
    val color: String?,
    val textColor: String?
)

@Serializable
data class ProductButtonDto(
    val text: String?,
    val color: String?,
    val textColor: String?
)

@Serializable
data class ProductBackgroundDto(
    val color: String?,
    val textColor: String?
)

@Serializable
enum class AssetType(val type: String) {
    POLL("poll"),
    IMAGE("image"),
    VIDEO("video");

    companion object {
        fun from(type: String): AssetType {
            return entries.find { it.type == type }
                ?: throw IllegalArgumentException("Unknown asset type: $type")
        }
    }
}