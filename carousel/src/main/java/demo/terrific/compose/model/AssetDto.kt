package demo.terrific.compose.model

import kotlinx.serialization.Serializable

@Serializable
data class AssetsResponse(
    val assets: List<AssetDto>,
    val carouselConfig: CarouselConfigDto
)

@Serializable
data class CarouselConfigDto (
    val carouselAutoPlay: Boolean? = null,
    val carouselAutoPlayInterval: Int? = 0,
    val name: String? = null,
    val showName: String? = null,
    val showTimestamps: String? = null,
    val timestampFormat: String? = null,
    val sponsorship: SponsorshipDto? = null

)

@Serializable
data class SponsorshipDto(
    val adPlacementType: String? = null,
    val backgroundColor: String? = null,
    val badge: SponsorshipBadgeDto? = null,
    val banner: SponsorshipBannerDto? = null,
    val clickRedirect: String? = null,
    val enabled: Boolean = false,
    val poll: SponsorshipPollDto? = null,
    val sideLogoUrl: String? = null,
    val sponsorLabel: String? = null,
    val topLogoUrl: String? = null,
    val verticalClickRedirect: String? = null,
    val verticalEnabled: Boolean = false
)

@Serializable
data class SponsorshipBadgeDto(
    val backgroundColor: String? = null,
    val clickRedirect: String? = null,
    val logoUrl: String? = null,
    val position: String? = null,
    val title: String? = null
)

@Serializable
data class SponsorshipPollDto(
    val adPosition: String? = null,
    val clickRedirect: String? = null,
    val logoUrl: String? = null
)

@Serializable
data class AssetDto(
    val id: String,
    val background: BackgroundDto? = null,
    val name: String? = null,
    val type: String? = null,
    val title: String? = null,
    val description: String? = null,
    val timestamp: String? = null,
    val media: MediaDto? = null,
    val pollData: PollDataDto? = null,
    val products: List<ProductDto>? = null
)
@Serializable
data class SponsorshipBannerDto(
    val imageUrl: String? = null,
    val clickRedirect: String? = null,
    val backgroundColor: String? = null
)
@Serializable
data class BackgroundDto(
    val color: ColorDto? = null,
    val imageUrl: String? = null,
    val type: String? = null
)

@Serializable
data class ColorDto(
    val primary: String? = null,
    val secondary: String? = null
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

}