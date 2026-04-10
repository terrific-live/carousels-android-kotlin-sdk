# 🎬 Video Feature SDK for Android (Jetpack Compose)

Reusable video carousel + vertical feed SDK for Android.
Designed as a **self-contained library** with minimal integration effort.

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
implementation("com.github.terrific-live:carousels-android-kotlin-sdk:v1.0.7")
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
        storeId = "1FEyyLAlBJY8000v5nfL",
        carouselId = "sQsA6UF3MwDfIz4TZXM7"
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
    carouselId = "your_carousel_id",
    style = VideoFeatureStyle(
        carouselHeight: Dp = 220.dp,
        cornerRadius: Dp = 16.dp
    )
)
```

---

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

By default the SDK fetches a data from by this base Url

```
"https://terrific-staging-polls.web.app/"
```
You can change it by initializing config in your App file

### Example

```kotlin
class TerrificApp : Application() {

    override fun onCreate() {
        super.onCreate()
        VideoSdk.initialize(
            VideoSdkConfig(
                baseUrl = "https://terrific-live-polls.web.app/"
            )
        )
    }
}
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

