package com.pubscale.basicvideoplayer

import android.app.PictureInPictureParams
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.util.Rational
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.lifecycle.lifecycleScope
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import com.pubscale.basicvideoplayer.model.VideoState
import com.pubscale.basicvideoplayer.viewmodel.VideoViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private var player: ExoPlayer? = null
    private var playerView: PlayerView? = null
    private val viewModel: VideoViewModel by viewModels()
    private lateinit var progress: ProgressBar
    private lateinit var backPressCallback: OnBackPressedCallback

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        progress = findViewById(R.id.progressbar)

        backPressCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    enterPictureInPictureMode(
                        PictureInPictureParams.Builder()
                            .setAspectRatio(Rational(16, 9))
                            .build()
                    )
                } else {
                    finish()
                }
            }
        }

        onBackPressedDispatcher.addCallback(this, backPressCallback)

        lifecycleScope.launch {
            viewModel.videoState.collect { state ->
                when (state) {
                    is VideoState.Loading -> {
                        progress.visibility = View.VISIBLE
                    }

                    is VideoState.Success -> {
                        progress.visibility = View.GONE
                        val mediaItem = MediaItem.fromUri(state.url.toUri())
                        setupExoPLayer(mediaItem)
                    }

                    is VideoState.Error -> {
                        progress.visibility = View.GONE
                        val videoUri =
                            ("android.resource://" + packageName + "/" + R.raw.sample_video).toUri()
                        val mediaItem = MediaItem.fromUri(videoUri)
                        setupExoPLayer(mediaItem)
                        Toast.makeText(
                            applicationContext,
                            "Something went wrong",
                            Toast.LENGTH_SHORT
                        )
                    }
                }
            }
        }

        viewModel.fetchVideoUrl()
    }

    private fun setupExoPLayer(mediaItem: MediaItem) {
        playerView = findViewById(R.id.player_view)
        playerView?.visibility = View.VISIBLE
        player = ExoPlayer.Builder(this).build()
        playerView?.player = player
        player?.setMediaItem(mediaItem)
        player?.prepare()
        player?.playWhenReady = true
    }

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

    override fun onPictureInPictureModeChanged(
        isInPictureInPictureMode: Boolean,
        newConfig: Configuration
    ) {
        super.onPictureInPictureModeChanged(isInPictureInPictureMode, newConfig)
        if (isInPictureInPictureMode) {
            playerView?.useController = false
        } else {
            playerView?.useController = true
        }
    }


    override fun onPause() {
        super.onPause()
        if (isInPictureInPictureMode) {
            player?.playWhenReady = true
        } else {
            player?.pause()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        backPressCallback.remove()
        player?.release()
        player = null
    }

    override fun onRestart() {
        super.onRestart()
        player?.play()
    }

    override fun onStop() {
        super.onStop()
        player?.pause()
    }
}