import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.compose.compiler)
}

kotlin {
    jvm("desktop")

    sourceSets {
        val desktopMain by getting

        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.materialIconsExtended)
            implementation(compose.animation)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(libs.androidx.lifecycle.viewmodel)
            implementation(libs.androidx.lifecycle.runtime.compose)
            implementation(libs.precompose.navigatation)
            implementation(libs.precompose.viewmodel)
            implementation(libs.androidx.data.store.core)
            implementation(libs.kamel.image)
        }
        desktopMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(libs.kotlinx.coroutines.swing)
            // Retrofit
            implementation(libs.retrofit)
            implementation(libs.retrofitConverterGson)


            //Ok-http
            implementation(libs.okhttp)
            implementation(libs.okhttpLogging)
            implementation(libs.logback)
            implementation(libs.okhttp.ktor.engine)

        }
    }
}


compose.desktop {
    application {
        mainClass = "com.invictus.img.downloader.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageVersion = "1.0.1"
            packageName = "Qt-Downloder"
            includeAllModules = true
        }
        buildTypes.release.proguard {
            isEnabled = false
        }
    }

}
