package demo.terrific.compose.compose.vertical

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.icons.outlined.ThumbUp
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import coil.compose.AsyncImage
import demo.terrific.R
import demo.terrific.compose.compose.common.DateTimeBadge
import demo.terrific.compose.compose.common.VideoProgressBar
import demo.terrific.compose.compose.common.toFormatted
import demo.terrific.compose.model.AssetDto
import demo.terrific.compose.style.VideoFeatureStyle
import demo.terrific.compose.style.withSdkFont
import kotlinx.coroutines.delay

@Composable
fun ImageAsset(
    asset: AssetDto,
    timestampFormat: String?,
    isLiked: Boolean,
    onLikeClick: (String) -> Unit,
    onBackClicked: () -> Unit,
    style: VideoFeatureStyle
) {
    val products = asset.products.orEmpty()
    val hasProducts = products.isNotEmpty()
    var progress by remember { mutableFloatStateOf(0f) }

    LaunchedEffect(asset.id) {
        val duration = 3000L
        val startTime = System.currentTimeMillis()

        while (true) {
            val elapsed = System.currentTimeMillis() - startTime
            progress = (elapsed.toFloat() / duration).coerceIn(0f, 1f)

            if (progress >= 1f) break

            delay(16)
        }
    }


    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {

                asset.background?.let {
                    AsyncImage(
                        model = it.imageUrl,
                        contentDescription = "background",
                        modifier = Modifier
                            .fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }
                AsyncImage(
                    model = asset.media?.mobileUrl,
                    contentDescription = "image",
                    contentScale = ContentScale.Crop,
                    modifier = if (asset.background != null) {
                        Modifier
                            .fillMaxSize()
                            .padding(16.dp)
                            .clip(RoundedCornerShape(16.dp))
                    } else {
                        Modifier
                            .fillMaxSize()
                    }
                )

                ImageOverlay(
                    asset = asset,
                    timestampFormat = timestampFormat,
                    isLiked = isLiked,
                    onLikeClick = onLikeClick,
                    onBackClicked = onBackClicked,
                    style = style
                )

                VideoProgressBar(
                    progress = progress,
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth()
                        .padding(start = 24.dp, top = 0.dp, end = 24.dp, bottom = 32.dp),
                    height = 8.dp,
                    trackColor = Color.White.copy(alpha = 0.28f),
                    progressColor = Color.White
                )
            }

            if (hasProducts) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.verticalGradient(
                                listOf(
                                    Color.Transparent,
                                    Color.Black.copy(alpha = 0.35f)
                                )
                            )
                        )
                        .padding(horizontal = 12.dp, vertical = 12.dp)
                ) {
                    TimelineProductsRow(
                        products = products,
                        style = style
                    )
                }
            }
        }
    }
}

@Composable
fun ImageOverlay(
    asset: AssetDto,
    timestampFormat: String?,
    isLiked: Boolean,
    onLikeClick: (String) -> Unit,
    onBackClicked: () -> Unit,
    style: VideoFeatureStyle
) {

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 32.dp, end = 32.dp, top = 32.dp, bottom = 48.dp)
            .zIndex(1f)
    ) {

        // CLOSE BUTTON
        IconButton(
            onClick = { onBackClicked() },
            modifier = Modifier.align(Alignment.TopEnd)
        ) {
            Icon(Icons.Default.Close, contentDescription = "Close", tint = Color.White)
        }

        val formatted = remember(asset.timestamp) {
            timestampFormat?.let { asset.timestamp?.toFormatted(it) }
        }

        if (formatted?.isNotEmpty() == true) {
            DateTimeBadge(formatted)
        }

        // RIGHT ACTIONS
        Column(
            modifier = Modifier
                .align(Alignment.BottomEnd),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            IconButton(onClick = { onLikeClick(asset.id) }) {
                Icon(
                    imageVector = if (isLiked) {
                        Icons.Filled.ThumbUp
                    } else {
                        Icons.Outlined.ThumbUp
                    },
                    contentDescription = "Like",
                    tint = Color.White
                )
            }

            Spacer(Modifier.height(12.dp))

            val context = LocalContext.current

            IconButton(
                onClick = {
                    val intent = Intent(Intent.ACTION_SEND).apply {
                        type = "text/plain"
                        putExtra(Intent.EXTRA_TEXT, asset.media?.mobileUrl)
                    }
                    context.startActivity(Intent.createChooser(intent, "Share"))
                }
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_share),
                    contentDescription = "Share",
                    tint = Color.White
                )
            }

        }

        // TITLE + DESCRIPTION
        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(end = 80.dp)
        ) {

            asset.title?.let {
                Text(
                    text = it,
                    color = Color.White,
                    fontWeight = FontWeight.SemiBold,
                    lineHeight = 24.sp,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis,
                    style = style.titleTextStyle.withSdkFont(style.fontFamily)
                )
            }

            Spacer(Modifier.height(6.dp))

            asset.description?.let {
                Text(
                    text = it,
                    color = Color.White,
                    fontWeight = FontWeight.Medium,
                    lineHeight = 24.sp,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis,
                    style = style.subtitleTextStyle.withSdkFont(style.fontFamily)
                )
            }
        }
    }
}