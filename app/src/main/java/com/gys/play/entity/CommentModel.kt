package com.gys.play.entity

import android.graphics.Color
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.gys.play.util.Timestamp

class CommentModel(
    val id: Int = -1,
    var user_id: Int = 0,
    var avatar: String = "https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fitem%2F201901%2F17%2F20190117092809_ffwKZ.thumb.700_0.jpeg&refer=http%3A%2F%2Fb-ssl.duitang.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1631861867&t=23dc989b4449b3a826efb6f50abcf25c",
    var user_name: String = "xxxxxxx",
    var status: Int = 1, //状态 状态：0-待审核 1-审核通过 2-删除
    var is_audit_avatar: Int = 1, //头像审核状态 0-未审核 1-已审核通过 2-未通过
    var agree: Int = 0, //点赞数
    var content: String = "", //评论内容
    var created: Long = 0,//时间戳
    var vip_expires_time: Long = 0, //会员过期时间戳
    var is_agree: Int = 0,
    var reply_num: Int = 0, //回复数量
    var reply: List<Reply>
) {

    fun getVipTime(): Boolean {
//        val viptime = Timestamp.transToString(vip_expires_time * 1000)
//        val df = SimpleDateFormat("yyyy-MM-dd")
//        df.setTimeZone(TimeZone.getTimeZone("GMT+08"))
//        val ed: Date = df.parse(viptime)
        return vip_expires_time.toInt() != 0
    }

    fun getTime(): String {
        val t = System.currentTimeMillis() / 1000
        if (t < created) {
            return Timestamp.transToString(created * 1000)
        }
        //秒
        val sec = t - created
        //分钟
        val min = sec / 60
        if (min < 1) {
            return "刚刚"
        }
        if (min < 60) {
            return "${min}分钟前"
        }
        //小时
        val h = min / 60
        if (h < 24) {
            return "${h}小时前"
        }
        //天
        val tt = h / 24
        if (tt < 8) {
            return "${tt}天前"
        }

        return Timestamp.transToString(created * 1000)
    }

    fun inReview() = status == 0

}

@BindingAdapter("vipText")
fun vipText(textView: TextView, item: CommentModel) {
    if (item.getVipTime()) {
        textView.setTextColor(Color.parseColor("#ffff6352"))
    } else {
        textView.setTextColor(Color.parseColor("#222222"))
    }
}

@BindingAdapter("vipIcon")
fun vipIcon(imageView: ImageView, item: CommentModel) {
    if (item.getVipTime()) {
        imageView.visibility = View.VISIBLE
    } else {
        imageView.visibility = View.GONE
    }
}