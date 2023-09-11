package com.gys.play.entity

import com.gys.play.MyApplication
import com.gys.play.R

data class WelfareInfo(
    val id: Int,
    val title_tw: String?,
    val title_en: String?,
    val title: String?,
    val jump_type: Int,
    val coins: Int,
    val task_type: Int,
    val task_status: Int,
    val task_identify: Int,
    val is_show_progress: Int,
    val unit_name: String,
    val over_times: Int,
    val total_times: Int,

    val times: Int,
    val unit_num: Int,
    val read_time: Int,
    val speed: Int,
    val over_time: Int
) {

    fun taskStatus(): String {
        var s = ""
        val resource = MyApplication.getInstance().getActivityResources()
        if (task_type == 3) {
            when (task_status) {
                0 -> s = resource.getString(R.string.no_right)
                1 -> s = resource.getString(R.string.get_it)
                2 -> s = resource.getString(R.string.reward_done)
            }
        } else {
            when (task_status) {
                0 -> s = resource.getString(R.string.to_do)
                1 -> s = resource.getString(R.string.get_it)
                2 -> s = resource.getString(R.string.task_done)
            }
        }
        return s
    }

    fun getTitleStr(): String? {
        if (!title.isNullOrEmpty()) {
            return title
        }
        val lang =
            MyApplication.getInstance().getActivityResources().getString(R.string.lang_type)
        return if (lang == "2") {
            title_en
        } else {
            title_tw
        }
    }

    fun getProgressVisible(): Boolean {
        return is_show_progress == 1 || times > 1 || task_identify == 8 || task_identify == 9 || task_identify == 11
    }


    fun getProgressText(): String {
        if (total_times > 0) {
            return "$over_times/$total_times$unit_name"
        }
        return when (task_identify) {
            11, 8 -> "$over_time/${this.times}"
            9 -> "$read_time/$unit_num" + MyApplication.getInstance()
                .getActivityResources().getString(R.string.read_time)
            else -> "$speed/${this.times}"
        }
    }
}