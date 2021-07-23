package com.mena97villalobos.issue_reporter.model

import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Build
import java.util.*

/** Holds device information to share it on the issue */
class DeviceInfo(context: Context) {
    private var versionCode = 0
    private var versionName: String? = null
    private val buildVersion = Build.VERSION.INCREMENTAL
    private val releaseVersion = Build.VERSION.RELEASE
    private val sdkVersion = Build.VERSION.SDK_INT
    private val buildID = Build.DISPLAY
    private val brand = Build.BRAND
    private val manufacturer = Build.MANUFACTURER
    private val device = Build.DEVICE
    private val model = Build.MODEL
    private val product = Build.PRODUCT
    private val hardware = Build.HARDWARE

    private val abis = Build.SUPPORTED_ABIS
    private val abis32Bits = Build.SUPPORTED_32_BIT_ABIS
    private val abis64Bits = Build.SUPPORTED_64_BIT_ABIS

    init {
        val packageInfo: PackageInfo? =
            try {
                context.packageManager.getPackageInfo(context.packageName, 0)
            } catch (e: PackageManager.NameNotFoundException) {
                null
            }
        if (packageInfo != null) {
            versionCode = packageInfo.longVersionCode.toInt()
            versionName = packageInfo.versionName
        } else {
            versionCode = -1
            versionName = null
        }
    }

    /** Converts device information into markdown table */
    fun toMarkdown(): String {
        return """
             Device info:
             ---
             <table>
             <tr><td>App version</td><td>$versionName</td></tr>
             <tr><td>App version code</td><td>$versionCode</td></tr>
             <tr><td>Android build version</td><td>$buildVersion</td></tr>
             <tr><td>Android release version</td><td>$releaseVersion</td></tr>
             <tr><td>Android SDK version</td><td>$sdkVersion</td></tr>
             <tr><td>Android build ID</td><td>$buildID</td></tr>
             <tr><td>Device brand</td><td>$brand</td></tr>
             <tr><td>Device manufacturer</td><td>$manufacturer</td></tr>
             <tr><td>Device name</td><td>$device</td></tr>
             <tr><td>Device model</td><td>$model</td></tr>
             <tr><td>Device product name</td><td>$product</td></tr>
             <tr><td>Device hardware name</td><td>$hardware</td></tr>
             <tr><td>ABIs</td><td>${Arrays.toString(abis)}</td></tr>
             <tr><td>ABIs (32bit)</td><td>${Arrays.toString(abis32Bits)}</td></tr>
             <tr><td>ABIs (64bit)</td><td>${Arrays.toString(abis64Bits)}</td></tr>
             </table>
             
             """.trimIndent()
    }

    override fun toString(): String {
        return """
             App version: $versionName
             App version code: $versionCode
             Android build version: $buildVersion
             Android release version: $releaseVersion
             Android SDK version: $sdkVersion
             Android build ID: $buildID
             Device brand: $brand
             Device manufacturer: $manufacturer
             Device name: $device
             Device model: $model
             Device product name: $product
             Device hardware name: $hardware
             ABIs: ${Arrays.toString(abis)}
             ABIs (32bit): ${Arrays.toString(abis32Bits)}
             ABIs (64bit): ${Arrays.toString(abis64Bits)}
             """.trimIndent()
    }
}
