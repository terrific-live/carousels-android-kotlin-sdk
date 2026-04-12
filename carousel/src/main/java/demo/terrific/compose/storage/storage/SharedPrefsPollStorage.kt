package demo.terrific.compose.storage.storage

import android.content.Context
import androidx.core.content.edit

class SharedPrefsPollStorage(
    context: Context
) : PollStorage {

    private val prefs = context.getSharedPreferences(
        "video_feature_polls",
        Context.MODE_PRIVATE
    )

    override fun saveVote(questionId: String, optionText: String) {
        prefs.edit {
            putString("poll_vote_$questionId", optionText)
        }
    }

    override fun getVote(questionId: String): String? {
        return prefs.getString("poll_vote_$questionId", null)
    }

    override fun hasVoted(questionId: String): Boolean {
        return prefs.contains("poll_vote_$questionId")
    }
}