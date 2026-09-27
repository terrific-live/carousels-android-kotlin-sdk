package demo.terrific.compose.analytics.utils

class ActiveViewTimer(
    private val now: () -> Long = System::currentTimeMillis
) {

    private var startedAt: Long? = null
    private var accumulatedMs: Long = 0L

    fun start() {
        if (startedAt == null) {
            startedAt = now()
        }
    }

    fun pause() {
        val start = startedAt ?: return

        accumulatedMs += now() - start
        startedAt = null
    }

    fun getDurationMs(): Long {
        val currentSession = startedAt?.let {
            now() - it
        } ?: 0L

        return accumulatedMs + currentSession
    }

    fun reset() {
        startedAt = null
        accumulatedMs = 0L
    }
}