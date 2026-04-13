package demo.terrific.compose.analytics

internal sealed class AnalyticsEvent(val name: String) {

    // ===== GENERAL =====
    object UserClickedGoToProduct : AnalyticsEvent("UserClickedGoToProduct")
    object ChatMessagePinned : AnalyticsEvent("ChatMessagePinned")
    object ChatMessageUnpinned : AnalyticsEvent("ChatMessageUnpinned")
    object UserAddedSessionToCalendar : AnalyticsEvent("UserAddedSessionToCalendar")
    object UserSetSessionReminder : AnalyticsEvent("UserSetSessionReminder")
    object ItemViewed : AnalyticsEvent("ItemViewed")
    object HostFeaturedItem : AnalyticsEvent("HostFeaturedItem")
    object ItemAddedToCart : AnalyticsEvent("ItemAddedToCart")
    object ItemRemovedFromCartByUser : AnalyticsEvent("ItemRemovedFromCartByUser")

    // ===== TIMELINE =====
    object TimelineAssetViewStarted : AnalyticsEvent("TimelineAssetViewStarted")
    object TimelineAssetViewEnded : AnalyticsEvent("TimelineAssetViewEnded")
    object TimelineOpened : AnalyticsEvent("TimelineOpened")
    object TimelineClosed : AnalyticsEvent("TimelineClosed")
    object TimelineAssetLiked : AnalyticsEvent("TimelineAssetLiked")
    object TimelineAssetShared : AnalyticsEvent("TimelineAssetShared")
    object TimelinePollVoted : AnalyticsEvent("TimelinePollVoted")

    object TimelineCarouselHovered : AnalyticsEvent("TimelineCarouselHovered")
    object TimelineCarouselClicked : AnalyticsEvent("TimelineCarouselClicked")
    object TimelineCarouselViewed : AnalyticsEvent("TimelineCarouselViewed")
    object TimelineCarouselLoaded : AnalyticsEvent("TimelineCarouselLoaded")
    object TimelineCarouselAssetViewed : AnalyticsEvent("TimelineCarouselAssetViewed")
    object TimelineCTAButtonClicked : AnalyticsEvent("TimelineCTAButtonClicked")

    object TimelineProductAddedToCart : AnalyticsEvent("TimelineProductAddedToCart")
    object TimelineProductClicked : AnalyticsEvent("TimelineProductClicked")
    object TimelineGoToProduct : AnalyticsEvent("TimelineGoToProduct")
    object TimelineProductDrawerOpened : AnalyticsEvent("TimelineProductDrawerOpened")
    object TimelineProductDrawerClosed : AnalyticsEvent("TimelineProductDrawerClosed")

    // ===== ADS =====
    object TimelineAdImpression : AnalyticsEvent("TimelineAdImpression")
    object TimelineAdClick : AnalyticsEvent("TimelineAdClick")
    object TimelineAdError : AnalyticsEvent("TimelineAdError")
}