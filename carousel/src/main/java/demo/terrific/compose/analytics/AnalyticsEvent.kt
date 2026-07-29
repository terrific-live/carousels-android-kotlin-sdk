package demo.terrific.compose.analytics

import demo.terrific.compose.analytics.basemodels.HorizontalSponsorshipPlacement
import demo.terrific.compose.analytics.basemodels.SponsorshipPosition
import demo.terrific.compose.analytics.basemodels.VerticalSponsorshipPlacement

interface AnalyticsEvent {
    val name: String
}

sealed interface TimelineEvent : AnalyticsEvent {

    data class TimelineAssetViewStartedEvent(
        val assetType: String?,
        val parentUrl: String?,
        val fixedPosition: Int?,
        val position: Int? = null,
        val products: List<String>? = null,
        val customProducts: List<String>? = null,
        val sponsorshipPlacement: VerticalSponsorshipPlacement? = null,
        val sponsorshipPosition: SponsorshipPosition? = null,
        val sponsorshipUrl: String? = null
    ) : AnalyticsEvent {
        override val name = "TimelineAssetViewStarted"
    }

    data class TimelineAssetViewEndedEvent(
        val assetType: String?,
        val parentUrl: String?,
        val externalUserId: String? = null,
        val viewDurationMs: Long? = null,
        val drawerOpenDurationMs: Long? = null,
        val netoAssetWatchTimeMs: Long? = null,
        val position: Int? = null,
        val products: List<String>? = null,
        val customProducts: List<String>? = null,
        val sponsorshipPlacement: VerticalSponsorshipPlacement? = null,
        val sponsorshipPosition: SponsorshipPosition? = null,
        val sponsorshipUrl: String? = null
    ) : AnalyticsEvent {
        override val name = "TimelineAssetViewEnded"
    }

    data class TimelineOpenedEvent(
        val parentUrl: String?,
        val sponsorshipPlacement: VerticalSponsorshipPlacement? = null,
        val sponsorshipPosition: SponsorshipPosition? = null,
        val sponsorshipUrl: String? = null
    ) : AnalyticsEvent {
        override val name = "TimelineOpened"
    }

    data class TimelineClosedEvent(
        val parentUrl: String?,
        val totalOpenDurationMs: Long? = null,
        val activeViewDurationMs: Long? = null,
        val sponsorshipPlacement: VerticalSponsorshipPlacement? = null,
        val sponsorshipPosition: SponsorshipPosition? = null,
        val sponsorshipUrl: String? = null
    ) : AnalyticsEvent {
        override val name = "TimelineClosed"
    }

    data class TimelineAssetLikedEvent(
        val parentUrl: String?,
        val customProducts: List<String>? = null,
        val position: Int? = null,
        val sponsorshipPlacement: VerticalSponsorshipPlacement? = null,
        val sponsorshipPosition: SponsorshipPosition? = null,
        val sponsorshipUrl: String? = null
    ) : AnalyticsEvent {
        override val name = "TimelineAssetLiked"
    }

    data class TimelineAssetSharedEvent(
        val parentUrl: String?,
        val customProducts: List<String>? = null,
        val position: Int? = null,
        val sponsorshipPlacement: VerticalSponsorshipPlacement? = null,
        val sponsorshipPosition: SponsorshipPosition? = null,
        val sponsorshipUrl: String? = null
    ) : AnalyticsEvent {
        override val name = "TimelineAssetShared"
    }

    data class TimelinePollVotedEvent(
        val pollId: String,
        val pollAnswer: String,
        val parentUrl: String?,
        val questionId: String,
        val position: Int? = null,
        val sponsorshipPlacement: VerticalSponsorshipPlacement? = null,
        val sponsorshipPosition: SponsorshipPosition? = null,
        val sponsorshipUrl: String? = null
    ) : AnalyticsEvent {
        override val name = "TimelinePollVoted"
    }

    data class TimelineCarouselViewedEvent(
        val assetIds: List<String>,
        val assetTimestamps: List<String>,
        val parentUrl: String?,
        val totalAssets: Int,
        val position: Int? = null,
        val sponsorshipPlacement: HorizontalSponsorshipPlacement? = null,
        val sponsorshipUrl: String? = null
    ) : AnalyticsEvent {
        override val name = "TimelineCarouselViewed"
    }

    data class TimelineCarouselHoveredEvent(
        val parentUrl: String?,
        val totalAssets: Int,
        val assetId: String,
        val assetIds: List<String>,
        val assetTimestamps: List<String>,
        val externalUserId: String? = null,
        val customProducts: List<String>? = null,
        val position: Int? = null,
        val sponsorshipPlacement: HorizontalSponsorshipPlacement? = null,
        val sponsorshipUrl: String? = null
    ) : AnalyticsEvent {
        override val name = "TimelineCarouselHovered"
    }

