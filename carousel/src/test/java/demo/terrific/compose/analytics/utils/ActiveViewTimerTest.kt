package demo.terrific.compose.analytics.utils

import org.junit.Assert.assertEquals
import org.junit.Test

class ActiveViewTimerTest {

    @Test
    fun `counts only active periods`() {
        var time = 1_000L
        val timer = ActiveViewTimer { time }

        timer.start()

        time = 1_400L
        timer.pause() // +400

        time = 3_000L
        timer.start()

        time = 3_250L
        timer.pause() // +250

        assertEquals(650L, timer.getDurationMs())
    }

    @Test
    fun `start called twice does not restart timer`() {
        var time = 100L
        val timer = ActiveViewTimer { time }

        timer.start()
        time = 200L
        timer.start()
        time = 350L

        assertEquals(250L, timer.getDurationMs())
    }

    @Test
    fun `reset clears accumulated duration`() {
        var time = 100L
        val timer = ActiveViewTimer { time }

        timer.start()
        time = 500L
        timer.pause()
        timer.reset()

        assertEquals(0L, timer.getDurationMs())
    }
}
