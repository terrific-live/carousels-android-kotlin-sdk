package demo.terrific.compose.storage.storage

interface PollStorage {
    fun saveVote(questionId: String, optionText: String)
    fun getVote(questionId: String): String?
    fun hasVoted(questionId: String): Boolean
}