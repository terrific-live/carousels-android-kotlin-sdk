# Terrific Carousel SDK for Android

A lightweight and flexible carousel SDK built with **Jetpack Compose**, designed for smooth video browsing experiences and easy integration.

---

## 📦 Requirements

- Android API 17+
- Kotlin
- Jetpack Compose

---

## 🚀 Installation

### 1. Add JitPack repository

In your **`settings.gradle.kts`**:

```kotlin
dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://jitpack.io") }
    }
}
```

---

### 2. Add dependency

In your **app `build.gradle.kts`**:

```kotlin
dependencies {
    implementation("com.github.terrific-live:carousels-android-kotlin-sdk:v1.0.5")
}
```

---

## 🛠 Usage (Jetpack Compose)

SDK provides two main composables:

- `VideoCarousel` — horizontal preview carousel  
- `VerticalScreen` — full-screen video feed  

---

## 🎬 Video Carousel

Use this to display a horizontal list of videos:

```kotlin
VideoCarousel(
    assets = assets.value,
    onVideoClick = { index ->
        navController.navigate("feed/$index")
    }
)
```

### Parameters

| Parameter      | Type           | Description                      |
|----------------|----------------|----------------------------------|
| `assets`       | List           | List of video items              |
| `onVideoClick` | (Int) -> Unit  | Callback when video is clicked   |

---

## 📱 Vertical Video Screen

Full-screen immersive video feed:

```kotlin
VerticalScreen(
    assets = assets.value,
    videoId = videoId,
    likedVideos = likedVideos.value,
    onLikeClick = {
        viewModel.toggleLike(it)
    }
)
```

### Parameters

| Parameter       | Type             | Description                      |
|-----------------|------------------|----------------------------------|
| `assets`        | List             | Video items                      |
| `videoId`       | Int / String     | Initial video index or ID        |
| `likedVideos`   | List / Set       | Liked videos state               |
| `onLikeClick`   | (Video) -> Unit  | Like button callback             |

---

## 🔄 Navigation Example

```kotlin
NavHost(navController, startDestination = "carousel") {

    composable("carousel") {
        VideoCarousel(
            assets = assets,
            onVideoClick = { index ->
                navController.navigate("feed/$index")
            }
        )
    }

    composable("feed/{index}") { backStackEntry ->
        val index = backStackEntry.arguments?.getString("index")?.toInt() ?: 0

        VerticalScreen(
            assets = assets,
            videoId = index,
            likedVideos = likedVideos,
            onLikeClick = { viewModel.toggleLike(it) }
        )
    }
}
```

---

## 🧩 Data Model Example

```kotlin
@Serializable
data class AssetsResponse(
    val assets: List<AssetDto>
)

@Serializable
data class AssetDto(
    val id: String,
    val name: String?,
    val type: String,
    val title: String?,
    val description: String?,
    val media: MediaDto?
)

@Serializable
data class MediaDto(
    val coverUrl: String?,
    val desktopUrl: String?,
    val mobileUrl: String?,
    val srcUrl: String?,
    val videoPreviewUrl: String?
)
```

> Replace with your actual model if different.

