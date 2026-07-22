import com.vanniktech.maven.publish.AndroidSingleVariantLibrary
import com.vanniktech.maven.publish.JavadocJar
import com.vanniktech.maven.publish.SourcesJar

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.maven.publish)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "demo.terrific"
    compileSdk {
        version = release(36)
    }

    defaultConfig {
        minSdk = 24

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }
    buildFeatures {
        compose = true
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    implementation("androidx.core:core-ktx:1.10.1")

    implementation(platform("androidx.compose:compose-bom:2026.03.01"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.material3:material3")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.10.2")
    implementation("com.squareup.retrofit2:retrofit:3.0.0")
    implementation("com.squareup.retrofit2:converter-gson:3.0.0")
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")

    implementation("androidx.compose.material:material-icons-extended:1.6.7")

    implementation("androidx.media3:media3-exoplayer:1.10.0")
    implementation("androidx.media3:media3-ui:1.10.0")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.7.0")
    implementation("androidx.compose.foundation:foundation-layout:1.10.6")
    implementation("io.coil-kt:coil-compose:2.5.0")
}

mavenPublishing {
    configure(
        AndroidSingleVariantLibrary(
            variant = "release",
            sourcesJar = SourcesJar.Sources(),
            javadocJar = JavadocJar.Empty()
        )
    )

    publishToMavenCentral()
    signAllPublications()

    coordinates(
        groupId = "io.github.terrific-live",
        artifactId = "carousels-android-kotlin-sdk",
        version = "1.0.8"
    )

    pom {
        name.set("Terrific Live Carousel SDK")

        description.set(
            "Android SDK for displaying interactive video, image and poll carousels."
        )

        inceptionYear.set("2026")

        url.set(
            "https://github.com/terrific-live/carousels-android-kotlin-sdk"
        )

        licenses {
            license {
                name.set("The Apache License, Version 2.0")
                url.set(
                    "https://www.apache.org/licenses/LICENSE-2.0.txt"
                )
                distribution.set("repo")
            }
        }

        developers {
            developer {
                id.set("terrific-live")
                name.set("Terrific Live")
                url.set("https://github.com/terrific-live")
            }
        }

        scm {
            url.set(
                "https://github.com/terrific-live/carousels-android-kotlin-sdk"
            )

            connection.set(
                "scm:git:https://github.com/terrific-live/carousels-android-kotlin-sdk.git"
            )

            developerConnection.set(
                "scm:git:ssh://git@github.com/terrific-live/carousels-android-kotlin-sdk.git"
            )
        }
    }
}