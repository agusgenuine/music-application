package com.quanlv.musicplayer.alias

import com.quanlv.musicplayer.playback.AudioFocusHelper
import com.quanlv.musicplayer.playback.players.BeatPlayer

typealias OnPrepared<T> = T.() -> Unit
typealias OnError<T> = T.(error: Throwable) -> Unit
typealias OnCompletion<T> = T.() -> Unit
typealias OnMetaDataChanged = BeatPlayer.() -> Unit
typealias OnIsPlaying = BeatPlayer.(playing: Boolean, byUi: Boolean) -> Unit
typealias LiveDataFilter<T> = (T) -> Boolean
typealias OnAudioFocusGain = AudioFocusHelper.() -> Unit
typealias OnAudioFocusLoss = AudioFocusHelper.() -> Unit
typealias OnAudioFocusLossTransient = AudioFocusHelper.() -> Unit
typealias OnAudioFocusLossTransientCanDuck = AudioFocusHelper.() -> Unit
