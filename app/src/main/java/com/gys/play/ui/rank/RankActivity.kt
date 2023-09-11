package com.gys.play.ui.rank

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import com.gys.play.MyApplication
import com.gys.play.R
import com.gys.play.databinding.ActivityRankBinding
import com.gys.play.entity.RankItemInfo
import com.gys.play.fragment.RankListFragment
import com.gys.play.fragment.home.PagerAdapter
import com.gys.play.ui.NovelBaseActivity
import com.gys.play.viewmodel.NovelBasePresent

val CHANNEL_TYPE = "CHANNEL_TYPE"
val SORT_TYPE = "SORT_TYPE"

const val JUMP_RANK_ALL = 0
const val JUMP_RANK_BOYS = 1
const val JUMP_RANK_GIRLS = 2

//排序类型：1-必读榜 2-新剧榜 3-完结榜
const val itemType1 = 1
const val itemType2 = 2
const val itemType3 = 3

class RankActivity :
    NovelBaseActivity<NovelBasePresent<RankActivity>, ActivityRankBinding>(R.layout.activity_rank) {

    //    var tvTabList: MutableList<TextView> = mutableListOf()
    var selTabIndex = 0
    var sortType = 0
    val itemList = mutableListOf<RankItemInfo>()

    companion object {
//        var selTab = MutableLiveData<Int>()

        /**
         * @param channelType 频道 男频 女频
         * @param position 类型  必读榜 人气榜 收藏榜
         */
        fun start(context: Context) {
            val intent = Intent(context, RankActivity::class.java)
            context?.startActivity(intent)
        }
    }

    override fun initView() {

        addItem("必看榜", itemType1)
        addItem("新劇榜", itemType2)
        addItem("完結榜", itemType3)
        binding.viewPager.offscreenPageLimit = itemList.size
        binding.viewPager.isUserInputEnabled = true
        val fragment = mutableListOf<Fragment>()
        for (data in itemList) {
            fragment.add(RankListFragment.newInstance(data.sort_type))
        }
        binding.viewPager.adapter = PagerAdapter(this, fragment)
        binding.viewPager.isUserInputEnabled = false
    }

    private fun addItem(strId: String, type: Int) {
        val layout =
            LayoutInflater.from(this).inflate(R.layout.item_rank_activity_item, null, false)
        layout.findViewById<TextView>(R.id.text).text = strId
        val rankItemInfo = RankItemInfo(type, layout)
        binding.rankLeftLayout.addView(layout)
        itemList.add(rankItemInfo)

        layout.setOnClickListener {
            for (index in 0 until itemList.size) {
                val d = itemList[index]
                if (d.view == it) {
                    selView(index)
                    binding.viewPager.currentItem = index
                } else {
                    unSelView(index)
                }
            }
        }
    }

    override fun initData() {
        selView(sortType)
        binding.viewPager.currentItem = sortType
        selTabView(selTabIndex)
    }

    override fun loadData(savedInstanceState: Bundle?) {
    }

    private fun selView(index: Int) {
        itemList[index].run {
            view.isSelected = true
        }
    }

    private fun unSelView(index: Int) {
        itemList[index].view.isSelected = false
    }

    private fun selTabView(index: Int) {
//        tvTabList[index].isSelected = true
//        selTab.value = index
    }

//    private fun unSelTabView(index: Int) {
//        tvTabList[index].isSelected = false
//    }

    override fun setListener() {
        binding.rlBack.setOnClickListener {
            finish()
        }
    }
}