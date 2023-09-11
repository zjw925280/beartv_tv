package com.gys.play.ui

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.liba.network.protocol.BaseListInfo
import com.gys.play.BR
import com.gys.play.R
import com.gys.play.adapter.base.Adapter
import com.gys.play.databinding.ActivityVipOpenRecordBinding
import com.gys.play.entity.VipOpenRecordInfo
import com.gys.play.viewmodel.VipOpenRecordViewModel
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener

class VipOpenRecordActivity :
    NovelBaseActivity<VipOpenRecordViewModel<VipOpenRecordActivity>, ActivityVipOpenRecordBinding>(R.layout.activity_vip_open_record) {
    var page = 1
    var vipOpenRecordList:ArrayList<VipOpenRecordInfo> = arrayListOf()
    var vipOpenRecordAdapter = Adapter(BR.item,R.layout.item_vip_open_record,vipOpenRecordList)
    override fun initView() {
        getListData()
        setTitleText(getString(R.string.vip_open_record))
        var linearLayoutManager = LinearLayoutManager(activity)
        linearLayoutManager.orientation= LinearLayoutManager.VERTICAL
        binding.rvVipOpenRecord.layoutManager = linearLayoutManager
        binding.rvVipOpenRecord.adapter = vipOpenRecordAdapter
        binding.sRefreshLayout.setOnRefreshLoadMoreListener(object : OnRefreshLoadMoreListener {
            override fun onRefresh(refreshLayout: RefreshLayout) {
                page = 1
                vipOpenRecordAdapter?.clear()
                vipOpenRecordList.clear()
                getListData()
            }

            override fun onLoadMore(refreshLayout: RefreshLayout) {
                page++
                getListData()
            }
        })
    }

    fun getListData(){
        presenter.getPayVipLog(
            page,
            20
        ){
            _,data:BaseListInfo<VipOpenRecordInfo> ->
            if(data!=null){
                if(data.items!=null)
                vipOpenRecordAdapter?.addDatas(data.items,data.items.size)
            }
            binding.sRefreshLayout.finishRefresh()
            binding.sRefreshLayout.finishLoadMore()
            if(data!=null){
                if(data.items!=null)
                if (data.items.isEmpty()) {
                    binding.sRefreshLayout.finishLoadMoreWithNoMoreData()
                    if(page == 1)
                    binding.emptyLayout.visibility = View.VISIBLE
                } else { binding.emptyLayout.visibility = View.GONE}
            }
        }
    }

    override fun initData() {

    }

    override fun loadData(savedInstanceState: Bundle?) {

    }

    override fun setListener() {

    }

}