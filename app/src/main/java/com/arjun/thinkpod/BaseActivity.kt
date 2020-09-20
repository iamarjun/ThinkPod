package com.arjun.thinkpod

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.media.AudioManager
import android.os.IBinder
import androidx.appcompat.app.AppCompatActivity
import com.arjun.thinkpod.model.xml.Item
import com.arjun.thinkpod.service.PodcastService
import com.arjun.thinkpod.util.isServiceRunning

abstract class BaseActivity : AppCompatActivity() {
    private var podcastService: PodcastService? = null
    var item: Item? = null

    private val connection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val binder = service as PodcastService.PodcastServiceBinder
            podcastService = binder.service

            // Pass player updates to interested observers.
            podcastService?.playerStatusLiveData?.observe(this@BaseActivity, {
//                _playerStatusLiveData.value = it

//                if (it is PlayerStatus.Cancelled) {
//                    stopAudioService()
//                }
            })

            // Show player after config change.
//            val episodeId = podcastService?.episodeId
//            if (episodeId != null) {

//                viewModel.refreshIfNecessary(episodeId)
//            }
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            podcastService = null
        }
    }

    private fun bindToAudioService() {
        if (podcastService == null) {
            PodcastService.instance(baseContext, item).also { intent ->
                bindService(intent, connection, Context.BIND_AUTO_CREATE)
            }
        }
    }


    private fun unbindAudioService() {
        if (podcastService != null) {
            unbindService(connection)
            podcastService = null
        }
    }

    private fun stopAudioService() {
        podcastService?.pause()

        unbindAudioService()
        stopService(Intent(this, PodcastService::class.java))

        podcastService = null
    }

    override fun onStart() {
        super.onStart()

        // Show the player, if the audio service is already running.
        if (applicationContext.isServiceRunning(PodcastService::class.java.name)) {
            bindToAudioService()
        }
    }

    override fun onResume() {
        super.onResume()
        // Set the audio stream so the app responds to the volume control on the device
        volumeControlStream = AudioManager.STREAM_MUSIC
    }

    override fun onStop() {
        unbindAudioService()
        super.onStop()
    }

}