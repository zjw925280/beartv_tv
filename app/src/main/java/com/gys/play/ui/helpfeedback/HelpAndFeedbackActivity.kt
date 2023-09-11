package com.gys.play.ui.helpfeedback

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ListPopupWindow
import android.widget.TextView
import androidx.core.view.get
import androidx.core.view.size
import com.android.liba.network.HttpUtil
import com.android.liba.network.protocol.BaseListInfo
import com.android.liba.util.AppUtil
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.config.PictureConfig
import com.luck.picture.lib.config.PictureMimeType
import com.luck.picture.lib.entity.LocalMedia
import com.gys.play.Config
import com.gys.play.MyApplication
import com.gys.play.R
import com.gys.play.SpServiceConfig
import com.gys.play.databinding.ActivityHelpAndFeedbackBinding
import com.gys.play.entity.FeedbackTypeInfo
import com.gys.play.entity.ImageUrlInfo
import com.gys.play.entity.StatusInfo
import com.gys.play.ui.NovelBaseActivity
import com.gys.play.util.HeadUtils
import com.gys.play.util.dialog.BigImageDialog
import com.gys.play.util.picture.GlideCacheEngine
import com.gys.play.util.picture.GlideEngine
import com.yalantis.ucrop.view.OverlayView
import java.io.File

