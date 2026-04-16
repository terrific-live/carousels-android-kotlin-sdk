package demo.terrific.compose.compose.common

internal sealed interface VideoScreen {
    data object Carousel : VideoScreen
    data object Feed : VideoScreen
}