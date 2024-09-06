package com.invictus.img.downloader.data.mapper.user

import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.intl.Locale
import com.invictus.img.downloader.data.dto.IdCardUserDto
import com.invictus.img.downloader.domain.model.user.IdCardUserModel
import com.invictus.img.downloader.domain.model.user.UserModel
import ins.quivertech.app.data.data_source.remote.dto.AuthResponseDto
import java.time.Instant

fun AuthResponseDto.toUserModel(): UserModel = UserModel(
    name = name.capitalize(Locale.current),
    email = email ?: "",
    mobileNumber = mobileNo ?: "",
    username = username,
    id = id,
    loginId = loginId,
    role = role ?: "user"
)

fun IdCardUserDto.toModel() = IdCardUserModel(
    id = id,
    prefix = prefix ?: "",
    loginId = loginId ?: "",
    applicationNo = applicationNo ?: "",
    displayId = enrollmentNo?.takeIf { it.isNotBlank() }
        ?: rollNo?.takeIf { it.isNotBlank() }
        ?: username
        ?: loginId ?: "Unknown id",
    classId = className ?: "",
    section = section ?: "",
    rollNo = rollNo ?: "",
    enrollmentNo = enrollmentNo ?: "",
    fullName = name ?: "",
    picture = picture ?: "",
    lastClickedDate = try {
        if (lastClickedDate != null) {
            Instant.parse(lastClickedDate).toEpochMilli()
        } else null
    } catch (_: Exception) {
        null
    }
)

