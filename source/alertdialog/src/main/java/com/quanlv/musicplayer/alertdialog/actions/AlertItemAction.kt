/*
 * Copyright (c) 2020. Carlos René Ramos López. All rights reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.quanlv.musicplayer.alertdialog.actions

import android.view.View
import androidx.annotation.DrawableRes
import com.quanlv.musicplayer.alertdialog.enums.AlertItemTheme

data class AlertItemAction(
    val title: String = "",
    var selected: Boolean = false,
    val theme: AlertItemTheme? = AlertItemTheme.DEFAULT,
    @DrawableRes val icon: Int = -1,
    var input: String = "",
    var root: View? = null,
    var action: (AlertItemAction) -> Unit = {}
)