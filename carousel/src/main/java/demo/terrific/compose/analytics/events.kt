package demo.terrific.compose.analytics

import demo.terrific.compose.analytics.basemodels.AnalyticsAssetType
import demo.terrific.compose.analytics.basemodels.HorizontalSponsorshipData
import demo.terrific.compose.analytics.basemodels.HorizontalSponsorshipPlacement
import demo.terrific.compose.analytics.basemodels.SponsorshipPosition
import demo.terrific.compose.analytics.basemodels.TimelineCustomProductEventData
import demo.terrific.compose.analytics.basemodels.TimelineProductAnalyticsData
import demo.terrific.compose.analytics.basemodels.VerticalSponsorshipData
import demo.terrific.compose.analytics.basemodels.VerticalSponsorshipPlacement

data class TimelineOpenedRequest(
    override val userId: String,
    override val sessionId: String,
    val auxData: TimelineOpenedAuxData,
) : AnalyticsEventRequest {

    override val name: String = "TimelineOpened"
}

data class TimelineOpenedAuxData(
    val parentUrl: String?,
    val externalUserId: String? = null,
    override val sponsorshipPlacement: VerticalSponsorshipPlacement? = null,
    override val sponsorshipPosition: SponsorshipPosition? = null,
    override val sponsorshipUrl: String? = null,
) : VerticalSponsorshipData

//====================================

data class TimelineClosedRequest(
    override val userId: String,
    override val sessionId: String,
    val auxData: TimelineClosedAuxData,
) : AnalyticsEventRequest {

    override val name: String = "TimelineClosed"
}

data class TimelineClosedAuxData(
    val parentUrl: String?,
    val externalUserId: String? = null,
    val totalOpenDurationMs: Long? = null,
    val activeViewDurationMs: Long? = null,
    override val sponsorshipPlacement: VerticalSponsorshipPlacement? = null,
    override val sponsorshipPosition: SponsorshipPosition? = null,
    override val sponsorshipUrl: String? = null,
) : VerticalSponsorshipData

//====================================

data class TimelineCarouselClickedRequest(
    override val userId: String,
    override val sessionId: String,
    val auxData: TimelineCarouselClickedAuxData,
) : AnalyticsEventRequest {

    override val name: String = "TimelineCarouselClicked"
}

data class TimelineCarouselClickedAuxData(
    val assetId: String,
    val assetIds: List<String>,
    val assetTimestamps: List<String>,
    val parentUrl: String?,
    val totalAssets: Int,
    val externalUserId: String? = null,
    val brandName: String? = null,
    val campaignName: String? = null,
    val customProducts: List<TimelineCustomProductEventData>? = null,
    val position: Int? = null,
    override val sponsorshipPlacement: HorizontalSponsorshipPlacement? = null,
    override val sponsorshipUrl: String? = null,
) : HorizontalSponsorshipData

//====================================

data class TimelineCarouselViewedRequest(
    override val userId: String,
    override val sessionId: String,
    val auxData: TimelineCarouselViewedAuxData,
) : AnalyticsEventRequest {

    override val name: String = "TimelineCarouselViewed"
}

data class TimelineCarouselViewedAuxData(
    val assetIds: List<String>,
    val assetTimestamps: List<String>,
    val parentUrl: String?,
    val totalAssets: Int,
    val externalUserId: String? = null,
    val position: Int? = null,
    override val sponsorshipPlacement: HorizontalSponsorshipPlacement? = null,
    override val sponsorshipUrl: String? = null,
) : HorizontalSponsorshipData

//====================================

data class TimelineCarouselLoadedRequest(
    override val userId: String,
    override val sessionId: String,
    val auxData: TimelineCarouselLoadedAuxData,
) : AnalyticsEventRequest {

    override val name: String = "TimelineCarouselLoaded"
}

data class TimelineCarouselLoadedAuxData(
    val assetIds: List<String>,
    val assetTimestamps: List<String>,
    val parentUrl: String?,
    val totalAssets: Int,
    val externalUserId: String? = null,
    val position: Int? = null,
    override val sponsorshipPlacement: HorizontalSponsorshipPlacement? = null,
    override val sponsorshipUrl: String? = null,
) : HorizontalSponsorshipData

//====================================

data class TimelineCarouselAssetViewedRequest(
    override val userId: String,
    override val sessionId: String,
    val auxData: TimelineCarouselAssetViewedAuxData,
) : AnalyticsEventRequest {

    override val name: String = "TimelineCarouselAssetViewed"
}

data class TimelineCarouselAssetViewedAuxData(
    val assetTimestamp: String,
    val fixedPosition: Int?,
    val isInitialView: Boolean,
    val parentUrl: String?,
    val brandName: String? = null,
    val campaignName: String? = null,
    val customProducts: List<TimelineCustomProductEventData>? = null,
    val position: Int? = null,
    override val sponsorshipPlacement: HorizontalSponsorshipPlacement? = null,
    override val sponsorshipUrl: String? = null,
) : HorizontalSponsorshipData

//====================================

data class TimelineAssetViewStartedRequest(
    override val userId: String,
    override val sessionId: String,
    val auxData: TimelineAssetViewStartedAuxData,
) : AnalyticsEventRequest {

    override val name: String = "TimelineAssetViewStarted"
}

data class TimelineAssetViewStartedAuxData(
    val assetType: AnalyticsAssetType,
    val parentUrl: String?,
    val fixedPosition: Int?,
    val externalUserId: String? = null,
    val brandName: String? = null,
    val campaignName: String? = null,
    val position: Int? = null,
    val products: List<TimelineProductAnalyticsData>? = null,
    val customProducts: List<TimelineCustomProductEventData>? = null,
    override val sponsorshipPlacement: VerticalSponsorshipPlacement? = null,
    override val sponsorshipPosition: SponsorshipPosition? = null,
    override val sponsorshipUrl: String? = null,
) : VerticalSponsorshipData

//====================================

data class TimelineAssetViewEndedRequest(
    override val userId: String,
    override val sessionId: String,
    val auxData: TimelineAssetViewEndedAuxData,
) : AnalyticsEventRequest {

    override val name: String = "TimelineAssetViewEnded"
}

data class TimelineAssetViewEndedAuxData(
    val assetType: AnalyticsAssetType,
    val parentUrl: String?,
    val externalUserId: String? = null,
    val viewDurationMs: Long? = null,
    val drawerOpenDurationMs: Long? = null,
    val netoAssetWatchTimeMs: Long? = null,
    val brandName: String? = null,
    val campaignName: String? = null,
    val position: Int? = null,
    val products: List<TimelineProductAnalyticsData>? = null,
    val customProducts: List<TimelineCustomProductEventData>? = null,
    override val sponsorshipPlacement: VerticalSponsorshipPlacement? = null,
    override val sponsorshipPosition: SponsorshipPosition? = null,
    override val sponsorshipUrl: String? = null,
) : VerticalSponsorshipData

//====================================

