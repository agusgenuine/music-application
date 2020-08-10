package com.quanlv.musicplayer

import android.app.Application
import com.crashlytics.android.Crashlytics
import com.quanlv.musicplayer.BuildConfig.DEBUG
import com.quanlv.musicplayer.notifications.notificationModule
import com.quanlv.musicplayer.playback.playbackModule
import com.quanlv.musicplayer.repository.repositoriesModule
import com.quanlv.musicplayer.ui.viewmodels.viewModelModule
import com.quanlv.musicplayer.utils.ReleaseTree
import com.quanlv.musicplayer.utils.utilsModule
import io.fabric.sdk.android.Fabric
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import timber.log.Timber


class MusicPlayerApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        //Fabric.with(this, Crashlytics())

        if (DEBUG) {
            Timber.plant(Timber.DebugTree())
        } else {
            Timber.plant(ReleaseTree())
        }

        val modules = listOf(
            mainModel,
            notificationModule,
            playbackModule,
            repositoriesModule,
            viewModelModule,
            utilsModule
        )
        startKoin {
            androidContext(this@MusicPlayerApplication)
            modules(modules)
        }
    }
}