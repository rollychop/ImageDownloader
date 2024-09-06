package com.invictus.img.downloader.util

import java.io.File

object AppFileHelper {

    // Function to get the cache directory based on the OS
    fun getCacheDirectory(appName: String = "QTImage"): File {
        return when (getOperatingSystem()) {
            OS.WINDOWS -> File(System.getenv("LOCALAPPDATA"), "$appName/cache")
            OS.MACOS -> File(System.getProperty("user.home"), "Library/Caches/$appName")
            OS.LINUX -> File(System.getProperty("user.home"), ".cache/$appName")
            OS.UNKNOWN -> throw IllegalStateException("Unsupported Operating System")
        }.apply {
            if (!exists()) mkdirs()
        }
    }

    // Function to get the data directory based on the OS
    fun getDataDirectory(appName: String = "QTImage"): File {
        return when (getOperatingSystem()) {
            OS.WINDOWS -> File(System.getenv("APPDATA"), appName)
            OS.MACOS -> File(System.getProperty("user.home"), "Library/Application Support/$appName")
            OS.LINUX -> File(System.getProperty("user.home"), ".local/share/$appName")
            OS.UNKNOWN -> throw IllegalStateException("Unsupported Operating System")
        }.apply {
            if (!exists()) mkdirs()
        }
    }

    // Function to get the files directory (for user-specific files)
    fun getFilesDirectory(appName: String = "QTImage"): File {
        return File(getDataDirectory(appName), "files").apply {
            if (!exists()) mkdirs()
        }
    }


    // New function to get a folder in the user's home directory (for user-visible files)
    fun getUserHomeDirectory(appName: String = "QTImage", folderName: String = "Downloads"): File {
        val homeDir = File(System.getProperty("user.home"), folderName)
        return File(homeDir, appName).apply {
            if (!exists()) mkdirs()
        }
    }

    // Utility function to detect the operating system
    private fun getOperatingSystem(): OS {
        val osName = System.getProperty("os.name").lowercase()
        return when {
            osName.contains("win") -> OS.WINDOWS
            osName.contains("mac") -> OS.MACOS
            osName.contains("nix") || osName.contains("nux") || osName.contains("aix") -> OS.LINUX
            else -> OS.UNKNOWN
        }
    }

    enum class OS {
        WINDOWS, MACOS, LINUX, UNKNOWN
    }
}
