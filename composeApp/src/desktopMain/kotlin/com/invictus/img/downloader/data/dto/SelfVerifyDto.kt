package ins.quivertech.app.data.data_source.remote.dto

import com.invictus.img.downloader.data.dto.OrganisationDto

data class SelfVerifyDto(
    val user: Map<String, Any?>,
    val organisation: OrganisationDto
)
