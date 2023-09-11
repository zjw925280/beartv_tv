package com.gys.play.fragment.open_record

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.liba.network.protocol.BaseListInfo
import com.android.liba.ui.base.BindingFragment
import com.gys.play.BR
import com.gys.play.R
import com.gys.play.adapter.base.Adapter
import com.gys.play.databinding.FragmentConsumeListBinding
import com.gys.play.entity.ConsumeRecordInfo
import com.gys.play.util.DateFormatUtils
import com.gys.play.util.dialog.BottomTimeDialog
import com.gys.play.viewmodel.ConsumeViewModel
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener

class ConsumeListFragment :
    BindingFragment<ConsumeViewModel, FragmentConsumeListBinding>() {

    var page = 1
    private var mDatePicker: com.gys.play.util.CustomDatePicker? = null
    var consumeRecordList: ArrayList<ConsumeRecordInfo> = arrayListOf()
    var consumeRecordAdapter: Adapter<ConsumeRecordInfo> =
        Adapter(BR.item, R.layout.item_consume_record)

    override fun initData() {
        getListData()
        initDatePicker()
        binding.clTimeSel.setOnClickListener {
            BottomTimeDialog(it.context, binding.tvTime.text.toString()) {
                binding.tvTime.text = it
            }.show()
        }
        var linearLayoutManager = LinearLayoutManager(activity)
        linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
        binding.rv.layoutManager = linearLayoutManager
        binding.rv.adapter = consumeRecordAdapter
        binding.sRefreshLayout.setOnRefreshLoadMoreListener(object : OnRefreshLoadMoreListener {
            override fun onRefresh(refreshLayout: RefreshLayout) {
                page = 1
                consumeRecordAdapter?.clear()
                consumeRecordList.clear()
                getListData()
            }

            override fun onLoadMore(refreshLayout: RefreshLayout) {
                page++
                getListData()
            }
        })
    }

    private fun initDatePicker() {
        val beginTimestamp: Long = com.gys.play.util.DateFormatUtils.str2Long("1960-01-01", false)
        val endTimestamp = System.currentTimeMillis()

        // 通过时间戳初始化日期，毫秒级别
        mDatePicker = com.gys.play.util.CustomDatePicker(
            this.activity,
            {
                binding.tvTime.text = com.gys.play.util.DateFormatUtils.long2Str(it,"yyyy-MM")
                page = 1
                consumeRecordAdapter?.clear()
                consumeRecordList.clear()
                getListData()
            }, beginTimestamp, endTimestamp
        )
        // 不允许点击屏幕或物理返回键关闭
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

    fun getListData() {
        presenter.getUserConsumeLog(
            binding.tvTime.text.toString(),
            page,
            20,
        ) { _, data: BaseListInfo<ConsumeRecordInfo> ->
            if (data != null) {
                if (data.items != null)
                    consumeRecordAdapter?.addDatas(data.items, data.items.size)
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

    override fun getLayoutId(): Int = R.layout.fragment_consume_list

    override fun initView() {
        binding.tvTime.text = DateFormatUtils.long2Str(System.currentTimeMillis(), "yyyy-MM")
    }
}