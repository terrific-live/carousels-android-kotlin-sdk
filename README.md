# 🎬 Video Feature SDK for Android (Jetpack Compose)

Reusable video carousel + vertical feed SDK for Android.
Designed as a **self-contained library** with minimal integration effort.

---

## ✨ Features

* 🎞 Video Carousel (horizontal)
* 📱 Vertical video feed (TikTok-style)
* ❤️ Like support
* 🎨 Fully customizable UI (radius, height, fonts)
* 🚀 No ViewModel / No Hilt / No Navigation required
* 🔌 Works in both **Compose** and **XML projects**

---

## 📦 Installation

### 1. Add repositories

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

```kotlin
implementation("com.github.terrific-live:carousels-android-kotlin-sdk:v1.0.9")
```
---

## 🚀 Usage (Jetpack Compose)

### Example
```kotlin
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            TerrificTheme {
                AppRoot()
            }
        }
    }
}

@Composable
fun AppRoot() {

    AssetCarousel(
        storeId = "your_store_id",
        carouselId = "your_carousel_id"
    )

}
```

---

## 🧱 Usage (XML)

### XML

```xml
<androidx.compose.ui.platform.ComposeView
    android:id="@+id/videoFeature"
    android:layout_width="match_parent"
    android:layout_height="match_parent"/>
```

### Fragment / Activity

```kotlin
composeView.setViewCompositionStrategy(
    ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed
)

composeView.setContent {
    AssetCarousel(
        storeId = "your_store_id",
        carouselId = "your_carousel_id"
    )
}
```

---

## 🎨 UI Customization

You can fully customize UI via `VideoFeatureStyle`.

### Example

```kotlin
AssetCarousel(
    storeId = "your_store_id",
    carouselId = "your_carousel_id"
    style = VideoFeatureStyle(
        carouselHeight: Dp = 220.dp,
        cornerRadius: Dp = 16.dp
    )
)
```

---

## Analytics callback
```
    VideoSdk.setAnalyticsListener(
        object : VideoSdkAnalyticsListener {
            override fun onAnalyticsEventTracked(event: UserEventRequest) {
                Log.d("SDK_ANALYTICS", "Tracked: ${event.name}")
            }
        }
    )
```


## 🧠 Architecture

The SDK is built with:

* **State-driven UI (no Navigation)**
* **Controller instead of ViewModel**
* **Internal Retrofit data layer**
* **Public simple API**

```
VideoFeature(feedId)
    ↓
Controller (StateFlow)
    ↓
Repository
    ↓
Retrofit API
```

---

## 🔌 Configuration
By default base url to fetch a data is 

```
"https://terrific-live-polls.web.app/"
```

---

## ⚠️ Requirements

* minSdk: 24+
* Kotlin 2.x
* Jetpack Compose enabled

---

## 💡 Notes

* SDK manages its own state internally
* No dependency on host app architecture
* Can be embedded into any screen

---

## 🧑‍💻 Author

Built for scalable Android video experiences.

---
