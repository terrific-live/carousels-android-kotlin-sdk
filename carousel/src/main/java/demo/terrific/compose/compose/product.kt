package demo.terrific.compose.compose

import android.annotation.SuppressLint
import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import demo.terrific.compose.model.ProductDto

@Composable
fun TimelineProductCard(
    product: ProductDto,
    modifier: Modifier = Modifier,
    onClick: (String) -> Unit
) {
    val context = LocalContext.current

    val backgroundColor = product.background?.color?.toComposeColorOrNull() ?: Color(0xFF4A4A4A)
    val textColor = product.background?.textColor?.toComposeColorOrNull() ?: Color.White
    val badgeColor = product.badge?.color?.toComposeColorOrNull() ?: Color(0xFF2C2C2C)
    val badgeTextColor = product.badge?.textColor?.toComposeColorOrNull() ?: Color.White

    Surface(
        modifier = modifier,
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
            verticalAlignment = Alignment.CenterVertically
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
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                product.description
                    ?.takeIf { it.isNotBlank() }
                    ?.let {
                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = it,
                            color = textColor.copy(alpha = 0.9f),
                            fontSize = 13.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
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
                                fontSize = 12.sp
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