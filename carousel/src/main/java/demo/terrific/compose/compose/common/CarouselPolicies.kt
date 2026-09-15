package demo.terrific.compose.compose.common

import demo.terrific.compose.model.AssetDto
import demo.terrific.compose.model.CarouselConfigDto

internal const val DEFAULT_AUTO_PLAY_INTERVAL_SECONDS = 2

internal fun CarouselConfigDto?.autoPlayDelayMillis(): Long? {
    if (this?.carouselAutoPlay != true) return null

    val intervalSeconds = carouselAutoPlayInterval
        ?.takeIf { it > 0 }
        ?: DEFAULT_AUTO_PLAY_INTERVAL_SECONDS

    return intervalSeconds * 1_000L
}

internal data class SharePayload(
    val text: String,
    val url: String?
) {
    val intentText: String
        get() = listOfNotNull(text, url).joinToString("\n\n")
}

internal fun AssetDto.sharePayload(): SharePayload? {
    val text = listOfNotNull(
        title?.trim()?.takeIf { it.isNotEmpty() },
        description?.trim()?.takeIf { it.isNotEmpty() }
    ).joinToString("\n\n")

    if (text.isBlank()) return null

    val url = ctaButton?.url
        ?.trim()
        ?.takeIf { it.isNotEmpty() }
        ?: products.orEmpty().firstNotNullOfOrNull { product ->
            product.externalUrl?.trim()?.takeIf { it.isNotEmpty() }
        }

    return SharePayload(text = text, url = url)
}
