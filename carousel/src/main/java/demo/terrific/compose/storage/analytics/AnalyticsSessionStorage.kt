package demo.terrific.compose.storage.analytics

interface AnalyticsSessionStorage {
    fun getOrCreateUserId(): String
    fun getOrCreateSessionId(storeId: String): String
}