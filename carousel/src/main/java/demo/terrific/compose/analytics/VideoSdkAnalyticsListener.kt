package demo.terrific.compose.analytics

import demo.terrific.compose.model.analytics.UserEventRequest

interface VideoSdkAnalyticsListener {
    fun onAnalyticsEventTracked(event: UserEventRequest)
//    fun onAnalyticsEventSent(event: UserEventRequest, success: Boolean, code: Int?)
}