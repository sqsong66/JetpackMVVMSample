package com.example.jetpacksample.utils

import android.annotation.SuppressLint
import android.content.Context
import android.media.RingtoneManager
import android.net.ConnectivityManager
import android.os.Environment
import android.provider.Settings
import java.io.File
import java.math.RoundingMode
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.*
import java.util.regex.Pattern
import kotlin.math.log10
import kotlin.math.pow

object CommonUtils {

    private val EMAIL_ADDRESS_REGEX =
        Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE)

    fun isEmailValid(emailAddress: String?): Boolean {
        if (emailAddress == null) return false
        val matcher = EMAIL_ADDRESS_REGEX.matcher(emailAddress)
        return matcher.find()
    }

    fun isNetworkAvailable(context: Context): Boolean {
        val manager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = manager.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
    }

    fun getVersionName(context: Context): String? {
        try {
            return context.packageManager.getPackageInfo(context.packageName, 0).versionName
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
        return null
    }

    fun getVersionCode(context: Context): Long {
        try {
            return if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.P) {
                context.packageManager.getPackageInfo(context.packageName, 0).longVersionCode
            } else {
                context.packageManager.getPackageInfo(context.packageName, 0).versionCode.toLong()
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
        return 0
    }

    fun playDefaultSound(context: Context?) {
        try {
            val uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
            val ringtone = RingtoneManager.getRingtone(context, uri)
            ringtone.play()
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }

    @SuppressLint("HardwareIds")
    fun getAndroidId(context: Context): String =
        Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)

    fun getAndroidVersion() = "${android.os.Build.MODEL},${android.os.Build.VERSION.RELEASE}"

    private fun isSDCardMounted(): Boolean =
        Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED

    private fun getAppFilePath(rootDirName: String): String {
        return if (isSDCardMounted()) {
            Environment.getExternalStorageDirectory().absolutePath + File.separator + rootDirName
        } else {
            Environment.getDataDirectory().absolutePath + File.separator + rootDirName
        }
    }

    fun getTempFilePath(rootDirName: String): String = getAppFilePath(rootDirName) + "/temp"

    fun getApkFilePath(rootDirName: String): String = getAppFilePath(rootDirName) + "/apk"

    fun getImageFilePath(rootDirName: String): String = getAppFilePath(rootDirName) + "/image"

    fun getAdsFilePath(rootDirName: String): String = getAppFilePath(rootDirName) + "/ads"

    fun getReadableFileSize(size: Long): String {
        if (size <= 0) return "0M"
        val units = arrayOf("B", "KB", "MB", "GB", "TB")
        val digitGroups = (log10(size.toDouble()) / log10(1024.0)).toInt()
        return DecimalFormat("#,##0.#").format(size / 1024.0.pow(digitGroups.toDouble())) + " " + units[digitGroups]
    }

    fun deleteTempFile(rootDirName: String) {
        val tempDir = File(getTempFilePath(rootDirName))
        if (tempDir.exists()) {
            deleteDir(tempDir)
        }
    }

    private fun deleteDir(tempDir: File): Boolean {
        if (tempDir.isDirectory) {
            val fileList = tempDir.listFiles()
            if (fileList != null) {
                for (file in fileList) {
                    deleteDir(file)
                }
            }
        }
        return tempDir.delete()
    }

    fun formatDoubleWithComma(
        number: Double,
        decimal: Int = 2,
        isInteger: Boolean = false
    ): String {
        return if (isInteger) {
            String.format(Locale.getDefault(), "%,d", number.toInt())
        } else {
            val formatter: DecimalFormat =
                NumberFormat.getInstance(Locale.getDefault()) as DecimalFormat
            val symbols = formatter.decimalFormatSymbols
            symbols.groupingSeparator = ','
            formatter.roundingMode = RoundingMode.HALF_UP
            formatter.minimumFractionDigits = decimal
            formatter.maximumFractionDigits = decimal
            formatter.decimalFormatSymbols = symbols
            // formatter.applyPattern("0.#")
            formatter.format(number)
        }
    }

    fun isValidIDCard(idCard: String?): Boolean {
        if (idCard == null || idCard.isEmpty()) return false
        val regex = "[1-9]\\d{13,16}[a-zA-Z0-9]"
        return Pattern.matches(regex, idCard)
    }

    fun getFolderSize(dir: File): Long {
        if (dir.exists()) {
            var result = 0L
            dir.listFiles()?.forEach {
                result += if (it.isDirectory) {
                    getFolderSize(it)
                } else {
                    it.length()
                }
            }
            return result
        }
        return 0
    }

}