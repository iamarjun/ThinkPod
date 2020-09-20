package com.arjun.thinkpod.service

import android.app.Notification
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Binder
import android.os.IBinder
import android.support.v4.media.session.MediaSessionCompat
import androidx.annotation.MainThread
import androidx.annotation.Nullable
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import com.arjun.thinkpod.BuildConfig
import com.arjun.thinkpod.GlideApp
import com.arjun.thinkpod.R
import com.arjun.thinkpod.model.xml.Item
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.audio.AudioAttributes
import com.google.android.exoplayer2.ext.mediasession.MediaSessionConnector
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.ui.PlayerNotificationManager
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.util.*

class PodcastService : LifecycleService() {

    inner class PodcastServiceBinder : Binder() {
        val service
            get() = this@PodcastService

        val exoPlayer
            get() = this@PodcastService.exoPlayer
    }

    private var playbackTimer: Timer? = null
    private var item: Item? = null

    private lateinit var exoPlayer: SimpleExoPlayer
    private var playerNotificationManager: PlayerNotificationManager? = null
    private var mediaSession: MediaSessionCompat? = null
    private var mediaSessionConnector: MediaSessionConnector? = null

    private val _playerStatusLiveData = MutableLiveData<PlayerStatus>()
    val playerStatusLiveData: LiveData<PlayerStatus>
        get() = _playerStatusLiveData


    override fun onCreate() {
        super.onCreate()

        exoPlayer = SimpleExoPlayer.Builder(this).build()
        val audioAttributes = AudioAttributes.Builder()
            .setUsage(C.USAGE_MEDIA)
            .setContentType(C.CONTENT_TYPE_SPEECH)
            .build()
        exoPlayer.setAudioAttributes(audioAttributes, true)

        // Monitor ExoPlayer events.
        exoPlayer.addListener(PlayerEventListener())

        // Setup notification and media session.
        playerNotificationManager = PlayerNotificationManager.createWithNotificationChannel(
            applicationContext,
            PLAYBACK_CHANNEL_ID,
            R.string.playback_channel_name,
            R.string.playback_channel_description,
            PLAYBACK_NOTIFICATION_ID,
            object : PlayerNotificationManager.MediaDescriptionAdapter {
                override fun getCurrentContentTitle(player: Player): String {
                    return item?.title ?: "..."
                }

                @Nullable
                override fun createCurrentContentIntent(player: Player): PendingIntent? =
                    PendingIntent.getActivity(
                        applicationContext,
                        0,
                        PodcastActivity.instance(applicationContext),
                        PendingIntent.FLAG_UPDATE_CURRENT
                    )

                @Nullable
                override fun getCurrentContentText(player: Player): String? {
                    return null
                }

                @Nullable
                override fun getCurrentLargeIcon(
                    player: Player,
                    callback: PlayerNotificationManager.BitmapCallback
                ): Bitmap? {
                    loadBitmap(item?.itemImages?.get(0)?.itemImageHref, callback)
                    return null
                }
            },
            object : PlayerNotificationManager.NotificationListener {
                override fun onNotificationPosted(
                    notificationId: Int,
                    notification: Notification,
                    ongoing: Boolean
                ) {
                    if (ongoing) {
                        // Make sure the service will not get destroyed while playing media.
                        startForeground(notificationId, notification)
                    } else {
                        // Make notification cancellable.
                        stopForeground(false)
                    }
                }

                override fun onNotificationCancelled(notificationId: Int) {
//                    _playerStatusLiveData.value = PlayerStatus.Cancelled(episodeId)

                    stopSelf()
                }

            }
        ).apply {
            // Omit skip previous and next actions.
            setUseNavigationActions(false)

            // Add stop action.
            setUseStopAction(true)

            val incrementMs = 30000L
            setFastForwardIncrementMs(incrementMs)
            setRewindIncrementMs(incrementMs)

            setPlayer(exoPlayer)
        }

//        // Show lock screen controls and let apps like Google assistant manager playback.
//        mediaSession = MediaSessionCompat(applicationContext, MEDIA_SESSION_TAG).apply {
//            isActive = true
//        }
//
//        playerNotificationManager?.setMediaSessionToken(mediaSession?.sessionToken!!)
//
//        mediaSessionConnector = MediaSessionConnector(mediaSession!!).apply {
//            setQueueNavigator(object : TimelineQueueNavigator(mediaSession) {
//                override fun getMediaDescription(
//                    player: Player,
//                    windowIndex: Int
//                ): MediaDescriptionCompat {
//                    val bitmap =
//                        getBitmapFromVectorDrawable(applicationContext, R.drawable.vd_sed_icon)
//                    val extras = Bundle().apply {
//                        putParcelable(MediaMetadataCompat.METADATA_KEY_ALBUM_ART, bitmap)
//                        putParcelable(MediaMetadataCompat.METADATA_KEY_DISPLAY_ICON, bitmap)
//                    }
//
//                    val title = item?.title ?: getString(R.string.loading_dots)
//
//                    return MediaDescriptionCompat.Builder()
//                        .setIconBitmap(bitmap)
//                        .setTitle(title)
//                        .setExtras(extras)
//                        .build()
//                }
//            })
//
//            val incrementMs = 30000
//            setFastForwardIncrementMs(incrementMs)
//            setRewindIncrementMs(incrementMs)
//
//            setPlayer(exoPlayer)
//        }

    }

