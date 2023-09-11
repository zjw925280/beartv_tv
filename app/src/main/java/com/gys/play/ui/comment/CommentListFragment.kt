package com.gys.play.ui.comment

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.drake.brv.BindingAdapter
import com.drake.brv.utils.models
import com.gys.play.R
import com.gys.play.coroutines.Quest
import com.gys.play.databinding.FragmentCommentListBinding
import com.gys.play.entity.CommentInfo
import com.gys.play.entity.SpUserInfo
import com.gys.play.ui.LoginActivity
import com.gys.play.ui.ReportActivity
import com.gys.play.ui.video.VideoListActivity
import com.gys.play.ui.video.VideoViewModel
import com.jccppp.dialog.JustDialog
import com.jccppp.dialog.addView
import com.jccppp.dialog.setClick
import com.jccppp.dialog.setClickDismiss
import com.jccppp.start.argument
import com.jccppp.start.set
import com.jccppp.start.setBundle
import com.mybase.libb.ext.*
import com.mybase.libb.ui.vb.BaseVbFragment
import post

class CommentListFragment : BaseVbFragment<FragmentCommentListBinding>() {

    companion object {

        fun newInstance(type: Int, resource_id: String, comment_id: String, order: String) =
            CommentListFragment().setBundle {
                it["type"] = type
                it["resource_id"] = resource_id
                it["comment_id"] = comment_id
                it["order"] = order
            }

        //我的评论
        fun newMyInstance(order: String) = CommentListFragment().setBundle {
            it["mySelf"] = true
            it["order"] = order
        }
    }

    private val type by argument(0)
    private val resource_id by argument("")
    private val comment_id by argument("")
    private var order by argument("")
    private val mySelf by argument(false)

    private var viewModel: VideoViewModel? = null

    private val editViewModel by activityViewModels<EditCommentViewModel>()

    private val mSendCommentDialog by lazy {
        SendCommentDialog(this, type) {
            initData()
        }
    }

    var onTopListener: ((Boolean) -> Unit)? = null

    private var isEdit = false

    private val checkDelete by lazy {
        JustDialog(mActivity).addView(R.layout.dialog_check_delete).setClickDismiss(R.id.tvCancel)
    }

