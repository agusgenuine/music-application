package com.quanlv.musicplayer.ui.activities

import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import com.quanlv.musicplayer.R
import com.quanlv.musicplayer.databinding.ActivitySettingsBinding
import com.quanlv.musicplayer.extensions.getColorByTheme
import com.quanlv.musicplayer.ui.activities.base.BaseActivity
import com.quanlv.musicplayer.alertdialog.AlertDialog
import com.quanlv.musicplayer.alertdialog.actions.AlertItemAction
import com.quanlv.musicplayer.alertdialog.stylers.AlertItemStyle
import com.quanlv.musicplayer.alertdialog.enums.AlertItemTheme
import com.quanlv.musicplayer.alertdialog.enums.AlertType
import com.quanlv.musicplayer.utils.BeatConstants
import com.quanlv.musicplayer.utils.SettingsUtility
import org.koin.android.ext.android.inject

class SettingsActivity : BaseActivity() {

    private lateinit var binding: ActivitySettingsBinding
    private lateinit var dialog: AlertDialog
    private val settingsUtility by inject<SettingsUtility>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_settings)
        init()
    }

    private fun init() {
        dialog = buildThemeDialog()

        binding.let {
            it.executePendingBindings()

            it.lifecycleOwner = this
        }
    }

    private fun buildThemeDialog(): AlertDialog {
        val style = AlertItemStyle()
        style.apply {
            textColor = getColorByTheme(R.attr.titleTextColor)
            selectedTextColor = getColorByTheme(R.attr.colorAccent)
            backgroundColor = getColorByTheme(R.attr.colorPrimarySecondary2)
        }
        return AlertDialog(
            getString(R.string.theme_title),
            getString(R.string.theme_description),
            style,
            AlertType.BOTTOM_SHEET
        ).apply {
            addItem(AlertItemAction(
                getString(R.string.default_theme),
                settingsUtility.currentTheme == BeatConstants.AUTO_THEME,
                AlertItemTheme.DEFAULT
            ) {
                it.selected = true
                settingsUtility.currentTheme =
                    BeatConstants.AUTO_THEME
                recreateActivity()
            })
            addItem(AlertItemAction(
                getString(R.string.light_theme),
                settingsUtility.currentTheme == BeatConstants.LIGHT_THEME,
                AlertItemTheme.DEFAULT
            ) {
                it.selected = true
                settingsUtility.currentTheme =
                    BeatConstants.LIGHT_THEME
                recreateActivity()
            })
            addItem(AlertItemAction(
                getString(R.string.dark_theme),
                settingsUtility.currentTheme == BeatConstants.DARK_THEME,
                AlertItemTheme.DEFAULT
            ) {
                it.selected = true
                settingsUtility.currentTheme =
                    BeatConstants.DARK_THEME
                recreateActivity()
            })
        }
    }

    fun showThemes(view: View) {
        try {
            dialog.show(this)
        } catch (ex: IllegalStateException) {
        }
    }
}
