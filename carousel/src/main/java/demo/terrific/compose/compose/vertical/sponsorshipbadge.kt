package demo.terrific.compose.compose.vertical

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import demo.terrific.compose.model.SponsorshipBannerDto
import demo.terrific.compose.model.SponsorshipDto

@Composable
fun SponsorshipBadge(
    title: String?,
    logoUrl: String?,
    backgroundColor: Color,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
//            .padding(horizontal = 12.dp, vertical = 8.dp)
            .background(backgroundColor),
        verticalAlignment = Alignment.CenterVertically
    ) {
        title?.let {
            Text(
                text = it,
                color = Color.Black,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(12.dp)
            )
        }

        Spacer(modifier = Modifier.width(8.dp))

        AsyncImage(
            model = logoUrl,
            contentDescription = "Sponsor logo",
            modifier = Modifier
                .height(28.dp)
                .widthIn(max = 100.dp),
            contentScale = ContentScale.Fit
        )
    }
}

@Composable
fun SponsorshipBanner(
    banner: SponsorshipBannerDto,
    modifier: Modifier = Modifier,
    onClick: (String) -> Unit
) {
    val backgroundColor = remember(banner.backgroundColor) {
        runCatching {
            Color(android.graphics.Color.parseColor(banner.backgroundColor))
        }.getOrDefault(Color.Transparent)
    }

    val redirectUrl = banner.clickRedirect

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(32.dp)
            .background(backgroundColor)
            .clickable(
                enabled = !redirectUrl.isNullOrBlank()
            ) {
                redirectUrl?.let(onClick)
            },
        contentAlignment = Alignment.Center
    ) {
        AsyncImage(
            model = banner.logoUrl,
            contentDescription = "Sponsor logo",
            modifier = Modifier
                .fillMaxHeight()
                .padding(vertical = 10.dp)
                .fillMaxWidth(0.3f),
            contentScale = ContentScale.Fit
        )
    }
}

@Composable
fun PollSponsorLogo(
    sponsorship: SponsorshipDto,
    modifier: Modifier,
    onClick: (() -> Unit)? = null
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        AsyncImage(
            model = sponsorship.poll?.logoUrl,
            contentDescription = null,
            modifier = Modifier
                .size(48.dp)
                .clip(RoundedCornerShape(8.dp))
                .clickable(enabled = onClick != null) {
                    onClick?.invoke()
                }
        )
    }
}