class HelpAndFeedbackActivity :
    NovelBaseActivity<HelpAndFeedbackViewModel, ActivityHelpAndFeedbackBinding>(R.layout.activity_help_and_feedback) {

    var list = ArrayList<String>()
    var item: List<FeedbackTypeInfo>? = null
    var listPopupWindow: ListPopupWindow? = null
    var mapUrl = mutableMapOf<Int, String>()
    var imgList = mutableListOf<String>()
    var feedId = ""

    override fun initView() {
        listPopupWindow = ListPopupWindow(this)
        listPopupWindow?.setAdapter(
            ArrayAdapter<String>(
                this,
                R.layout.item_fankuitype,
                list
            )
        )
        listPopupWindow?.anchorView = binding.btnFankuitype
        listPopupWindow?.isModal = true
    }

    override fun initData() {
        presenter.getFeedbackType() { _: HelpAndFeedbackActivity, data: BaseListInfo<FeedbackTypeInfo> ->
            item = data.items
            for (info in data.items) {
                list.add(info.name)
            }
        }
    }

    override fun loadData(savedInstanceState: Bundle?) {
    }

    override fun setListener() {
        binding.itemTopBack.setOnClickListener {
            finish()
        }

        binding.itemTopRight.setOnClickListener {
            val serviceEmail = SpServiceConfig.getServiceEmail()
            if (serviceEmail.isNullOrEmpty()) {
                return@setOnClickListener
            }
            startActivity(
                Intent(Intent.ACTION_SENDTO)
                    .setData(Uri.parse("mailto:$serviceEmail"))
            )
        }

        binding.btnFankuitype.setOnClickListener {
            //显示类型
            showListPopupWindow()
        }

        binding.btnAddImg.setOnClickListener {
            //打开手机中的相册
            PictureSelector.create(this@HelpAndFeedbackActivity)
                .openGallery(PictureMimeType.ofImage())
                .imageEngine(GlideEngine.createGlideEngine())
                .maxSelectNum(3)
                .minSelectNum(1)
                .imageSpanCount(4)
                .isEditorImage(false)//是否编辑图片
                .isEnableCrop(false)// 是否裁剪
                .isCompress(true)// 是否压缩
//                        .queryMaxFileSize(2f)// 只查多少M以内的图片、视频、音频  单位M
                .hideBottomControls(true)// 是否显示uCrop工具栏，默认不显示
                .withAspectRatio(1, 1)// 裁剪比例 如16:9 3:2 3:4 1:1 可自定义
                .isWebp(false)
                .isCamera(false)
                .loadCacheResourcesCallback(GlideCacheEngine.createCacheEngine())// 获取图片资源缓存，主要是解决华为10部分机型在拷贝文件过多时会出现卡的问题，这里可以判断只在会出现一直转圈问题机型上使用
                .freeStyleCropEnabled(true)// 裁剪框是否可拖拽
                .freeStyleCropMode(OverlayView.DEFAULT_FREESTYLE_CROP_MODE)// 裁剪框拖动模式
//                        .isCropDragSmoothToCenter(true)// 裁剪框拖动时图片自动跟随居中
//                        .circleDimmedLayer(true)// 是否圆形裁剪
                .showCropFrame(true)// 是否显示裁剪矩形边框 圆形裁剪时建议设为false
                .showCropGrid(true)// 是否显示裁剪矩形网格 圆形裁剪时建议设为false
                .showCropFrame(true)// 是否显示裁剪矩形边框 圆形裁剪时建议设为false
                .showCropGrid(true)// 是否显示裁剪矩形网格 圆形裁剪时建议设为false
                .cutOutQuality(90)// 裁剪输出质量 默认100
                .minimumCompressSize(100)// 小于多少kb的图片不压缩
                .selectionMode(PictureConfig.SINGLE)
                .forResult(PictureConfig.CHOOSE_REQUEST)
        }
        binding.ivFankuiImg1Del.setOnClickListener {
            binding.fankuiImg1.visibility = View.GONE
            binding.btnAddImg.visibility = View.VISIBLE
            binding.ivFankuiImg1.setImageDrawable(null)
            mapUrl.remove(1)
            showImgNum(mapUrl)
        }
        binding.ivFankuiImg2Del.setOnClickListener {
            binding.fankuiImg2.visibility = View.GONE
            binding.btnAddImg.visibility = View.VISIBLE
            binding.ivFankuiImg2.setImageDrawable(null)
            mapUrl.remove(2)
            showImgNum(mapUrl)
        }
        binding.ivFankuiImg3Del.setOnClickListener {
            binding.fankuiImg3.visibility = View.GONE
            binding.btnAddImg.visibility = View.VISIBLE
            binding.ivFankuiImg3.setImageDrawable(null)
            mapUrl.remove(3)
            showImgNum(mapUrl)
        }
        binding.btnFankuicommit.setOnClickListener {
            var resource = MyApplication.getInstance().getActivityResources()
            //提交至后台
            if (binding.etFankuitype.text.toString() == "" || binding.etFankuitype.text.toString() == resource.getString(
                    R.string.choose
                )
            ) {
                showToast(resource.getString(R.string.choose_qusetion))
                return@setOnClickListener
            } else if (binding.etFankuiContext.text.toString() == "") {
                showToast(resource.getString(R.string.enter_question))
                return@setOnClickListener
            } else {
                showLoading()
                var index = 0
                if (mapUrl.isNotEmpty()) {
                    for (i in mapUrl.values) {
                        val file = File(i)
                        val body = HttpUtil.getUploadMultipartBodyPart(file, "image/jpeg")
                        presenter.uploadImage(
                            Config.APP_UPLOAD_INDEX,
                            MyApplication.getInstance().getActivityResources()
                                .getString(R.string.lang_type),
                            body,
                            com.gys.play.util.FileMD5Utils.getFileMD5(file),
                            "android",
                            "${AppUtil.getVersionCode(this)}"
                        ) { activity: HelpAndFeedbackActivity, data: ImageUrlInfo ->
                            if (data.code == 1) {
                                imgList.add(data.url)
                                index++
                                if (index >= mapUrl.size) {
                                    addFeedback()
                                }
                            } else {
                                closeLoading()
                            }
                        }
                    }
                } else {
                    addFeedback()
                }
            }
        }
        listPopupWindow?.setOnItemClickListener { parent, view, position, id ->
            for (i in 0 until parent.size) {
                parent[i].background =
                    resources.getDrawable(R.drawable.shape_rect_ffffff_0_eeeeee_line_1)
                parent[i].findViewById<TextView>(R.id.fankui_type).setTextColor(Color.BLACK)
            }
            view.findViewById<TextView>(R.id.fankui_type).setTextColor(Color.WHITE)
            view.background = resources.getDrawable(R.drawable.shape_rect_bb86ff)
            binding.etFankuitype.setText(list.get(position))
            feedId = item?.get(position)!!.id
            listPopupWindow?.dismiss()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        var images: List<LocalMedia?>
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                PictureConfig.CHOOSE_REQUEST -> {
                    // 图片选择结果回调
                    images = PictureSelector.obtainMultipleResult(data)
                    if (images.isNotEmpty()) {
                        when {
                            binding.ivFankuiImg1.drawable == null -> {
                                binding.fankuiImg1.visibility = View.VISIBLE
                                if (binding.fankuiImg1.visibility == View.VISIBLE && binding.fankuiImg2.visibility == View.VISIBLE && binding.fankuiImg3.visibility == View.VISIBLE)
                                    binding.btnAddImg.visibility = View.GONE
                                images[0]?.let {
                                    HeadUtils.roundedCornerImageUrl(
                                        binding.ivFankuiImg1,
                                        it.compressPath
                                    )
                                    binding.ivFankuiImg1.setOnClickListener {
                                        images[0]?.let { it1 ->
                                            BigImageDialog.show(
                                                this,
                                                it1.path
                                            )
                                        }
                                    }
                                }
                                mapUrl.put(1, images!![0].compressPath)
                            }
                            binding.ivFankuiImg2.drawable == null -> {
                                binding.fankuiImg2.visibility = View.VISIBLE
                                if (binding.fankuiImg1.visibility == View.VISIBLE && binding.fankuiImg2.visibility == View.VISIBLE && binding.fankuiImg3.visibility == View.VISIBLE)
                                    binding.btnAddImg.visibility = View.GONE
                                images[0]?.let {
                                    HeadUtils.roundedCornerImageUrl(
                                        binding.ivFankuiImg2,
                                        it.compressPath
                                    )
                                }
                                binding.ivFankuiImg2.setOnClickListener {
                                    images[0]?.let { it1 ->
                                        BigImageDialog.show(
                                            this,
                                            it1.path
                                        )
                                    }
                                }
                                mapUrl.put(2, images!![0].compressPath)
                            }
                            binding.ivFankuiImg3.drawable == null -> {
                                binding.fankuiImg3.visibility = View.VISIBLE
                                if (binding.fankuiImg1.visibility == View.VISIBLE && binding.fankuiImg2.visibility == View.VISIBLE && binding.fankuiImg3.visibility == View.VISIBLE)
                                    binding.btnAddImg.visibility = View.GONE
                                images[0]?.let {
                                    HeadUtils.roundedCornerImageUrl(
                                        binding.ivFankuiImg3,
                                        it.compressPath
                                    )
                                }
                                binding.ivFankuiImg3.setOnClickListener {
                                    images[0]?.let { it1 ->
                                        BigImageDialog.show(
                                            this,
                                            it1.path
                                        )
                                    }
                                }
                                mapUrl.put(3, images!![0].compressPath)
                            }
                            else -> {
                                showToast(
                                    MyApplication.getInstance().getActivityResources()
                                        .getString(R.string.feed_sel_img)
                                )
                            }
                        }
                    }
                    showImgNum(mapUrl)
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun showImgNum(mapUrl: MutableMap<Int, String>) {
        binding.tvNum.text = "(" + mapUrl.size + "/3)"
    }

    fun addFeedback() {
        var images = ""
        for (s in imgList) {
            images += s + ","
        }
        if (images != "")
            images = images.substring(0, images.length - 1)
        presenter.addFeedback(
            this,
            binding.etUserPhoneOrQq.text.toString(),
            binding.etFankuiContext.text.toString(),
            feedId,
            images
        ) { activity: HelpAndFeedbackActivity, data: StatusInfo ->
            if (data.status == "success") {
                closeLoading()
                finish()
            } else {
                closeLoading()
            }
            showToast(data.desc)
        }
    }

    fun showListPopupWindow() {
        listPopupWindow?.show()
    }
}