package demo.terrific.compose.compose.vertical

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import coil.compose.AsyncImage

@Composable
fun ImageAsset(url: String) {
    AsyncImage(
        model = url,
        contentDescription = "image",
        contentScale = ContentScale.Crop,
        modifier = Modifier.fillMaxSize()
    )
}