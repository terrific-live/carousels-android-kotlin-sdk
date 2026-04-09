package demo.terrific.storage

import android.content.Context
import javax.inject.Inject

class LocalStorage @Inject constructor(
    context: Context
) {

    private val prefs = context.getSharedPreferences("video_prefs", Context.MODE_PRIVATE)

    companion object {
        private const val KEY_LIKED = "liked_videos"
        private const val KEY_MUTED = "is_muted"
    }

    // ❤️ LIKE

    fun getLikedVideos(): Set<String> {
        return prefs.getStringSet(KEY_LIKED, emptySet()) ?: emptySet()
    }

    fun saveLikedVideos(set: Set<String>) {
        prefs.edit().putStringSet(KEY_LIKED, set).apply()
    }

    // 🔊 MUTE

    fun isMuted(): Boolean {
        return prefs.getBoolean(KEY_MUTED, false)
    }

    fun setMuted(value: Boolean) {
        prefs.edit().putBoolean(KEY_MUTED, value).apply()
    }
}