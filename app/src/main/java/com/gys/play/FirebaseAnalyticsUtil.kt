package com.gys.play

import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics

const val COMPREHENSIVE = "comprehensive"

private const val TAG = "FirebaseAnalyticsUtil"
private const val ICON = "icon"
private const val HOME_TAB = "Home_tab"
private const val RANKING = "ranking"
private const val SHELFBOOKSHELF = "shelfbookshelf"
private const val CLASSIFY = "classify"
private const val FIRST = "first"

const val CONTENT_PAGE = "content_page"

private const val CATALOGUE = "catalogue"
private const val DETAILS = "details"
private const val PURCHASE = "purchase"

const val RECHARGE = "recharge"

private const val BOOK_COIN = "book_coin"
private const val VIP = "vip"
private const val ERROR = "error"

const val USER_CENTER = "user_center"

private const val LOG_ON = "log_on"
private const val MINE = "mine"


class FirebaseAnalyticsUtil(val firebaseAnalytics: FirebaseAnalytics) {
    /**
     *我的评论没有
     */
    fun setMine(string: String?) {
        string?.let {
            val bundle = Bundle()
            bundle.putString(MINE, string)
            logEvent(USER_CENTER, bundle)
        }
    }

    fun setLogOn(string: String?) {
        string?.let {
            val bundle = Bundle()
            bundle.putString(LOG_ON, string)
            logEvent(USER_CENTER, bundle)
        }
    }

    fun setVip(string: String?) {
        string?.let {
            val bundle = Bundle()
            bundle.putString(VIP, string)
            logEvent(RECHARGE, bundle)
        }
    }

    fun setError(string: String?) {
        string?.let {
            val bundle = Bundle()
            bundle.putString(ERROR, string)
            logEvent(RECHARGE, bundle)
        }
    }

    fun setBookCoin(string: String?) {
        string?.let {
            val bundle = Bundle()
            bundle.putString(BOOK_COIN, string)
            logEvent(RECHARGE, bundle)
        }
    }

    fun setPurchase(string: String?) {
        string?.let {
            val bundle = Bundle()
            bundle.putString(PURCHASE, string)
            logEvent(CONTENT_PAGE, bundle)
        }
    }

    fun setDetails(string: String?) {
        string?.let {
            val bundle = Bundle()
            bundle.putString(DETAILS, string)
            logEvent(CONTENT_PAGE, bundle)
        }
    }

    fun setCatalogue(string: String?) {
        string?.let {
            val bundle = Bundle()
            bundle.putString(CATALOGUE, string)
            logEvent(CONTENT_PAGE, bundle)
        }
    }

    fun setFirst(string: String?) {
        string?.let {
            val bundle = Bundle()
            bundle.putString(FIRST, string)
            logEvent(SHELFBOOKSHELF, bundle)
        }
    }

    fun setClassify(string: String?) {
        string?.let {
            val bundle = Bundle()
            bundle.putString(CLASSIFY, string)
            logEvent(SHELFBOOKSHELF, bundle)
        }
    }

    fun setShelfbookshelf(string: String?) {
        string?.let {
            val bundle = Bundle()
            bundle.putString(RANKING, string)
            logEvent(SHELFBOOKSHELF, bundle)
        }
    }

    fun setRanking(string: String?) {
        string?.let {
            val bundle = Bundle()
            bundle.putString(RANKING, string)
            logEvent(COMPREHENSIVE, bundle)
        }
    }

    fun setHomeTab(string: String?) {
        string?.let {
            val bundle = Bundle()
            bundle.putString(HOME_TAB, string)
            logEvent(COMPREHENSIVE, bundle)
        }
    }

    fun setIcon(string: String?) {
        string?.let {
            val bundle = Bundle()
            bundle.putString(ICON, string)
            logEvent(COMPREHENSIVE, bundle)
        }
    }

    fun setHomeTop(string: String?) {
        string?.let {
            val bundle = Bundle()
            bundle.putString("top", string)
            logEvent("home", bundle)
        }
    }


    fun logEvent(string: String, params: Bundle) {
//        UIHelper.showLog(TAG, "logEvent: $String $params")
        firebaseAnalytics.logEvent(string, params)
    }
}