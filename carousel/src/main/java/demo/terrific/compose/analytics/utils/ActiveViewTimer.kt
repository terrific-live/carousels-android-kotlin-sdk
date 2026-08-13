package demo.terrific.compose.analytics.utils

class ActiveViewTimer {

    private var startedAt: Long? = null
    private var accumulatedMs: Long = 0L

    fun start() {
        if (startedAt == null) {
            startedAt = System.currentTimeMillis()
        }
    }

    fun pause() {
        val start = startedAt ?: return

        accumulatedMs += System.currentTimeMillis() - start
        startedAt = null
    }

    fun getDurationMs(): Long {
        val currentSession = startedAt?.let {
            System.currentTimeMillis() - it
        } ?: 0L

        return accumulatedMs + currentSession
    }

    fun reset() {
        startedAt = null
        accumulatedMs = 0L
    }
}