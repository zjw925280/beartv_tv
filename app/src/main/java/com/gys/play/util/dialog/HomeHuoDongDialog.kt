package com.gys.play.util.dialog

import android.content.Context
import android.view.View
import android.widget.ImageView
import com.android.liba.context.AppContext
import com.android.liba.util.UIHelper
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.gys.play.R
import com.gys.play.entity.HomeBannerInfo
import java.text.SimpleDateFormat
import java.util.*

class HomeHuoDongDialog(
    context: Context?,
    list: MutableList<HomeBannerInfo>,
    listener: OnHuoDongListener?,
) :
    HomeAdsBaseCenterDialog(context) {

    var list: MutableList<HomeBannerInfo>? = null
    var listener: OnHuoDongListener? = null
    var isFirstHuoDong = true

    init {
        this.list = list
        this.listener = listener
    }

    override fun getContentView(): View {
        val data = list!![0]
        UIHelper.showLog("活动弹窗 thumb = " + data.thumb)
        val view = layoutInflater.inflate(R.layout.dialog_home_huodong, null)
        val imgHuoDong = view.findViewById<ImageView>(R.id.img_huodong)
        val imgHuoDongClose = view.findViewById<ImageView>(R.id.img_huodong_close)
        val imgHuoDongClose2 = view.findViewById<ImageView>(R.id.img_huodong_close2)

        Glide.with(context)
            .load(data.thumb)
            .skipMemoryCache(false)
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .into(imgHuoDong)
        imgHuoDongClose.setOnClickListener {
            if (list!!.size >= 2) {
                isFirstHuoDong = false
                imgHuoDongClose.visibility = View.GONE
                imgHuoDongClose2.visibility = View.VISIBLE
                Glide.with(imgHuoDong.context)
                    .load(list!![1].thumb)
                    .skipMemoryCache(false)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .into(imgHuoDong)
            } else {
                dismiss()
            }
        }
        imgHuoDongClose2.setOnClickListener {
            dismiss()
        }
        imgHuoDong.setOnClickListener {
            data.jump(context)
        }
        return view
    }

    companion object {
        private const val TODAY_KEY = "today_key"
        fun show(
            context: Context?,
            list: MutableList<HomeBannerInfo>,
            listener: OnHuoDongListener? = null,
        ): HomeHuoDongDialog {
            var dialog = HomeHuoDongDialog(context, list, listener)
            dialog!!.setCancelable(false)
            dialog.show()
            return dialog!!
        }

        fun isNotToday(): Boolean {
            val lastTime = AppContext.getMMKV().decodeString(TODAY_KEY, "2017-04-08")
            val df = SimpleDateFormat("yyyy-MM-dd")
            val todayTime = df.format(Date())

            if (lastTime.equals(todayTime)) { //如果两个时间段相等
                return false
            } else {
                AppContext.getMMKV().encode(TODAY_KEY, todayTime)
            }

            return true
        }
    }
}
