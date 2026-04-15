package demo.terrific.compose.storage.storage

data class SavedPollOption(
    val text: String,
    val numberOfVotes: Int
)

data class SavedPollState(
    val selectedOptionText: String,
    val options: List<SavedPollOption>
)


