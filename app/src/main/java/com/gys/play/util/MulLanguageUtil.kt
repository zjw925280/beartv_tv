package com.gys.play.util

import android.app.Activity
import android.content.Context
import android.os.Build
import android.os.LocaleList
import android.util.Log
import com.gys.play.R
import com.gys.play.SpServiceConfig
import com.tencent.mmkv.MMKV
import java.util.*

object MulLanguageUtil {

    val SP_LANGUAGE = "SP_LANGUAGE"
    val SP_COUNTRY = "SP_COUNTRY"

    fun checkSetLanguage(activity: Activity) {
        val defaultLanguage = when (SpServiceConfig.getServiceConfig().default_language) {
            2 -> "en"
            3 -> "zh"
            4 -> "th"
            else -> "zh"
        }
        val currentLanguage = when (activity.getString(R.string.lang_type)) {
            "2" -> "en"
            "3" -> "zh"
            "4" -> "th"
            else -> "zh"
        }

        val spLanguage = MMKV.defaultMMKV()!!.getString(SP_LANGUAGE, defaultLanguage)
        val spCountry = MMKV.defaultMMKV()!!.getString(SP_COUNTRY, "")
        Log.d("mulLanguage", "defaultLanguage = $defaultLanguage")
        Log.d("mulLanguage", "currentLanguage = $currentLanguage")
        Log.d("mulLanguage", "spLanguage = $spLanguage")
        if (currentLanguage == spLanguage) {
            return
        }
        setAppLanguage(activity, spLanguage, spCountry)
    }

    /**
     * 获取应用语言
     */
    fun getLocale(context: Context): Locale {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            context.resources.configuration.locales[0]
        } else {
            context.resources.configuration.locale
        }
    }

    /**
     * 获取系统语言
     */
    fun getSystemLocale(): Locale {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            LocaleList.getDefault().get(0)
        } else {
            Locale.getDefault()
        }
    }

    /**
     * 更新应用语言（核心）
     * PS:Context Activity都设置一遍！
     */
    fun setAppLanguage(activity: Activity, language: String?, country: String?) {
        if (language.isNullOrEmpty()) {
            setAppLanguage(activity, getSystemLocale())
        } else {
            if (country.isNullOrEmpty()) {
                setAppLanguage(activity, Locale(language, ""))
            } else {
                setAppLanguage(activity, Locale(language, country))
            }
        }
    }

    fun setAppLanguage(activity: Activity, language: String?) {
        if (language.isNullOrEmpty()) {
            setAppLanguage(activity, getSystemLocale())
        } else {
            setAppLanguage(activity, Locale(language, ""))
        }
    }

    fun setAppLanguage(activity: Activity, locale: Locale) {
        setAppLanguageReal(activity, locale)
        setAppLanguageReal(activity.applicationContext, locale)
    }

    private fun setAppLanguageReal(context: Context, locale: Locale) {
        val resources = context.resources
        val metrics = resources.displayMetrics
        val configuration = resources.configuration
        //Android 7.0以上的方法
        if (Build.VERSION.SDK_INT >= 24) {
            configuration.setLocale(locale)
            configuration.setLocales(LocaleList(locale))
            context.createConfigurationContext(configuration)
            //实测，updateConfiguration这个方法虽然很多博主说是版本不适用
            //但是我的生产环境androidX+Android Q环境下，必须加上这一句，才可以通过重启App来切换语言
            resources.updateConfiguration(configuration, metrics)
            context.createConfigurationContext(configuration)
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            //Android 4.1 以上方法
            configuration.setLocale(locale)
            resources.updateConfiguration(configuration, metrics)
        } else {
            configuration.locale = locale
            configuration.setLocale(locale)
            resources.updateConfiguration(configuration, metrics)
        }

        Log.d("mulLanguage", "setAppLanguageReal = ${locale.language}")

        MMKV.defaultMMKV()!!.putString(SP_LANGUAGE, locale.language)
        MMKV.defaultMMKV()!!.putString(SP_COUNTRY, locale.country)
    }
}