package demo.terrific.compose.analytics

sealed interface AnalyticsEventRequest {
    val name: String
    val userId: String
    val sessionId: String
}