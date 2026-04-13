package demo.terrific.compose.analytics

data class AnalyticsDebugEvent(
    val timestamp: String,
    val name: String,
    val details: String
)