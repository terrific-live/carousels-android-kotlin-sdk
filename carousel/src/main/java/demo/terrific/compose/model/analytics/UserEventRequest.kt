package demo.terrific.compose.model.analytics

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
//    val assetId: String? = null,
//    val assetIds: List<String>? = null,
//    val assetTimestamps: List<Long>? = null,
    val parentUrl: String? = null,
//    val totalAssets: Int? = null,
//    val assetType: String? = null,
)