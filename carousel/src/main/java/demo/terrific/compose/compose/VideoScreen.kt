package demo.terrific.compose.compose

internal sealed interface VideoScreen {
    data object Carousel : VideoScreen
    data object Feed : VideoScreen
}