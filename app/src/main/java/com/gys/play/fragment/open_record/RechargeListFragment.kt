package com.gys.play.fragment.open_record

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.liba.network.protocol.BaseListInfo
import com.android.liba.ui.base.BindingFragment
import com.gys.play.BR
import com.gys.play.R
import com.gys.play.adapter.base.Adapter
import com.gys.play.databinding.FragmentRechargeListBinding
import com.gys.play.entity.RechargeRecordInfo
import com.gys.play.util.DateFormatUtils
import com.gys.play.util.dialog.BottomTimeDialog
import com.gys.play.viewmodel.RechargeViewModel
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener

class RechargeListFragment :
    BindingFragment<RechargeViewModel, FragmentRechargeListBinding>() {

    var page = 1
    private var mDatePicker: com.gys.play.util.CustomDatePicker? = null
    var rechargeRecordList: ArrayList<RechargeRecordInfo> = arrayListOf()
    var rechargeRecordAdapter: Adapter<RechargeRecordInfo> =
        Adapter(BR.item, R.layout.item_recharge_record)

    override fun initData() {
        getListData()
        initDatePicker()
        binding.clTimeSel.setOnClickListener {
            BottomTimeDialog(it.context, binding.tvTime.text.toString()) {
                binding.tvTime.text = it
            }.show()
//            mDatePicker?.show(binding.tvTime.text.toString() + "-01")
        }
        var linearLayoutManager = LinearLayoutManager(activity)
        linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
        binding.rv.layoutManager = linearLayoutManager
        binding.rv.adapter = rechargeRecordAdapter
        binding.sRefreshLayout.setOnRefreshLoadMoreListener(object : OnRefreshLoadMoreListener {
            override fun onRefresh(refreshLayout: RefreshLayout) {
                page = 1
                rechargeRecordAdapter?.clear()
                rechargeRecordList.clear()
                getListData()
            }

            override fun onLoadMore(refreshLayout: RefreshLayout) {
                page++
                getListData()
            }
        })
    }

    fun getListData() {
        presenter.getUserRechargeLog(
            binding.tvTime.text.toString(),
            page,
            20,
        ) { _, data: BaseListInfo<RechargeRecordInfo> ->
            if (data != null) {
                if (data.items != null)
                    rechargeRecordAdapter?.addDatas(data.items, data.items.size)
            }
            binding.sRefreshLayout.finishRefresh()
            binding.sRefreshLayout.finishLoadMore()
            if (data != null) {
                if (data.items != null)
                    if (data.items.isEmpty()) {
                        binding.sRefreshLayout.finishLoadMoreWithNoMoreData()
                        if (page == 1)
                            binding.emptyLayout.visibility = View.VISIBLE
                    } else {
                        binding.emptyLayout.visibility = View.GONE
                    }
            }
        }
    }

    private fun initDatePicker() {
        val beginTimestamp: Long = DateFormatUtils.str2Long("1960-01-01", false)
        val endTimestamp = System.currentTimeMillis()

        // 通过时间戳初始化日期，毫秒级别
        mDatePicker = com.gys.play.util.CustomDatePicker(
            this.activity,
            {
                binding.tvTime.text = DateFormatUtils.long2Str(it, "yyyy-MM")
                page = 1
                rechargeRecordAdapter?.clear()
                rechargeRecordList.clear()
                getListData()
            }, beginTimestamp, endTimestamp
        )
        // 允许点击屏幕或物理返回键关闭
        mDatePicker!!.setCancelable(true)
        // 不显示日和月
        mDatePicker!!.setCanShowPreciseTimeOnlyYear(false)
        // 不显示时和分
        mDatePicker!!.setCanShowPreciseTime(false)
        // 不允许循环滚动
        mDatePicker!!.setScrollLoop(false)
        // 不允许滚动动画
        mDatePicker!!.setCanShowAnim(false)
    }

    override fun getLayoutId(): Int = R.layout.fragment_recharge_list

    override fun initView() {
        binding.tvTime.text = DateFormatUtils.long2Str(System.currentTimeMillis(), "yyyy-MM")

    }
}