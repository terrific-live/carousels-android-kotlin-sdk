package demo.terrific

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import demo.terrific.compose.compose.AssetCarousel
import demo.terrific.compose.style.VideoFeatureStyle
import demo.terrific.ui.theme.TerrificTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            TerrificTheme {
                AppRoot()
            }
        }
    }
}

@Composable
fun AppRoot() {

//    AssetCarousel(
//        storeId = "1FEyyLAlBJY8000v5nfL",
//        carouselId = "sQsA6UF3MwDfIz4TZXM7"
//    )

//    VideoSdk.setAnalyticsListener(
//        object : VideoSdkAnalyticsListener {
//            override fun onAnalyticsEventTracked(event: UserEventRequest) {
//                Log.d("SDK_ANALYTICS", "Tracked: ${event.name}")
//            }
//        }
//    )

    AssetCarousel(
        storeId = "0bor4CHMEbm3M4Dluput",
        carouselId = "HmUOF0rG4fO1v9U63t7Z",
        style = VideoFeatureStyle(
            carouselHeight = 15.dp,
            cornerRadius = 0.dp
        )

    )

}