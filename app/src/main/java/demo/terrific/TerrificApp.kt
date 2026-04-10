package demo.terrific

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class TerrificApp : Application() {

    override fun onCreate() {
        super.onCreate()
//        VideoSdk.initialize(
//            VideoSdkConfig(
//                baseUrl = "https://terrific-live-polls.web.app/"
//            )
//        )
    }
}