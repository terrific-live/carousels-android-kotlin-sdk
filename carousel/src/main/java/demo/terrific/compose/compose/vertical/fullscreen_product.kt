package demo.terrific.compose.compose.vertical

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
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
import demo.terrific.compose.compose.toComposeColorOrNull
import demo.terrific.compose.model.ProductDto


@Composable
fun TimelineProductsRow(
    products: List<ProductDto>,
    modifier: Modifier = Modifier,
    onProductClick: (String) -> Unit
) {
    LazyRow(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(
            items = products,
            key = { it.id }
        ) { product ->
            TimelineProductCardFullscreen(
                product = product,
                modifier = Modifier.fillParentMaxWidth(1f),
                onClick = onProductClick
            )
        }
    }
}
@Composable
fun TimelineProductCardFullscreen(
    product: ProductDto,
    modifier: Modifier = Modifier,
    onClick: (String) -> Unit
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
                    .size(96.dp)
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
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                product.description?.takeIf { it.isNotBlank() }?.let {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = it,
                        color = textColor.copy(alpha = 0.9f),
                        fontSize = 16.sp,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                product.badge?.text?.takeIf { it.isNotBlank() }?.let {
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
                            fontSize = 12.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            product.ctaButton?.text?.takeIf { it.isNotBlank() }?.let { ctaText ->
                TextButton(
                    onClick = { onClick(product.externalUrl.orEmpty()) },
                    colors = ButtonDefaults.textButtonColors(
                        containerColor = ctaColor,
                        contentColor = ctaTextColor
                    ),
                    shape = RoundedCornerShape(20.dp),
                    contentPadding = PaddingValues(horizontal = 18.dp, vertical = 10.dp)
                ) {
                    Text(
                        text = ctaText,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}