package com.invictus.img.downloader.domain.model.user


data class UserModel(
    val name: String,
    val email: String,
    val mobileNumber: String,
    val username: String,
    val id: String,
    val loginId: String,
    val role: String,
) {
    private constructor() : this(
        name = "",
        email = "",
        mobileNumber = "",
        username = "",
        id = "",
        loginId = "",
        role = "user"
    )


    val hasProofReadingPermission = role.equals("admin", true).or(
        role.equals("superadmin", true)
    )
    val hasPhotoCapPermission =
        hasProofReadingPermission || role.equals("staff", true)


    companion object {
        private val INSTANCE by lazy { UserModel() }
        fun empty(): UserModel = INSTANCE
    }

    val alias: String = name.take(2).uppercase()


}
