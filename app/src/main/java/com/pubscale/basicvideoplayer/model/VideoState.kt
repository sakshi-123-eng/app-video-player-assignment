package com.pubscale.basicvideoplayer.model

sealed class VideoState {
    data object Loading : VideoState()
    data class Success(val url: String) : VideoState()
    data class Error(val message: String) : VideoState()
}