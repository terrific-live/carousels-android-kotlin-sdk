package demo.terrific

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import dagger.hilt.android.AndroidEntryPoint
import demo.terrific.compose.VerticalScreen
import demo.terrific.compose.VideoCarousel
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
            VideoCarousel(
                viewModel = viewModel,
                onVideoClick = { index ->
                    navController.navigate("feed/$index")
                }
            )
        }

        composable(
            route = "feed/{startIndex}",
            arguments = listOf(
                navArgument("startIndex") {
                    type = NavType.IntType
                }
            )
        ) {
            val viewModel: FeedViewModel = hiltViewModel()
            VerticalScreen(
                viewModel = viewModel
            )
        }
    }
}