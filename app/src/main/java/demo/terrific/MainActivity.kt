package demo.terrific

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import demo.terrific.compose.compose.AssetCarousel
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
    AssetCarousel(
        storeId = "0bor4CHMEbm3M4Dluput",
        carouselId = "HmUOF0rG4fO1v9U63t7Z"
    )

}