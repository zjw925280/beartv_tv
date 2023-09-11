package com.gys.play.util.dialog

import android.content.Context
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.liba.context.AppContext
import com.android.liba.ui.dialog.BaseBottomDialog
import com.gys.play.Config
import com.gys.play.R
import com.gys.play.adapter.RewardAdapter
import com.gys.play.entity.RewardListInfo
import com.gys.play.entity.SpUserInfo

class RewardDialog(
    context: Context?,
    list: ArrayList<RewardListInfo>,
    listener: OnRewardListener
) :
    BaseBottomDialog(context) {

    var list = ArrayList<RewardListInfo>()
    var listener: OnRewardListener? = null
    var giftId = 0
    var coins = 0
    var position = 0

    init {
        this.list = list
        this.listener = listener
    }

    override fun getContentView(): View {
        var view = layoutInflater.inflate(R.layout.dialog_reward, null)
        var tvRewardCoins = view.findViewById<TextView>(R.id.tv_rewarc_coins)
        var tvRewardRecharge = view.findViewById<TextView>(R.id.tv_reward_recharge)
        var rvReward = view.findViewById<RecyclerView>(R.id.rv_reward)
        var etReward = view.findViewById<EditText>(R.id.et_reward)
        var tvReward = view.findViewById<TextView>(R.id.tv_reward)

        tvRewardCoins.text = SpUserInfo.getUserInfo().coins
        var gridLayoutManager = GridLayoutManager(context, 4)
        rvReward.layoutManager = gridLayoutManager
        var adapter = RewardAdapter(context, list)
        adapter.setList(list)
        rvReward.adapter = adapter

        adapter.listener = ::adapterListener
        tvRewardRecharge.setOnClickListener {
            Config.toRecharge(context)
            dismiss()
        }
        tvReward.setOnClickListener {
            if (coins > tvRewardCoins.text.toString().toInt()) {
                AppContext.showToast(context.getString(R.string.coins_not_enough))
                Config.toRecharge(context)
            } else {
                listener?.let {
                    it.onReward(etReward.text.toString(), giftId, position)
                }
            }
            dismiss()
        }
        return view
    }

    fun adapterListener(id: Int, c: Int, pos: Int) {
        giftId = id
        coins = c
        position = pos
    }

    companion object {
        fun show(
            context: Context?,
            list: ArrayList<RewardListInfo>,
            listener: OnRewardListener
        ): RewardDialog {
            var dialog = RewardDialog(context, list, listener)
            dialog.show()
            return dialog
        }
    }
}