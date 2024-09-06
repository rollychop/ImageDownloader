package ins.quivertech.app.data.data_source.remote.dto

import com.google.gson.annotations.SerializedName

data class AuthResponseDto(
    @SerializedName("_id")
    val id: String,
    val username: String,
    @SerializedName("login_id")
    val loginId: String,
    val name: String,
    val email: String?,
    @SerializedName("mobile_no")
    val mobileNo: String?,
    val verified: Boolean?,
    val prefix: String,
    val active: Boolean?,
    val role: String?
)
