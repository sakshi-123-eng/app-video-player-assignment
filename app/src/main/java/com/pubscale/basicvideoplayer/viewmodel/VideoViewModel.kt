package com.pubscale.basicvideoplayer.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pubscale.basicvideoplayer.model.VideoState
import com.pubscale.basicvideoplayer.repository.VideoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VideoViewModel @Inject constructor(
    private val videoRepository: VideoRepository
) : ViewModel() {
    private val _videoState = MutableStateFlow<VideoState>(VideoState.Loading)
    val videoState: StateFlow<VideoState> get() = _videoState

    fun fetchVideoUrl() {
        viewModelScope.launch {
            _videoState.value = videoRepository.fetchVideoUrl()
        }
    }
}