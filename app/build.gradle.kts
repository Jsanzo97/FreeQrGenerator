import com.android.build.api.dsl.LibraryExtension
import io.gitlab.arturbosch.detekt.Detekt
import io.gitlab.arturbosch.detekt.extensions.DetektExtension
import org.gradle.kotlin.dsl.libs
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import com.diffplug.gradle.spotless.SpotlessExtension
import org.jetbrains.kotlin.gradle.internal.builtins.StandardNames.FqNames.target

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.jetbrains.compose)
    alias(libs.plugins.mokkery)
    alias(libs.plugins.roborazzi)
    alias(libs.plugins.detekt.gradle.plugin)
    alias(libs.plugins.spotless.gradle.plugin)
}

kotlin {
    androidTarget {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_17)
        }
    }

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
        }
    }

    sourceSets {
        commonMain.dependencies {
            implementation(libs.androidx.lifecycle.viewmodel)
            implementation(libs.compose.runtime)
            implementation(libs.compose.foundation)
            implementation(libs.compose.material3.multiplatform)
            implementation(libs.compose.material.icons.extended)
            implementation(libs.compose.ui)
            implementation(libs.compose.components.resources)
            implementation(libs.compose.ui.tooling.preview.multiplatform)
            implementation(libs.androidx.navigation.compose)
            implementation(libs.koin.core)
            implementation(libs.koin.compose)
            implementation(libs.koin.compose.viewmodel)
            implementation(libs.compottie)
            implementation(libs.compose.colorpicker)
            implementation(libs.custom.qr.generator)
            implementation(libs.kotlinx.collections.immutable)
        }

        androidMain.dependencies {
            implementation(libs.androidx.activity.compose)
            implementation(libs.androidx.core.ktx)
            implementation(libs.accompanist.drawablepainter)
            implementation(libs.androidx.ui.tooling.preview)
            implementation(libs.androidx.ui.tooling)
        }

        commonTest.dependencies {
            implementation(kotlin("test"))
            implementation(libs.kotlinx.coroutines.test)
            implementation(libs.turbine)
            implementation(libs.kotest.assertions)
        }

        androidUnitTest.dependencies {
            implementation(kotlin("test"))
            implementation(libs.kotlinx.coroutines.test)
            implementation(libs.turbine)
            implementation(libs.kotest.assertions)
            implementation(libs.robolectric)
            implementation(libs.androidx.junit)
            implementation(libs.mockk)
            implementation(libs.androidx.ui.test.junit4)
            implementation(libs.roborazzi.core)
            implementation(libs.roborazzi.compose)
            implementation(libs.roborazzi.junit)
        }

        androidInstrumentedTest.dependencies {
            implementation(libs.kotest.assertions)
            implementation(libs.kotlinx.coroutines.test)
            implementation(libs.androidx.junit)
            implementation(libs.androidx.ui.test.junit4)
        }

        iosTest.dependencies {
            implementation(kotlin("test"))
            implementation(libs.kotlinx.coroutines.test)
            implementation(libs.turbine)
            implementation(libs.kotest.assertions)
        }
    }
}

dependencies {
    implementation(libs.detekt.gradle.plugin)
    implementation(libs.detekt.rules.compose)
    implementation(libs.spotless.gradle.plugin)

    detektPlugins(libs.detekt.rules.compose)
}

extensions.configure<DetektExtension> {
    config.setFrom("$rootDir/config/detekt.yml")
    source.setFrom(fileTree("src") {
        include("**/*.kt")
    })
}

tasks.withType<Detekt>().configureEach {
    jvmTarget = "17"
    exclude { it.file.absolutePath.contains("/build/generated") }
}

tasks.register("detektAll") {
    group = "verification"
    dependsOn(subprojects.mapNotNull { it.tasks.findByName("detekt") })
}

extensions.configure<SpotlessExtension> {
    kotlin {
        target("src/*/kotlin/**/*.kt")
        ktlint(libs.versions.ktlint.get())
            .editorConfigOverride(
                mapOf(
                    "end_of_line" to "native",
                    "ktlint_standard_filename" to "disabled",
                    "ktlint_standard_class-naming" to "disabled",
                    "ktlint_standard_function-naming" to "disabled",
                    "ktlint_standard_property-naming" to "disabled",
                    "ktlint_standard_discouraged-comment-location" to "disabled",
                    "ktlint_standard_no-empty-file" to "disabled",
                    "ktlint_standard_backing-property-naming" to "disabled",
                    "ktlint_standard_binary-expression-wrapping" to "disabled",
                    "ktlint_standard_chain-method-continuation" to "disabled",
                    "ktlint_standard_class-signature" to "disabled",
                    "ktlint_standard_condition-wrapping" to "disabled",
                    "ktlint_standard_function-expression-body" to "disabled",
                    "ktlint_standard_function-literal" to "disabled",
                    "ktlint_standard_function-type-modifier-spacing" to "disabled",
                    "ktlint_standard_multiline-loop" to "disabled",
                    "ktlint_standard_no-unused-imports" to "enabled",
                )
            )
    }
}

extensions.configure<LibraryExtension>("android") {
    namespace = "com.example.freeqrgenerator.shared"
    compileSdk = 36

    defaultConfig {
        minSdk = 23
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    testOptions {
        unitTests {
            isIncludeAndroidResources = true
            all {
                it.systemProperty("roborazzi.test.record", "true")
            }
        }
    }
}

roborazzi {
    outputDir = file("src/androidUnitTest/snapshots/images/")
}

compose.resources {
    publicResClass = true
    packageOfResClass = "com.example.freeqrgenerator.resources"
}