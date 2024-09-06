package ins.quivertech.app.data.data_source.remote.dto

import com.google.gson.annotations.SerializedName
import com.invictus.img.downloader.data.dto.IdCardUserDto

data class UserPageWrapperDto(
    @SerializedName("users")
    val data: UserPageResultDto?
)

data class UserPageResultDto(
    val totalDocs: Int,
    val limit: Int,
    val page: Int,
    val totalPages: Int,
    val nextPage: Int?,
    val prevPage: Int?,
    val hasPrevPage: Boolean,
    val hasNextPage: Boolean,
    val users: List<IdCardUserDto>
)
