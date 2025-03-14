package com.pubscale.basicvideoplayer.repository

import com.pubscale.basicvideoplayer.api.VideoApiService
import com.pubscale.basicvideoplayer.model.VideoState
import javax.inject.Inject

class VideoRepository @Inject constructor(
    private val videoApiService: VideoApiService
){

    suspend fun fetchVideoUrl(): VideoState {
        return try {
            val response = videoApiService.getVideoUrl()
            VideoState.Success(response.url)
        } catch (e: Exception) {
            VideoState.Error("Network error: ${e.message}")
        }
    }

}