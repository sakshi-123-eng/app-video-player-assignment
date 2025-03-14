# Floating Mini-Player with Picture-in-Picture Mode in Android

## Overview
This project implements a floating mini-player with Picture-in-Picture (PiP) mode in an Android video player app. The app dynamically fetches video URLs from a JSON source using MVVM architecture and utilizes Dagger-Hilt for dependency injection. ExoPlayer is used for media playback, and the UI is updated based on video state using Kotlin's StateFlow.

## Features
- **Picture-in-Picture (PiP) Mode**: Allows users to continue watching videos in a floating window when the app goes into the background.
- **MVVM Architecture**: Ensures a clean separation of concerns between the UI and business logic.
- **StateFlow for Live Data Handling**: Manages video state updates efficiently.
- **Dagger-Hilt for Dependency Injection**: Provides a scalable way to manage dependencies.
- **ExoPlayer Integration**: Handles video playback with smooth user controls.
- **Error Handling and Fallback Video**: Loads a default video if the fetched video URL fails.

## Project Structure
```
com.pubscale.basicvideoplayer
│── model
│   ├── VideoState.kt    # Defines loading, success, and error states
│
│── viewmodel
│   ├── VideoViewModel.kt # Fetches video URLs and updates state using StateFlow
│
│── MainActivity.kt       # Implements PiP mode and video playback
│── di
│   ├── NetworkModule.kt      # Provides dependencies using Dagger-Hilt
```

## Implementation Details
### 1. **Picture-in-Picture Mode Handling**
PiP mode is enabled when the user navigates away from the app:
```kotlin
override fun onUserLeaveHint() {
    super.onUserLeaveHint()
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        enterPictureInPictureMode(
            PictureInPictureParams.Builder()
                .setAspectRatio(Rational(16, 9))
                .build()
        )
    }
}
```
The `onPictureInPictureModeChanged` function adjusts UI behavior when PiP mode is enabled or disabled:
```kotlin
override fun onPictureInPictureModeChanged(
    isInPictureInPictureMode: Boolean,
    newConfig: Configuration
) {
    super.onPictureInPictureModeChanged(isInPictureInPictureMode, newConfig)
    playerView?.useController = !isInPictureInPictureMode
}
```

### 2. **MVVM Architecture & StateFlow for Video Streaming**
The `VideoViewModel` fetches video URLs dynamically and exposes state updates via StateFlow:
```kotlin
viewModel.videoState.collect { state ->
    when (state) {
        is VideoState.Loading -> showLoading()
        is VideoState.Success -> setupExoPlayer(state.url)
        is VideoState.Error -> showFallbackVideo()
    }
}
```

### 3. **ExoPlayer Setup**
ExoPlayer is initialized and configured to play the dynamically fetched video:
```kotlin
private fun setupExoPlayer(mediaItem: MediaItem) {
    playerView = findViewById(R.id.player_view)
    player = ExoPlayer.Builder(this).build()
    playerView?.player = player
    player?.setMediaItem(mediaItem)
    player?.prepare()
    player?.playWhenReady = true
}
```






This is Fully Functional according to asked useCases 🚀.
