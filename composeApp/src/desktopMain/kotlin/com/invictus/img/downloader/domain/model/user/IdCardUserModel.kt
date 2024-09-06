package com.invictus.img.downloader.domain.model.user

data class IdCardUserModel(
    val id: String,
    val prefix: String,
    val loginId: String,
    val applicationNo: String,
    val fullName: String,
    val displayId: String,
    val rollNo: String,
    val enrollmentNo: String,
    val classId: String,
    val section: String,
    val picture: String,
    val lastClickedDate: Long?
) {
    val isRemoteImage = picture.startsWith("https://") ||
            picture.startsWith("android.resource://")
}

val EMPTY_USER = IdCardUserModel(
    id = "",
    prefix = "",
    loginId = "",
    applicationNo = "",
    fullName = "",
    displayId = "",
    rollNo = "",
    enrollmentNo = "",
    classId = "",
    section = "",
    picture = "",
    lastClickedDate = null
)
