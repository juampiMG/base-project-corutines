package com.jp.data.utils

import android.content.SharedPreferences
import android.util.Log
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import android.R.id.edit


class AdvancedPreferences(settings: SharedPreferences) {
    private val TAG = AdvancedPreferences::class.java.simpleName

    var mSettings: SharedPreferences = settings
    var mEditor: SharedPreferences.Editor = mSettings.edit()
    private val mGson = Gson()


    fun getAll(): Map<String, *> {
        return mSettings.all
    }

    operator fun contains(key: String): Boolean {
        return mSettings.contains(key)
    }

    fun remove(key: String): SharedPreferences.Editor {
        mEditor.remove(key)
        return mEditor
    }

    fun clear(): SharedPreferences.Editor {
        mEditor.clear()
        return mEditor
    }

    fun commit(): Boolean {
        return mEditor.commit()
    }

    fun apply() {
        mEditor.apply()
    }

    operator fun get(key: String, defaultValue: Int): Int {
        return mSettings.getInt(key, defaultValue)
    }

    operator fun get(key: String, defaultValue: Set<String>): Set<String>? {
        return mSettings.getStringSet(key, defaultValue)
    }

    operator fun get(key: String, defaultValue: String): String? {
        return mSettings.getString(key, defaultValue)
    }

    operator fun get(key: String, defaultValue: Boolean): Boolean {
        return mSettings.getBoolean(key, defaultValue)
    }

    operator fun get(key: String, defaultValue: Long): Long {
        return mSettings.getLong(key, defaultValue)
    }

    operator fun get(key: String, defaultValue: Float): Float {
        return mSettings.getFloat(key, defaultValue)
    }

    operator fun get(key: String, defaultValue: JSONObject): JSONObject {
        var jsonObject: JSONObject? = null
        try {
            jsonObject = JSONObject(get(key, ""))
        } catch (e: JSONException) {
            Log.e(TAG, e.message, e)
        }

        return jsonObject ?: defaultValue
    }

    operator fun get(key: String, defaultValue: JSONArray): JSONArray {
        var jsonArray: JSONArray? = null
        try {
            jsonArray = JSONArray(get(key, ""))
        } catch (e: JSONException) {
            Log.e(TAG, e.message, e)
        }

        return jsonArray ?: defaultValue
    }

    operator fun <T> get(key: String, classType: Class<T>, defaultValue: T): T {
        var `object`: T? = null
        val gson = get(key, "")
        if (gson != null && !gson.isEmpty()) {
            try {
                `object` = mGson.fromJson(gson, classType)
            } catch (e: JsonSyntaxException) {
                Log.e(TAG, e.message, e)
            }

        }
        return `object` ?: defaultValue
    }

    private fun put(key: String, value: Int): SharedPreferences.Editor {
        mEditor.putInt(key, value)
        return mEditor
    }

    private fun put(key: String, value: Set<String>): SharedPreferences.Editor {
        mEditor.putStringSet(key, value)
        return mEditor
    }

    private fun put(key: String, value: String): SharedPreferences.Editor {
        mEditor.putString(key, value)
        return mEditor
    }

    private fun put(key: String, value: Boolean): SharedPreferences.Editor {
        mEditor.putBoolean(key, value)
        return mEditor
    }

    private fun put(key: String, value: Long): SharedPreferences.Editor {
        mEditor.putLong(key, value)
        return mEditor
    }

    private fun put(key: String, value: Float): SharedPreferences.Editor {
        mEditor.putFloat(key, value)
        return mEditor
    }

    private fun put(key: String, value: JSONObject): SharedPreferences.Editor {
        mEditor.putString(key, value.toString())
        return mEditor
    }

    private fun put(key: String, value: JSONArray): SharedPreferences.Editor {
        mEditor.putString(key, value.toString())
        return mEditor
    }

    fun put(key: String, value: Any): SharedPreferences.Editor {
        if (value is Int) put(key, value)
        else if (value is Set<*>) put(key, value as Set<String>)
        else if (value is String) put(key, value)
        else if (value is Boolean) put(key, value)
        else if (value is Long) put(key, value)
        else if (value is Float) put(key, value)
        else if (value is JSONObject) put(key, value)
        else if (value is JSONArray) put(key, value)
        else put(key, mGson.toJson(value))
        return mEditor
    }

    fun getEditor(): SharedPreferences.Editor {
        return mEditor
    }

    fun getSharedPreferences(): SharedPreferences {
        return mSettings
    }

    fun <T> setList(key: String, list: List<T>) {
        val gson = Gson()
        val json = gson.toJson(list)
        set(key, json)
    }

    operator fun set(key: String, value: String) {
        mEditor.putString(key, value)
        mEditor.commit()
    }
}