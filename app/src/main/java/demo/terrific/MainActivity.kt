package demo.terrific

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import dagger.hilt.android.AndroidEntryPoint
import demo.terrific.ui.theme.TerrificTheme

@AndroidEntryPoint
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

}