    override fun initView(savedInstanceState: Bundle?) {

        mBind.llComment.showGONE(!mySelf)

        if (mActivity is VideoListActivity) {
            viewModel = activityViewModels<VideoViewModel>().value
        }


        mBind.prv.setLimitSize(10)

        mBind.prv.config({

            val bean =
                if (!mySelf) Quest.api.getCommentList(Quest.getHead("App.Comment.CommentList") {
                    add("page", "$it")
                    add("order", order)
                    add("type", "$type")
                    add("resource_id", resource_id)
                    add("comment_id", comment_id)
                }) else Quest.api.myCommentList(Quest.getHead("App.Comment.MyCommentList") {
                    add("page", "$it")
                    add("order", order)
                })

            viewModel?.let {
                if (it.commentNum.value != "${bean.count}") {
                    it.commentNum.value = "${bean.count}"
                }
            }
            bean.items
        }, viewLifecycleOwner)

        mBind.prv.setEnableRefresh(false)

//        mBind.prv.prl.setEnableFooterFollowWhenNoMoreData(true)
//        mBind.prv.prl.setEnableLoadMoreWhenContentNotFull(true)

        mBind.prv.rv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    onTopListener?.invoke(!recyclerView.canScrollVertically(-1))
                }
            }
        })

        (mBind.prv.rv.adapter as? BindingAdapter)?.let {
            it.onBind {
                val item = getModel<CommentInfo>()

                itemView.setOnClickListener { v ->
                    if (isEdit) {
                        item.selected = !item.selected
                        it.notifyItemChanged(modelPosition)
                    }
                }

                findView<ImageView>(R.id.select).run {
                    showGONE(isEdit)
                    if (item.selected) {
                        setImageResource(R.mipmap.icon_select_s)
                    } else {
                        setImageResource(R.mipmap.icon_select_n)
                    }
                }

                if (item.followCommentList.notEmpty()) {
                    findView<RecyclerView>(R.id.birv).models =
                        arrayListOf(item.followCommentList[0])
                }
                val agree = findView<TextView>(R.id.agree)
                agree.isSelected = item.agree
                agree.setNoDouble {
                    post {
                        if (item.agree) {
                            Quest.api.removeAgreeComment(Quest.getHead("App.Comment.UnAgreeComment") {
                                add("comment_id", "${item.id}")
                            })
                        } else {
                            Quest.api.addAgreeComment(Quest.getHead("App.Comment.AgreeComment") {
                                add("comment_id", "${item.id}")
                            })
                        }
                        item.is_agree = if (item.agree) 0 else 1
                        it.isSelected = item.agree
                        if (it.isSelected) {
                            item.agree_count++
                        } else {
                            item.agree_count--
                        }
                        agree.text = "${item.agree_count}"
                    }
                }

                findView<View>(R.id.reply).let {
                    it.isEnabled = !isEdit
                    it.setNoDouble {
                        val rid = if (resource_id.notEmpty()) resource_id else item.resource_id

                        mSendCommentDialog.show(rid, "${item.id}", "回复:${item.user_name}")
                    }
                }

                findView<View>(R.id.allText).let {
                    it.isEnabled = !isEdit
                    it.setNoDouble {
                        val rid = if (resource_id.notEmpty()) resource_id else item.resource_id
                        val rType = if (type > 0) type else item.type
                        CommentWowDialog.newInstance(rType, rid, "${item.id}")
                            .show(childFragmentManager)
                    }
                }
            findView<View>(R.id.img_jubao).setOnClickListener{
                if (!SpUserInfo.isLogin()) {
                    startActivity(Intent(getBActivity(), LoginActivity::class.java))
                    return@setOnClickListener
                }
                getContext()?.let { it1 -> ReportActivity.start(it1) }
            }
            }
        }


        mBind.tvInput.setNoDouble {
            mSendCommentDialog.show(resource_id, comment_id, "發表評論，分享我的看法")
        }
    }

    override fun observeUi(owner: LifecycleOwner) {
        super.observeUi(owner)

        isEdit = editViewModel.editTxt.value == "取消"

        editViewModel.editSwitch.observe(owner) {
            if (!mBind.prv.rv.models.isNullOrEmpty()) {
                isEdit = it
                mBind.prv.rv.adapter?.notifyDataSetChanged()
            }
            editViewModel.editTxt.value = if (isEdit) "取消" else "编辑"
        }

        editViewModel.allSelected.observe(owner) {
            mBind.prv.rv.models?.forEach {
                if (it is CommentInfo) {
                    it.selected = true
                }
            }
            mBind.prv.rv.adapter?.notifyDataSetChanged()
        }

        editViewModel.changeOrderType.observe(owner) {
            order = if (it == 0) "1" else "0"
            initData()
        }

        editViewModel.startDelete.observe(owner) {
            var ids = ""
            mBind.prv.rv.models?.forEach {
                if (it is CommentInfo && it.selected) {
                    ids += "${it.id},"
                }
            }
            if (ids.isBlank()) {
                showToast("請選擇要删除的評論!")
                return@observe
            }

            ids = ids.substring(0, ids.length - 1)

            checkDelete.setClick(R.id.tvSubmit, dismiss = true) {
                post(showLoading = true) {
                    Quest.api.deleteComment(Quest.getHead("App.Comment.BatchDelComment") {
                        add("comment_ids", ids)
                    })
                    initData()
                }
            }

            if (!checkDelete.isShowing) {
                checkDelete.show()
            }

        }


    }

    override fun initData(tag: Any?) {
        super.initData(tag)
        mBind.prv.onRestart()
    }


}