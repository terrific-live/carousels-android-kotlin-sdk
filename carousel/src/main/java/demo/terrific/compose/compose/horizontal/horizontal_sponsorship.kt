package demo.terrific.compose.compose.horizontal

import android.graphics.Color.parseColor
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import demo.terrific.compose.model.SponsorshipDto

@Composable
fun HorizontalSponsorshipBanner(
    sponsorship: SponsorshipDto?,
    onClick: (String) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
            .clip(RoundedCornerShape(bottomStart = 12.dp, bottomEnd = 12.dp))
            .background(Color(parseColor(sponsorship?.backgroundColor)))
            .clickable{
                sponsorship?.clickRedirect?.let(onClick)
            },
        contentAlignment = Alignment.Center
    ) {
        AsyncImage(
            model = sponsorship?.sideLogoUrl,
            contentDescription = null,
            modifier = Modifier.height(24.dp)
        )
    }
}

@Composable
fun SponsorshipHeader(
    sponsorship: SponsorshipDto?
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {

        Text(
            text = sponsorship?.sponsorLabel.orEmpty(),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        AsyncImage(
            model = sponsorship?.topLogoUrl,
            modifier = Modifier
                .size(28.dp)
                .clip(RoundedCornerShape(4.dp)),
            contentDescription = null
        )
    }
}