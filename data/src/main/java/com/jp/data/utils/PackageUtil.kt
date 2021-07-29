package com.jp.data.utils

import android.content.Context
import android.content.pm.PackageManager
import okhttp3.internal.Version

object PackageUtil {

    @JvmStatic
    fun getAppAgentInfo(context: Context): String {
        return try {
            val appName = context.applicationInfo.loadLabel(context.packageManager)
            val version = context.packageManager.getPackageInfo(context.packageName, 0).versionName

            appName.toString() + "/" + version + " " + System.getProperty("http.agent") + " " + Version.userAgent()
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
            ""
        }
    }
}