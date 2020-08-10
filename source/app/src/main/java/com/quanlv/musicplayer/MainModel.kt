package com.quanlv.musicplayer

import android.content.ComponentName
import com.quanlv.musicplayer.playback.services.BeatPlayerService
import com.quanlv.musicplayer.playback.PlaybackConnection
import com.quanlv.musicplayer.playback.PlaybackConnectionImplementation
import org.koin.dsl.bind
import org.koin.dsl.module

val mainModel = module {
    single {
        val component = ComponentName(get(), BeatPlayerService::class.java)
        PlaybackConnectionImplementation(get(), component)
    } bind PlaybackConnection::class
}