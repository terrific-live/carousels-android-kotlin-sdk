package demo.terrific.compose.analytics

import demo.terrific.compose.analytics.basemodels.HorizontalSponsorshipPlacement
import demo.terrific.compose.analytics.basemodels.SponsorshipPosition
import demo.terrific.compose.analytics.basemodels.VerticalSponsorshipPlacement

sealed interface AnalyticsEventRequest {
    val name: String
    val userId: String
    val sessionId: String
}
data class AnalyticsRequest(
    val name: String,
    val userId: String,
    val sessionId: String,
    val storeId: String,
    val eventId: String,
    val timeStamp: String,
    val auxData: Map<String, Any?>,
    val pollId: String? = null,
    val pollAnswer: String? = null,
    val items: List<Any>? = null,
    val cartId: String? = null,
    val cartItemsCountDelta: Int? = null,
    val cartItemsValueDelta: Double? = null
)