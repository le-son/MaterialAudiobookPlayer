/*
 * This file is part of Material Audiobook Player.
 *
 * Material Audiobook Player is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or any later version.
 *
 * Material Audiobook Player is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Material Audiobook Player. If not, see <http://www.gnu.org/licenses/>.
 * /licenses/>.
 */

package de.ph1b.audiobook.persistence.internals

import android.content.SharedPreferences
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import org.json.JSONArray

fun Cursor.string(columnName: String): String {
    return stringNullable(columnName)!!
}

fun Cursor.stringNullable(columnName: String): String? {
    return getString(getColumnIndexOrThrow(columnName))
}

fun Cursor.float(columnName: String): Float {
    return getFloat(getColumnIndexOrThrow(columnName))
}

fun Cursor.long(columnName: String): Long {
    return getLong(getColumnIndexOrThrow(columnName))
}

fun Cursor.int(columnName: String): Int {
    return getInt(getColumnIndexOrThrow(columnName))
}

inline fun <T> SQLiteDatabase.asTransaction(func: SQLiteDatabase.() -> T): T {
    beginTransaction()
    try {
        val result = func()
        setTransactionSuccessful()
        return result
    } finally {
        endTransaction()
    }
}

inline fun SharedPreferences.edit(func: SharedPreferences.Editor.() -> Unit) {
    val editor = this.edit()
    editor.func()
    editor.apply()
}

inline fun Cursor.moveToNextLoop(func: Cursor.() -> Unit) {
    try {
        while (moveToNext()) {
            func()
        }
    } finally {
        close()
    }
}

fun SharedPreferences.Editor.setString(pair: Pair<String, String>) =
        putString(pair.first, pair.second)

fun SharedPreferences.Editor.setLong(pair: Pair<String, Long>) =
        putLong(pair.first, pair.second)

fun SharedPreferences.Editor.setInt(pair: Pair<String, Int>) =
        putInt(pair.first, pair.second)

fun SharedPreferences.Editor.setBoolean(pair: Pair<String, Boolean>) =
        putBoolean(pair.first, pair.second)

fun SharedPreferences.Editor.setStringSet(pair: Pair<String, Set<String>>) =
        putStringSet(pair.first, pair.second)

fun SQLiteDatabase.simpleQuery(table: String,
                               columns: Array<String>? = null,
                               selection: String? = null,
                               selectionArgs: Array<String>? = null,
                               groupBy: String? = null,
                               having: String ? = null,
                               orderBy: String? = null,
                               limit: String? = null,
                               distinct: Boolean = false): Cursor {
    return query(distinct, table, columns, selection, selectionArgs, groupBy, having, orderBy, limit)
}

inline fun <T> JSONArray.forEach(action: (T) -> Unit): Unit {
    for (i in 0..length() - 1) {
        @Suppress("UNCHECKED_CAST")
        action(get(i) as T)
    }
}