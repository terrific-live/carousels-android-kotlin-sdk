package demo.terrific

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import dagger.hilt.android.AndroidEntryPoint
import demo.terrific.ui.theme.TerrificTheme
import demo.terrific.viewmodel.CarouselViewModel
import demo.terrific.viewmodel.FeedViewModel

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {
            TerrificTheme {
                AppRoot()
            }
        }
    }
}

@Composable
fun AppRoot() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "carousel"
    ) {
        composable("carousel") {
            val viewModel: CarouselViewModel = hiltViewModel()
            val assets = viewModel.assets.collectAsState()
//            VideoCarousel(
//                assets = assets.value,
//                onVideoClick = { index ->
//                    navController.navigate("feed/$index")
//                }
//            )
        }

        composable(
            route = "feed/{videoId}",
            arguments = listOf(
                navArgument("videoId") {
                    type = NavType.StringType
                }
            )
        ) {
                backStackEntry ->

            val videoId = backStackEntry.arguments?.getString("videoId")!!
            val viewModel: FeedViewModel = hiltViewModel()
            viewModel.loadFeed()
            val assets = viewModel.videos.collectAsState()
            val likedVideos = viewModel.likedVideos.collectAsState()
//            VerticalScreen(
//                assets = assets.value,
//                videoId = videoId,
//                likedVideos = likedVideos.value,
//                onLikeClick = {
//                    viewModel.toggleLike(it)
//                },
//
//            )

        }
    }
}