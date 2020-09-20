package com.arjun.thinkpod.service

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.arjun.thinkpod.BaseActivity
import com.arjun.thinkpod.R
import com.arjun.thinkpod.model.xml.Item
import kotlinx.android.synthetic.main.activity_podcast.*
import timber.log.Timber

class PodcastActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_podcast)
        Timber.d("onCreate")

        intent?.let {
            item = it.getParcelableExtra(PODCAST_ITEM)
            PodcastService.instance(this, item).also { intent ->
                // This service will get converted to foreground service using the PlayerNotificationManager notification Id.
                startService(intent)
            }

            show_title.text = item?.title

        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        Timber.d("onNewIntent: ${intent.toString()}")
    }

    companion object {
        private const val PODCAST_ITEM = "PODCAST_ITEM"
        fun instance(context: Context, item: Item? = null): Intent {
            return Intent(context, PodcastActivity::class.java).also {
                it.putExtra(PODCAST_ITEM, item)
            }
        }
    }
}