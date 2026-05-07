package demo.terrific.compose.compose.horizontal

import android.annotation.SuppressLint
import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.snapping.SnapPosition
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import coil.compose.AsyncImage
import demo.terrific.compose.model.ProductDto
import demo.terrific.compose.style.VideoFeatureStyle
import demo.terrific.compose.style.withSdkFont
import kotlinx.coroutines.delay


@Composable
fun TimelineProductsRowCarousel(
    products: List<ProductDto>,
    modifier: Modifier = Modifier,
    style: VideoFeatureStyle
) {

    val listState = rememberLazyListState()
    val flingBehavior = rememberSnapFlingBehavior(
        lazyListState = listState,
        snapPosition = SnapPosition.Center
    )

    val loopedProducts = remember(products) {
        if (products.size == 1) products else products + products.first()
    }

    LaunchedEffect(products) {
        if (products.size <= 1) return@LaunchedEffect

        var currentIndex = 0

        while (true) {
            delay(1000)

            if (!listState.isScrollInProgress) {
                val nextIndex = currentIndex + 1

                if (nextIndex < loopedProducts.lastIndex) {
                    currentIndex = nextIndex
                    listState.animateScrollToItem(currentIndex)
                } else {
                    currentIndex = nextIndex
                    listState.animateScrollToItem(currentIndex)

                    listState.scrollToItem(0)
                    currentIndex = 0
                }
            }
        }
    }

    LazyRow(
        state = listState,
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        flingBehavior = flingBehavior
    ) {
        itemsIndexed(
            items = loopedProducts,
            key = { index, product -> "${product.id}_$index" }
        ) { _, product ->
            TimelineProductCard(
                product = product,
                modifier = Modifier.fillParentMaxWidth(1f),
                style = style
            )
        }
    }
}

@Composable
fun TimelineProductCard(
    product: ProductDto,
    modifier: Modifier = Modifier,
    style: VideoFeatureStyle
) {
    val context = LocalContext.current

    val backgroundColor = product.background?.color?.toComposeColorOrNull() ?: Color(0xFF4A4A4A)
    val textColor = product.background?.textColor?.toComposeColorOrNull() ?: Color.White
    val badgeColor = product.badge?.color?.toComposeColorOrNull() ?: Color(0xFF2C2C2C)
    val badgeTextColor = product.badge?.textColor?.toComposeColorOrNull() ?: Color.White

    Surface(
        modifier = modifier.wrapContentSize(),
        shape = RoundedCornerShape(22.dp),
        color = backgroundColor,
        onClick = {
            val intent = Intent(Intent.ACTION_VIEW, product.externalUrl?.toUri())
            context.startActivity(intent)
        }
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 12.dp, vertical = 10.dp),
//            verticalAlignment = Alignment.CenterVertically
        ) {

            AsyncImage(
                model = product.imageUrl,
                contentDescription = product.name,
                modifier = Modifier
                    .height(72.dp)
                    .aspectRatio(1f)
                    .clip(RoundedCornerShape(14.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = product.name,
                    color = textColor,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = style.subtitleTextStyle.withSdkFont(style.fontFamily)

                )

                product.description
                    ?.takeIf { it.isNotBlank() }
                    ?.let { desc ->
                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = desc.trimStart { it == '\n' },
                            color = textColor.copy(alpha = 0.9f),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            style = style.bodyTextStyle.withSdkFont(style.fontFamily)
                        )
                    }

                product.badge?.text
                    ?.takeIf { it.isNotBlank() }
                    ?.let {
                        Spacer(modifier = Modifier.height(8.dp))

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(badgeColor)
                                .padding(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = it,
                                color = badgeTextColor,
                                style = style.badgeTextStyle.withSdkFont(style.fontFamily)
                            )
                        }
                    }
            }
        }
    }
}

@SuppressLint("UseKtx")
fun String.toComposeColorOrNull(): Color {
    return Color(android.graphics.Color.parseColor(this))
}