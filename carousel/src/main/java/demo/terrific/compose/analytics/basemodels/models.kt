package demo.terrific.compose.analytics.basemodels

data class TimelineCustomProductEventData(
    val name: String,
    val externalURL: String,
    val price: String? = null,
    val currency: String? = null,
    val description: String? = null,
)

data class TimelineProductAnalyticsData(
    val id: String?,
    val sku: String?,
    val categories: List<String>,
    val tags: List<String>,
)

enum class AnalyticsAssetType {
    video,
    poll,
    image
}

enum class HorizontalSponsorshipPlacement {
    TopLogo,
    SideLogo
}

enum class VerticalSponsorshipPlacement {
    badgeLogo,
    bannerLogo,
    pollLogo
}

enum class SponsorshipPosition {
    top,
    bottom,
    both
}

interface HorizontalSponsorshipData {
    val sponsorshipPlacement: HorizontalSponsorshipPlacement?
    val sponsorshipUrl: String?
}

interface VerticalSponsorshipData {
    val sponsorshipPlacement: VerticalSponsorshipPlacement?
    val sponsorshipPosition: SponsorshipPosition?
    val sponsorshipUrl: String?
}