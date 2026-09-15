package demo.terrific.compose.compose.common

import com.google.gson.Gson
import demo.terrific.compose.model.AssetDto
import demo.terrific.compose.model.AssetButtonDto
import demo.terrific.compose.model.CarouselConfigDto
import demo.terrific.compose.model.MediaDto
import demo.terrific.compose.model.ProductDto
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class CarouselPoliciesTest {

    @Test
    fun `autoplay is disabled unless explicitly enabled`() {
        assertNull(null.autoPlayDelayMillis())
        assertNull(CarouselConfigDto(carouselAutoPlay = false).autoPlayDelayMillis())
    }

    @Test
    fun `autoplay uses configured interval in seconds`() {
        val config = CarouselConfigDto(
            carouselAutoPlay = true,
            carouselAutoPlayInterval = 7
        )

        assertEquals(7_000L, config.autoPlayDelayMillis())
    }

    @Test
    fun `autoplay uses safe default for missing or invalid interval`() {
        val config = CarouselConfigDto(
            carouselAutoPlay = true,
            carouselAutoPlayInterval = 0
        )

        assertEquals(2_000L, config.autoPlayDelayMillis())
    }

    @Test
    fun `share is hidden when prefilled text is unavailable`() {
        assertNull(asset(title = " ", description = null).sharePayload())
    }

    @Test
    fun `share includes prefilled text and canonical product URL`() {
        val payload = asset(
            title = "Headline",
            description = "Summary",
            productUrl = "https://example.com/story",
            mediaUrl = "https://cdn.example.com/video.mp4"
        ).sharePayload()

        assertEquals("Headline\n\nSummary", payload?.text)
        assertEquals("https://example.com/story", payload?.url)
        assertEquals(
            "Headline\n\nSummary\n\nhttps://example.com/story",
            payload?.intentText
        )
        assertFalse(payload?.intentText.orEmpty().contains("video.mp4"))
    }

    @Test
    fun `share prefers the canonical asset CTA URL`() {
        val asset = asset(
            title = "Headline",
            productUrl = "https://example.com/product",
            mediaUrl = "https://cdn.example.com/video.mp4"
        ).apply {
            ctaButton = AssetButtonDto(url = "https://example.com/story")
        }

        val payload = asset.sharePayload()

        assertEquals("https://example.com/story", payload?.url)
        assertFalse(payload?.intentText.orEmpty().contains("video.mp4"))
    }

    @Test
    fun `asset CTA is decoded without changing the primary constructor`() {
        val decoded = Gson().fromJson(
            """{"id":"asset-id","position":1,"ctaButton":{"url":"https://example.com/story"}}""",
            AssetDto::class.java
        )

        assertEquals("https://example.com/story", decoded.ctaButton?.url)
    }

    @Test
    fun `share falls back to a product URL without using media URL`() {
        val payload = asset(
            title = "Headline",
            productUrl = "https://example.com/product",
            mediaUrl = "https://cdn.example.com/video.mp4"
        ).sharePayload()

        assertEquals("https://example.com/product", payload?.url)
        assertTrue(payload?.intentText.orEmpty().startsWith("Headline"))
        assertFalse(payload?.intentText.orEmpty().contains("video.mp4"))
    }

    @Test
    fun `share uses text only when no canonical URL is available`() {
        val payload = asset(
            description = "Summary",
            mediaUrl = "https://cdn.example.com/video.mp4"
        ).sharePayload()

        assertEquals("Summary", payload?.intentText)
        assertNull(payload?.url)
    }

    private fun asset(
        title: String? = null,
        description: String? = null,
        productUrl: String? = null,
        mediaUrl: String? = null
    ) = AssetDto(
        id = "asset-id",
        title = title,
        description = description,
        media = MediaDto(
            coverUrl = null,
            desktopUrl = null,
            mobileUrl = mediaUrl,
            srcUrl = null,
            videoPreviewUrl = null
        ),
        products = productUrl?.let {
            listOf(
                ProductDto(
                    id = "product-id",
                    name = "Product",
                    description = null,
                    imageUrl = null,
                    externalUrl = it,
                    type = null,
                    price = null,
                    formattedPrice = null,
                    currency = null,
                    badge = null,
                    ctaButton = null,
                    background = null
                )
            )
        },
        position = 1
    )
}