    private fun loadBitmap(url: String?, callback: PlayerNotificationManager.BitmapCallback?) {
        GlideApp.with(this)
            .asBitmap()
            .load(url)
            .into(object : CustomTarget<Bitmap>() {
                override fun onResourceReady(
                    resource: Bitmap,
                    transition: Transition<in Bitmap>?
                ) {
                    callback?.onBitmap(resource)
                }

                override fun onLoadCleared(placeholder: Drawable?) {
                    Timber.d(placeholder.toString())
                }
            })
    }

    override fun onBind(intent: Intent): IBinder {
        super.onBind(intent)

        handleIntent(intent)

        return PodcastServiceBinder()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        handleIntent(intent)

        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        cancelPlaybackMonitor()

        mediaSession?.release()
        mediaSessionConnector?.setPlayer(null)
        playerNotificationManager?.setPlayer(null)

        exoPlayer.release()

        super.onDestroy()
    }

    @MainThread
    private fun handleIntent(intent: Intent?) {
        // Play
        intent?.let {
            intent.getParcelableExtra<Item>(PODCAST_ITEM)?.also {
                item = it
                val uri = item?.enclosures?.get(0)?.url
                play(Uri.parse(uri))
            } ?: Timber.w("Playback uri was not set")
        }
    }

    @MainThread
    fun play(uri: Uri) {
        val userAgent = Util.getUserAgent(applicationContext, BuildConfig.APPLICATION_ID)
        val defaultDataSourceFactory = DefaultDataSourceFactory(applicationContext, userAgent)
        val source = ProgressiveMediaSource.Factory(defaultDataSourceFactory)
            .createMediaSource(uri)
        exoPlayer.prepare(source)
        exoPlayer.playWhenReady = true
    }


    @MainThread
    fun resume() {
        exoPlayer.playWhenReady = true
    }

    @MainThread
    fun pause() {
        exoPlayer.playWhenReady = false
    }

    @MainThread
    fun changePlaybackSpeed(playbackSpeed: Float) {
        exoPlayer.setPlaybackParameters(PlaybackParameters(playbackSpeed))
    }

    @MainThread
    private fun monitorPlaybackProgress() {
        if (playbackTimer == null) {
            playbackTimer = Timer()

            playbackTimer?.scheduleAtFixedRate(
                object : TimerTask() {
                    override fun run() {

                        lifecycleScope.launch {
                            withContext(Dispatchers.Main) {
                                if (exoPlayer.duration - exoPlayer.contentPosition <= PLAYBACK_TIMER_DELAY) {
                                    playbackTimer?.cancel()
                                }
                            }
                        }
                    }
                },
                PLAYBACK_TIMER_DELAY,
                PLAYBACK_TIMER_DELAY
            )
        }
    }

    @MainThread
    private fun cancelPlaybackMonitor() {

        playbackTimer?.cancel()
        playbackTimer = null
    }

    private inner class PlayerEventListener : Player.EventListener {

        override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
            if (playbackState == Player.STATE_READY) {
                if (exoPlayer.playWhenReady) {
//                    episodeId?.let { _playerStatusLiveData.value = PlayerStatus.Playing(it) }
                } else {// Paused
//                    episodeId?.let { _playerStatusLiveData.value = PlayerStatus.Paused(it) }
                }
            } else if (playbackState == Player.STATE_ENDED) {
//                episodeId?.let { _playerStatusLiveData.value = PlayerStatus.Ended(it) }
            } else {
//                episodeId?.let { _playerStatusLiveData.value = PlayerStatus.Other(it) }
            }

            // Only monitor playback to record progress when playing.
            if (playbackState == Player.STATE_READY && exoPlayer.playWhenReady) {
                monitorPlaybackProgress()
            } else {
                cancelPlaybackMonitor()
            }
        }


        override fun onPlayerError(e: ExoPlaybackException) {
//            episodeId?.let { _playerStatusLiveData.value = PlayerStatus.Error(it, e) }
        }

    }

    companion object {

        private const val ACTION_RELEASE_OLD_PLAYER = "ACTION_RELEASE_OLD_PLAYER"
        private const val PODCAST_ITEM = "PODCAST_ITEM"

        private const val MEDIA_SESSION_TAG = "sed_audio"
        private const val PLAYBACK_CHANNEL_ID = "playback_channel"
        private const val PLAYBACK_NOTIFICATION_ID = 2
        private const val PLAYBACK_TIMER_DELAY = 5 * 1000L

        private const val ARG_EPISODE_ID = "episode_id"
        private const val ARG_URI = "uri_string"
        private const val ARG_TITLE = "title"
        private const val ARG_START_POSITION = "start_position"


        fun instance(context: Context, item: Item? = null): Intent {
            val serviceIntent = Intent(context, PodcastService::class.java)
            item?.let {
                serviceIntent.putExtra(PODCAST_ITEM, item)
            }
            return serviceIntent
        }

    }

}