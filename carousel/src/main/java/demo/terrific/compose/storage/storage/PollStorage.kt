package demo.terrific.compose.storage.storage

import demo.terrific.compose.model.PollOptionDto

interface PollStorage {
    fun savePollState(
        questionId: String,
        selectedOptionText: String,
        options: List<PollOptionDto>
    )
    fun getSavedPollState(questionId: String): SavedPollState?
}