package com.invictus.img.downloader.data.dto

import com.google.gson.annotations.SerializedName

data class OrganisationDto(
    @SerializedName("_id")
    val id: String,
    val prefix: String,
    val name: String,
    val board: String?,
    @SerializedName("mobile_no")
    val mobileNo: String?,
    val website: String?,
    val address: String?,
    val email: String?,
    val logo: String?,
    @SerializedName("affiliation_no")
    val affiliationNo: String?,
    val signature: String?,
    val type: String?,
    val tagline: String?,
    @SerializedName("director_name")
    val directorName: String?,
    val active: Boolean?,
    val forms: List<FormDto>,
)


data class FormDto(
    val name: String,
    val active: Boolean,
    val fields: List<FieldDto>,
    @SerializedName("_id")
    val id: String,
    val updatedAt: String,
    val createdAt: String,
)

data class FieldDto(
    @SerializedName("field_name")
    val fieldName: String,
    @SerializedName("field_type")
    val fieldType: String,
    val required: Boolean,
    val options: List<String>,
    val minlength: Int?,
    val maxlength: Int?,
    @SerializedName("_id")
    val id: String,
    val regex: String?,
    @SerializedName("readonly")
    val readonly: Boolean?,
)
