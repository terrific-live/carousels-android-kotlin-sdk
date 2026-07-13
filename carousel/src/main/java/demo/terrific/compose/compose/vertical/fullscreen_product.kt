package demo.terrific.compose.compose.vertical

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.snapping.SnapPosition
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import coil.compose.AsyncImage
import demo.terrific.compose.compose.horizontal.toComposeColorOrNull
import demo.terrific.compose.model.ProductDto
import demo.terrific.compose.style.VideoFeatureStyle
import demo.terrific.compose.style.withSdkFont
import kotlinx.coroutines.delay


@Composable
fun TimelineProductsRow(
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
            delay(2500)

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
            TimelineProductCardFullscreen(
                product = product,
                modifier = Modifier.fillParentMaxWidth(1f),
                style = style
            )
        }
    }
}

@Composable
fun TimelineProductCardFullscreen(
    product: ProductDto,
    modifier: Modifier = Modifier,
    style: VideoFeatureStyle
) {
    val context = LocalContext.current
    val backgroundColor = product.background?.color?.toComposeColorOrNull() ?: Color(0xFF4A4A4A)
    val textColor = product.background?.textColor?.toComposeColorOrNull() ?: Color.White
    val badgeColor = product.badge?.color?.toComposeColorOrNull() ?: Color(0xFF2C2C2C)
    val badgeTextColor = product.badge?.textColor?.toComposeColorOrNull() ?: Color.White
    val ctaColor = product.ctaButton?.color?.toComposeColorOrNull() ?: Color.White
    val ctaTextColor = product.ctaButton?.textColor?.toComposeColorOrNull() ?: Color.Black

    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(22.dp),
        color = backgroundColor,
        onClick = {
            val intent = Intent(Intent.ACTION_VIEW, product.externalUrl?.toUri())
            context.startActivity(intent)
        }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = product.imageUrl,
                contentDescription = product.name,
                modifier = Modifier
                    .size(84.dp)
                    .clip(RoundedCornerShape(16.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(14.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = product.name,
                    color = textColor,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = style.subtitleTextStyle.withSdkFont(style.fontFamily)
                )

                product.description?.takeIf { it.isNotBlank() }?.let {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = it,
                        color = textColor.copy(alpha = 0.9f),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        style = style.bodyTextStyle.withSdkFont(style.fontFamily)
                    )
                }

                Text(
                    text = "$${product.price}",
                    color = textColor,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = style.subtitleTextStyle.withSdkFont(style.fontFamily)
                )



                Spacer(modifier = Modifier.width(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    product.badge?.text?.takeIf { it.isNotBlank() }?.let {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(badgeColor)
                                .padding(horizontal = 4.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = it,
                                color = badgeTextColor,
                                style = style.smallTextStyle.withSdkFont(style.fontFamily)
                            )
                        }
                    }

                    product.ctaButton?.text?.takeIf { it.isNotBlank() }?.let { ctaText ->
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(ctaColor)
                                .padding(horizontal = 6.dp, vertical = 4.dp)
                                .clickable(
                                    onClick = {
                                        val intent =
                                            Intent(Intent.ACTION_VIEW, product.externalUrl?.toUri())
                                        context.startActivity(intent)
                                    }
                                )
                        ) {
                            Text(
                                text = ctaText,
                                fontWeight = FontWeight.SemiBold,
                                color = ctaTextColor,
                                fontSize = 10.sp,
                                style = style.smallTextStyle.withSdkFont(style.fontFamily)
                            )
                        }
                    }

                }

            }

        }
    }
}