package com.quanlv.musicplayer.ui.activities.base

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.content.pm.PackageManager
import android.database.sqlite.SQLiteException
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.quanlv.musicplayer.db.DBHelper
import com.quanlv.musicplayer.models.Favorite
import com.quanlv.musicplayer.repository.FavoritesRepository
import com.quanlv.musicplayer.repository.FavoritesRepositoryImplementation
import com.quanlv.musicplayer.repository.PlaylistRepository
import com.quanlv.musicplayer.repository.PlaylistRepositoryImplementation
import com.quanlv.musicplayer.utils.BeatConstants.FAVORITE_ID
import com.quanlv.musicplayer.utils.BeatConstants.FAVORITE_NAME
import com.quanlv.musicplayer.utils.BeatConstants.FAVORITE_TYPE
import org.koin.android.ext.android.get


open class RequestPermissionActivity : AppCompatActivity() {

    // TODO. Make a permission manager

    companion object{
        private const val REQUEST_PERMISSIONS_CODE = 7444
    }

    protected var permissionsGranted: Boolean = false

    val playlistRepository: PlaylistRepository = get()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        verify()
    }

    private fun verify() {
        if (if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                checkSelfPermission(READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED || checkSelfPermission(
                        WRITE_EXTERNAL_STORAGE
                    ) != PackageManager.PERMISSION_GRANTED
            } else {
                TODO("VERSION.SDK_INT < M")
            }
        ) {
            isWriteStoragePermissionGranted()
            isReadStoragePermissionGranted()
        } else {
            permissionsGranted = true
        }
        val frF: FavoritesRepositoryImplementation = get()
        val frP: PlaylistRepositoryImplementation = get()

        try {
            createFavList(frF)
        } catch (ex: SQLiteException) {
            createDB(frF)
        }
        try {
            frP.getPlaylist(-1)
        } catch (ex: SQLiteException) {
            createDB(frP)
        }
    }

    private fun isReadStoragePermissionGranted(): Boolean {
        return if (if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                checkSelfPermission(READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
            } else {
                TODO("VERSION.SDK_INT < M")
            }
        ) {
            true
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(arrayOf(READ_EXTERNAL_STORAGE), REQUEST_PERMISSIONS_CODE)
            }
            false
        }
    }

    private fun createFavList(favoritesRepository: FavoritesRepository) {
        val favorite = favoritesRepository.getFavorite(FAVORITE_ID)
        if (favorite.id == -1L) {
            favoritesRepository.createFavorite(
                Favorite(
                    FAVORITE_ID,
                    FAVORITE_NAME,
                    FAVORITE_NAME,
                    FAVORITE_ID,
                    0,
                    0,
                    FAVORITE_TYPE
                )
            )
        }
    }

    private fun createDB(fr: DBHelper) {
        fr.onCreate(fr.writableDatabase)
    }

    private fun isWriteStoragePermissionGranted(): Boolean {
        return if (if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                checkSelfPermission(WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
            } else {
                TODO("VERSION.SDK_INT < M")
            }
        ) {
            true
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(arrayOf(WRITE_EXTERNAL_STORAGE), REQUEST_PERMISSIONS_CODE)
            }
            false
        }
    }

    protected open fun recreateActivity() {
        finish()
        overridePendingTransition(0, 0)
        startActivity(intent)
        overridePendingTransition(0, 0)
    }

    protected open fun onPermissionsGrantResult(result: Boolean) {
        permissionsGranted = result
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_PERMISSIONS_CODE -> {
                onPermissionsGrantResult(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            }
        }
    }
}
