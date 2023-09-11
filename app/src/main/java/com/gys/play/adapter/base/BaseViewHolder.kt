package com.gys.play.adapter.base

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

open class BaseViewHolder(val dataBinding: ViewDataBinding) :
    RecyclerView.ViewHolder(dataBinding.root)