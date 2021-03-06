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

package com.quanlv.musicplayer.db

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.SQLException
import android.database.sqlite.SQLiteConstraintException
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper


open class DBHelper(context: Context?, factory: SQLiteDatabase.CursorFactory? = null) :
    SQLiteOpenHelper(
        context,
        DATABASE_NAME, factory,
        DATABASE_VERSION
    ) {

    companion object {
        const val DATABASE_VERSION = 1
        const val DATABASE_NAME = "com.quanlv.listentome.db"
    }

    protected open fun insertRow(
        tableName: String,
        columns: Array<String>,
        values: Array<String>
    ): Int {
        if (columns.size != values.size)
            throw SQLException("The column names size must be the same as values size")

        val contentValues = ContentValues()

        for ((index, column) in columns.iterator().withIndex()) {
            contentValues.put(column, values[index])
        }

        val db = this.writableDatabase
        return try {
            db.insert(tableName, null, contentValues).toInt()
        } catch (ex: SQLException) {
            -1
        }
    }

    open fun bulkInsert(tableName: String, values: Array<ContentValues?>): Int {
        val db = this.writableDatabase
        db.beginTransaction()
        return try {
            for (cv in values) {
                val rowID: Long = db.insert(tableName, " ", cv)
                if (rowID <= 0) {
                    throw SQLException("Failed to insert row into $tableName")
                }
            }
            db.setTransactionSuccessful()
            values.size
        } catch (ex: SQLiteConstraintException) {
            -1
        } catch (ex: SQLException) {
            -1
        } finally {
            db.endTransaction()
        }
    }

    protected open fun deleteRow(
        tableName: String,
        whereClause: String,
        whereArgs: Array<String>
    ): Int {
        val db = this.writableDatabase
        return db.delete(tableName, whereClause, whereArgs)
    }

    protected open fun getRow(
        tableName: String,
        selection: String,
        whereClauses: String? = null,
        whereArgs: Array<String> = emptyArray(),
        orderClause: String? = null
    ): Cursor {
        val db = this.readableDatabase
        val sql = StringBuilder("SELECT ").apply {
            append(selection)
            append(" FROM ")
            append(tableName)
            if (whereClauses != null) append(" WHERE $whereClauses")
            if (orderClause != null) append(" ORDER BY $orderClause")
        }
        return db.rawQuery(
            sql.toString(),
            whereArgs
        )
    }

    protected open fun updateRow(
        tableName: String,
        values: ContentValues,
        whereClause: String,
        updateArgs: Array<String>
    ): Int {
        val db = this.writableDatabase
        return db.update(tableName, values, whereClause, updateArgs)
    }

    override fun onCreate(db: SQLiteDatabase?) {}
    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {}
}