    data class TimelineCarouselClickedEvent(
        val assetId: String,
        val assetIds: List<String>,
        val assetTimestamps: List<String>,
        val parentUrl: String?,
        val totalAssets: Int,
        val customProducts: List<String>? = null,
        val position: Int? = null,
        val sponsorshipPlacement: HorizontalSponsorshipPlacement? = null,
        val sponsorshipUrl: String? = null
    ) : AnalyticsEvent {
        override val name = "TimelineCarouselClicked"
    }

    data class TimelineCarouselLoadedEvent(
        val assetIds: List<String>,
        val assetTimestamps: List<String>,
        val parentUrl: String?,
        val totalAssets: Int,
        val externalUserId: String? = null,
        val position: Int? = null,
        val sponsorshipPlacement: HorizontalSponsorshipPlacement? = null,
        val sponsorshipUrl: String? = null
    ) : AnalyticsEvent {
        override val name = "TimelineCarouselLoaded"
    }

    data class TimelineCarouselAssetViewedEvent(
        val assetTimestamp: String,
        val fixedPosition: Int?,
        val isInitialView: Boolean,
        val parentUrl: String?,
        val customProducts: List<String>? = null,
        val position: Int? = null,
        val sponsorshipPlacement: HorizontalSponsorshipPlacement? = null,
        val sponsorshipUrl: String? = null
    ) : AnalyticsEvent {
        override val name = "TimelineCarouselAssetViewed"
    }

    data class TimelineCTAButtonClickedEvent(
        val parentUrl: String?,
        val terrificClickId: String? = null,
        val customProducts: List<String>? = null,
        val position: Int? = null,
        val url: String? = null,
        val targetUrl: String? = null,
        val sponsorshipPlacement: VerticalSponsorshipPlacement? = null,
        val sponsorshipPosition: SponsorshipPosition? = null,
        val sponsorshipUrl: String? = null
    ) : AnalyticsEvent {
        override val name = "TimelineCTAButtonClicked"
    }

    data class TimelineProductClickedEvent(
        val itemViewSource: String,
        val product: String,
        val parentUrl: String?,
        val items: List<String>? = null,
        val customProducts: List<String>? = null,
        val position: Int? = null,
        val sponsorshipPlacement: VerticalSponsorshipPlacement? = null,
        val sponsorshipPosition: SponsorshipPosition? = null,
        val sponsorshipUrl: String? = null
    ) : AnalyticsEvent {
        override val name = "TimelineProductClicked"
    }

    data class TimelineCarouselSponsorshipClickedEvent(
        val parentUrl: String?,
        val sponsorshipPlacement: HorizontalSponsorshipPlacement,
        val sponsorshipUrl: String? = null
    ) : AnalyticsEvent {
        override val name = "TimelineCarouselSponsorshipClicked"
    }

    data class TimelineAssetSponsorshipClickedEvent(
        val parentUrl: String?,
        val sponsorshipPlacement: VerticalSponsorshipPlacement,
        val sponsorshipPosition: SponsorshipPosition?,
        val clickPosition: Int? = null,
        val sponsorshipUrl: String? = null
    ) : AnalyticsEvent {
        override val name = "TimelineAssetSponsorshipClicked"
    }

// endregion

// region Session events

    sealed interface SessionAnalyticsEvent : AnalyticsEvent {
        val sessionState: String?
        val role: String?
    }

    data class UserClickedGoToProductEvent(
        val productId: String?,
        val externalUrl: String,
        val terrificProductId: String?,
        override val sessionState: String? = null,
        override val role: String? = null,
        val position: Int? = null
    ) : SessionAnalyticsEvent {
        override val name = "UserClickedGoToProduct"
    }

    data class PollVotedEvent(
        val pollId: String,
        val pollAnswer: String,
        override val sessionState: String? = null,
        override val role: String? = null
    ) : SessionAnalyticsEvent {
        override val name = "PollVoted"
    }

    data class PollViewedEvent(
        val pollId: String,
        val pollStatus: Int,
        override val sessionState: String? = null,
        override val role: String? = null
    ) : SessionAnalyticsEvent {
        override val name = "PollViewed"
    }

    data class ShareLinkClickedEvent(
        val shareLinkId: String,
        val shareTarget: String = "session",
        val refererUserId: String,
        val referralSource: String = "Other",
        override val sessionState: String? = null,
        override val role: String? = null,
        val externalUrl: String? = null
    ) : SessionAnalyticsEvent {
        override val name = "ShareLinkClicked"
    }

// endregion
}