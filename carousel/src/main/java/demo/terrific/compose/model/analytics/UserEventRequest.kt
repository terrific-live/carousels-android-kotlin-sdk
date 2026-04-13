package demo.terrific.compose.model.analytics

import demo.terrific.compose.model.AssetDto
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserEventRequest(
    @SerialName("userId")
    val userId: String,
    @SerialName("sessionId")
    val sessionId: String,
    @SerialName("auxData")
    val auxData: AuxData,
    @SerialName("name")
    val name: String
)

@Serializable
data class AuxData(
    @SerialName("assetType")
    val assetType: String = "",
    @SerialName("customProducts")
    val customProducts: List<String> = emptyList(),
    @SerialName("parentUrl")
    val parentUrl: String = "",
    @SerialName("fixedPosition")
    val fixedPosition: Int = 0,
    @SerialName("products")
    val products: List<String> = emptyList(),
    @SerialName("position")
    val position: Int = 0,
    @SerialName("assets")
    val assets: List<AssetDto> = emptyList()
)