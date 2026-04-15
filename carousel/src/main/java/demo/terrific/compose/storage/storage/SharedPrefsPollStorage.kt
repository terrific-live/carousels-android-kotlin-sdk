package demo.terrific.compose.storage.storage

import android.content.Context
import androidx.core.content.edit
import com.google.gson.Gson
import demo.terrific.compose.model.PollOptionDto

class SharedPrefsPollStorage(
    context: Context
): PollStorage {
    private val prefs = context.getSharedPreferences(
        "video_feature_polls",
        Context.MODE_PRIVATE
    )

    private val gson = Gson()

    override fun savePollState(
        questionId: String,
        selectedOptionText: String,
        options: List<PollOptionDto>
    ) {
        val savedState = SavedPollState(
            selectedOptionText = selectedOptionText,
            options = options.map {
                SavedPollOption(
                    text = it.text,
                    numberOfVotes = it.numberOfVotes
                )
            }
        )

        prefs.edit {
            putString(key(questionId), gson.toJson(savedState))
        }
    }

    override fun getSavedPollState(questionId: String): SavedPollState? {
        val json = prefs.getString(key(questionId), null) ?: return null
        return runCatching {
            gson.fromJson(json, SavedPollState::class.java)
        }.getOrNull()
    }

    private fun key(questionId: String): String = "poll_state_$questionId"
}