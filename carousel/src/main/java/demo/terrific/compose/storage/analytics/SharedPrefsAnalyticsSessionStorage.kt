package demo.terrific.compose.storage.analytics

import android.content.Context
import java.util.UUID
import androidx.core.content.edit

internal class SharedPrefsAnalyticsSessionStorage(
    context: Context
) : AnalyticsSessionStorage {

    private val prefs = context.getSharedPreferences(
        "video_sdk_analytics_prefs",
        Context.MODE_PRIVATE
    )

    override fun getOrCreateUserId(): String {
        val existing = prefs.getString(KEY_USER_ID, null)
        if (existing != null) return existing

        val newValue = UUID.randomUUID().toString()
        prefs.edit { putString(KEY_USER_ID, newValue) }
        return newValue
    }

    override fun getOrCreateSessionId(storeId: String): String {
        val existing = prefs.getString(KEY_SESSION_ID, null)
        if (existing != null) return existing

        val newValue = "${UUID.randomUUID()}-$storeId"
        prefs.edit { putString(KEY_SESSION_ID, newValue) }
        return newValue
    }

    private companion object {
        const val KEY_USER_ID = "analytics_user_id"
        const val KEY_SESSION_ID = "analytics_session_id"
    }
}