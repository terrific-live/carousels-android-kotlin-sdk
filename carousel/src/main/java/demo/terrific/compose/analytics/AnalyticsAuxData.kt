package demo.terrific.compose.analytics

import demo.terrific.compose.model.AssetDto
import demo.terrific.compose.model.ProductDto

data class AnalyticsAuxData(
    val assetType: String? = null,
    val assets: List<AssetDto>? = null,
    val customProducts: List<ProductDto>? = null,
    val fixedPosition: Int? = null,
    val parentUrl: String? = null,
    val position: Int? = null,
    val products: List<ProductDto>? = null
)