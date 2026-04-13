package demo.terrific.compose.analytics

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

internal object AnalyticsDebugStore {

    private const val MAX_EVENTS = 50

    private val _events = MutableStateFlow<List<AnalyticsDebugEvent>>(emptyList())
    val events: StateFlow<List<AnalyticsDebugEvent>> = _events.asStateFlow()

    fun add(
        name: String,
        details: String
    ) {
        val formatter = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
        val event = AnalyticsDebugEvent(
            timestamp = formatter.format(Date()),
            name = name,
            details = details
        )

        val updated = (_events.value + event).takeLast(MAX_EVENTS)
        _events.value = updated
    }

    fun clear() {
        _events.value = emptyList()
    }
}