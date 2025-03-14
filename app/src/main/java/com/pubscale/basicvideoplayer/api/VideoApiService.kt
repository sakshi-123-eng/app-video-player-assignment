package com.pubscale.basicvideoplayer.api

import com.pubscale.basicvideoplayer.model.VideoResponse
import retrofit2.http.GET

interface VideoApiService {
    @GET("greedyraagava/test/refs/heads/main/video_url.json")
    suspend fun getVideoUrl(): VideoResponse
}