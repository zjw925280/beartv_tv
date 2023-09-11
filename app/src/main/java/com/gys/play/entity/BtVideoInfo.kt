package com.gys.play.entity

import android.content.Intent
import android.graphics.Color
import android.view.View
import androidx.fragment.app.FragmentActivity
import com.android.liba.ui.base.BaseFragment
import com.blankj.utilcode.util.DebouncingUtils
import com.gys.play.Config
import com.gys.play.MyApplication
import com.gys.play.R
import com.gys.play.coroutines.Quest
import com.gys.play.ui.LoginActivity
import com.gys.play.ui.video.VideoListActivity
import com.gys.play.util.JumpUtils
import com.gys.play.util.dialog.OpenBoxDialog
import com.mybase.libb.ext.getLifeActivity
import com.mybase.libb.ui.rv.IBaseItem
import post

data class BtVideoInfo(
    var id: Int = 0,
    var book_id: Int = 0,
    var datatype: Int = 0, //数据来源类型  多布局
    var word: Int = 0,
    var finish: Int = 0,
    var is_vip: Int = 0,
    var chapter_id: Int = 0,
    var had_buy: Int = 0,
    var total_views: Long = 0,
    var total_favors: Long = 0,
    var title: String = "",
    var cover: String = "",
    var category_num: String = "",
    var description: String = "",
    var category_name: String = "",
    var book_name: String = "",
    var author: String = "",
    var score: Double = 0.0,
    var thumb: String = "",
    var word_txt: String = "",
    var must_read: String = "",
    var read_reason: String = "",
    var status_txt: String = "",
    var tag: String = "",
    var chapter_title: String = "",
    var finish_txt: String = "",
    var is_bookshelf: Int = 0,
    var position_id: Int = 0,
    var cid: Int = 0,
    var new_flag: Int = 0,
    var channel_id: Int = 0,
    var free_id: Int = 0,
    var free_status: Int = -1, //0-已结束 1-限免中 2-即将开始 3-到日期限免
    var remaining_time: Int = 0,
    var bg_image: String = "",
    var category_id: Int = 0,
    var channel_type: Int = 2,
    var intro: String? = "",
    var jump_url: String? = "",
    var jump_type: Int = 0,
    var pay_type: Int = 0,//付费类型 1:金币 2:VIP
    var rank_list: NovelRankList? = null,//用排行榜样式用的
) : IBaseItem {
    var is_more: Int = 0
    var icon: String = ""
    var list: MutableList<BtVideoInfo>? = null
    var category_title: String = ""
    var isSelect = false
    var position = 0
    var listener: Listener? = null;
    var shortTvType = 4 // 4-播放量 5-评分
    var moreListener: MoreListener? = null //更多的回调
    var page = 1

    fun getScores(): String {
        return "${score}分"
    }

    fun jump(activity: FragmentActivity) {
        if (jump_type == Config.JUMP_GIFT_DIALOG) {
            MyApplication.getAnalytics().setHomeTab("礼包点击量")
            if (!SpUserInfo.isLogin()) {
                activity.let {
                    activity.startActivity(Intent(it, LoginActivity::class.java))
                }
                return
            }
            showFirstGift(activity)
            return
        }
        JumpUtils.jump(activity, jump_type, position_id, book_id, title, jump_url)
    }

    private fun showFirstGift(activity: FragmentActivity) {
        activity.post {
            val data = Quest.api.getUserGiftStatusSSS(Quest.getHead(Config.APP_USER_GETGIFT))
            OpenBoxDialog.show(activity, data.is_get, data.coins) {
                activity.post {
                    val info = Quest.api.userGetGiftSSS(Quest.getHead(Config.APP_USER_GETGIFT))
                    BaseFragment.showToast(info.desc)
                    MyApplication.getPay().updateUserInfo()
                }
            }
        }
    }

    fun getIndexDrawableId(): Int? {
        return when (position) {
            0 -> R.mipmap.icon_one
            1 -> R.mipmap.icon_two
            2 -> R.mipmap.icon_three
            else -> null
        }
    }

    fun getIndexTextColor(): Int {
        return when (position) {
            0 -> Color.parseColor("#FFA706")
            1 -> Color.parseColor("#9DBFDA")
            2 -> Color.parseColor("#E2BD83")
            else -> Color.parseColor("#9A9A9A")
        }
    }

    fun getIndexStr(): String {
        return (position + 1).toString()
    }

    fun getIndexBackColor(): Int {
        return when (position) {
            0 -> Color.parseColor("#FFA706")
            1 -> Color.parseColor("#9DBFDA")
            2 -> Color.parseColor("#E2BD83")
            else -> Color.parseColor("#7A94A1")
        }
    }

    fun goFrequencyDivision(view: View) {
    }

    fun goBookDetail(view: View?) {
        view?.context?.getLifeActivity()?.let {
            if (DebouncingUtils.isValid(view)) {
                VideoListActivity.launch(it, if (id > 0) id else cid)
            }
        }

        /*  if (position_id != 0) {
              MyApplication.getInstance().bookshelfPresent.appHomeClick(position_id) {}
          }*/

    }

    fun goReadActivity(view: View) {
//        val intent = Intent(view.context, ReadActivity::class.java)
//        intent.putExtra(Config.IS_BOOKSHELF, true)
//        intent.putExtra(Config.NOVEL_ID, book_id)
//        intent.putExtra(Config.CHAPTER_ID, chapter_id)
//        view.context.startActivity(intent)
    }

    fun isVip(): Boolean {
        return pay_type == 2
    }

    fun isBookShelf(): Boolean {
        return is_bookshelf == 1
    }

    fun isShowNewBook(): Boolean {
        return new_flag == 1
    }

    fun freeLimt(): String {
        if (free_status == -1) {
            return category_name
        } else {
            return status_txt
        }
    }

    fun getTotalViewsStr(): CharSequence {
        if (total_views > 10000) {
            return "${(total_views / 10000)}萬"
        }
        return "${total_views}"
    }

    interface Listener {
        fun select(type: Int)
    }
}

