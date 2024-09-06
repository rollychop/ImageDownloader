package com.invictus.img.downloader.data.dto

import com.google.gson.annotations.SerializedName

data class IdCardUserDto(
    @SerializedName("_id")
    val id: String,
    val username: String?,
    @SerializedName("login_id")
    val loginId: String?,
    @SerializedName("application_no")
    val applicationNo: String?,
    val verified: Boolean?,
    val prefix: String?,
    val active: Boolean?,
    val printed: Boolean?,
    @SerializedName("org_code")
    val orgCode: String?,
    val name: String?,
    @SerializedName("enrollment_no")
    val enrollmentNo: String?,
    @SerializedName("class")
    val className: String?,
    val section: String?,

    val session: String?,
    @SerializedName("roll_no")
    val rollNo: String?,
    @SerializedName("enrollment_date")
    val enrollmentDate: String?,
    val dob: String?,
    val gender: String?,
    val category: String?,
    val picture: String?,
    @SerializedName("father_name")
    val fatherName: String?,
    @SerializedName("mobile_no")
    val mobileNo: String?,
    @SerializedName("whatsapp_no")
    val whatsappNo: String?,
    val email: String?,
    @SerializedName("emergency_mobile_no")
    val emergencyMobileNo: String?,
    val address: String?,
    @SerializedName("aadhar_no")
    val aadharNo: String?,
    @SerializedName("blood_group")
    val bloodGroup: String?,
    @SerializedName("house_name")
    val houseName: String?,
    val createdAt: String,
    val updatedAt: String,
    @SerializedName("last_clicked_date")
    val lastClickedDate: String?,
)
