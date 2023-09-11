package com.gys.play.adapter

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.view.View
import androidx.databinding.ViewDataBinding
import com.android.liba.util.UIHelper
import com.gys.play.R
import com.gys.play.entity.SpUserInfo
import com.gys.play.adapter.base.Adapter
import com.gys.play.databinding.ItemFreeLimitListBinding
import com.gys.play.entity.BtVideoInfo
import com.gys.play.ui.LoginActivity
import com.gys.play.BR

class FreeLimitAdapter(val context: Context) :
    Adapter<BtVideoInfo>(BR.item, R.layout.item_free_limit_list) {
    var bookShelfListener: BookShelfListener? = null
    override fun convert(binding: ViewDataBinding, item: BtVideoInfo, position: Int) {
        super.convert(binding, item, position)
        val binding = binding as ItemFreeLimitListBinding
        if (context.resources.getString(R.string.lang_type).equals("3")) {
            binding.name.setLines(1)
        } else {
            binding.name.setLines(2)
        }
        if (item.free_status != -1) {
            when (item.free_status) {
                0 -> binding.tvCategory.setTextColor(Color.parseColor("#cecece"))
                1, 2 -> binding.tvCategory.setTextColor(Color.parseColor("#353535"))
                3 -> binding.tvCategory.setTextColor(Color.parseColor("#9f9f9f"))
            }
        }
        binding.readAndAddTo.isEnabled = !item.isBookShelf()
        binding.readAndAddTo.setOnClickListener {
            if (!SpUserInfo.isLogin()) {
                context?.let {
                    it.startActivity(Intent(it, LoginActivity::class.java))
                }
                return@setOnClickListener
            }
            bookShelfListener?.run {
                UIHelper.showLog("item.isBookShelf() " + item.isBookShelf())
                if (item.isBookShelf()) return@run
                item.is_bookshelf = 1
                binding.readAndAddTo.isEnabled = !item.isBookShelf()
                notifyItemChanged(position)
                this.addBookShyelf(binding.readAndAddTo, item)
            }
        }
    }


    interface BookShelfListener {
        fun addBookShyelf(view: View, novelInfo: BtVideoInfo)
    }
}