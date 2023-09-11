package com.gys.play.fragment.book_city.style

import android.view.View
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.liba.ui.base.listgroup.holder.ViewHolder
import com.android.liba.util.UIHelper
import com.gys.play.Config
import com.gys.play.R
import com.gys.play.adapter.FreeLimitHomeAdapter
import com.gys.play.adapter.NovelStyleAdapter
import com.gys.play.databinding.ItemNovelStyle14Binding
import com.gys.play.entity.BtVideoInfo
import com.gys.play.util.FreeCountDownUtil
import com.gys.play.util.ImageLoaderUtils

object BtVideoStyle14 {

    fun getType(): Int {
        return Config.HOME_NOVEL_STYLE_14
    }

    fun getItemViewDelegate(activity: FragmentActivity): NovelStyleAdapter.NewUserReadItemViewDelegate {
        return object : NovelStyleAdapter.NewUserReadItemViewDelegate(
            R.layout.item_novel_style_14,
            getType(),
            0,
            true
        ) {
            override fun convert(holder: ViewHolder?, item: BtVideoInfo, position: Int) {
                holder?.let { h ->
                    val binding: ItemNovelStyle14Binding = h.getViewDataBinding()

                    UIHelper.showLog("NovelStyle14 $item")

                    binding.title.text = item.title
                    if (item.icon.isEmpty()) {
                        binding.icon.visibility = View.GONE
                    } else {
                        binding.icon.visibility = View.VISIBLE
                        ImageLoaderUtils.loadImage(binding.icon, item.icon)
                    }

                    val linearLayoutManager = LinearLayoutManager(activity)
                    linearLayoutManager.orientation = LinearLayoutManager.HORIZONTAL
                    binding.listRv.layoutManager = linearLayoutManager
                    val list = ArrayList<BtVideoInfo>()
                    item.list?.let {
                        it.forEach { d ->
                            if (d.free_status != 0) {
                                //未结束的才添加显示
                                list.add(d)
                            }
                        }
                        if (item.remaining_time > 0) {
                            binding.timeLayout.visibility = View.VISIBLE
                            FreeCountDownUtil.cancel()

                            FreeCountDownUtil.start(item.remaining_time)
                            FreeCountDownUtil.countDownTimer.observe(activity) { time ->
                                if (time > 0) {
                                    val timeArray = stringForTime(time)
                                    binding.hourTv.text = timeArray[0]
                                    binding.minTv.text = timeArray[1]
                                    binding.secondTv.text = timeArray[2]
                                } else {
                                    FreeCountDownUtil.cancel()
                                }
                            }
                        } else {
                            binding.timeLayout.visibility = View.GONE
                        }
                    }
                    val adapter = FreeLimitHomeAdapter(list, activity)
                    binding.listRv.adapter = adapter
                    binding.listRv.isNestedScrollingEnabled = false
                }
            }
        }
    }


    private fun stringForTime(totalSeconds: Long): List<String> {
        val seconds = totalSeconds % 60
        val minutes = totalSeconds / 60 % 60
        val hours = totalSeconds / 3600

        val time1 = if (hours < 10) "0$hours" else "$hours"
        val time2 = if (minutes < 10) "0$minutes" else "$minutes"
        val time3 = if (seconds < 10) "0$seconds" else "$seconds"

        return arrayListOf(time1, time2, time3)
    }
}