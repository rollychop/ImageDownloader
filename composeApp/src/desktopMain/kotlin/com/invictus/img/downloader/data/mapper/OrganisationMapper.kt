package com.invictus.img.downloader.data.mapper

import com.invictus.img.downloader.domain.model.OrganisationModel
import com.invictus.img.downloader.data.dto.OrganisationDto


fun OrganisationDto.toModel() = OrganisationModel(
    id = id,
    prefix = prefix,
    name = name,
    board = board ?: "",
    mobileNo = mobileNo ?: "",
    website = website ?: "",
    address = address ?: "",
    email = email ?: "",
    logo = logo ?: "",
    affiliationNo = affiliationNo ?: "",
    signature = signature ?: "",
    type = type ?: "",
    tagline = tagline ?: "",
    directorName = directorName ?: "",
    active = active == true,
)
