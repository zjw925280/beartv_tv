package com.gys.play.ui

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import com.android.liba.network.HttpUtil
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.config.PictureConfig
import com.luck.picture.lib.config.PictureMimeType
import com.luck.picture.lib.entity.LocalMedia
import com.gys.play.R
import com.gys.play.databinding.ActivityUserInfoBinding
import com.gys.play.entity.SpUserInfo
import com.gys.play.entity.TextItem
import com.gys.play.entity.TextItemClickListener
import com.gys.play.util.CustomDatePicker
import com.gys.play.util.ImageLoaderUtils
import com.gys.play.util.dialog.BottomDialog
import com.gys.play.util.dialog.EditDialog
import com.gys.play.util.picture.GlideCacheEngine
import com.gys.play.util.picture.GlideEngine
import com.gys.play.viewmodel.UserViewModel
import com.yalantis.ucrop.view.OverlayView
import java.io.File


class UserInfoActivity :
    NovelBaseActivity<UserViewModel<UserInfoActivity>, ActivityUserInfoBinding>(R.layout.activity_user_info) {

    var dialog: Dialog? = null
    val userInfo by lazy { SpUserInfo.getUserInfo() }
    private var mDatePicker: CustomDatePicker? = null

    override fun initView() {
        setTitleText(getString(R.string.personal_information))
        userInfo.run {
            ImageLoaderUtils.loadCircle(binding.userIcon, avatar)
            binding.userName.text = user_name
            binding.userId.text = id
            binding.gender.text = getSexType()
            binding.birthday.text = birthday
            binding.signature.text = sign
            getSexSelect(sex)
        }
        initDatePicker()
    }

    override fun initData() {

    }

    override fun loadData(savedInstanceState: Bundle?) {

    }

    val SEX = "sex"
    val AVATAR = "avatar"
    val USER_NAME = "user_name"
    val SIGN = "sign"
    val BIRTHDAY = "birthday"
    var colorPosition = 0
    override fun setListener() {
        binding.genderLayout.setOnClickListener {
            val list = mutableListOf<TextItem>()
            val tag = SEX
            list.add(TextItem(getString(R.string.female), clickListener, tag, value = "2"))
            list.add(TextItem(getString(R.string.male), clickListener, tag, value = "1"))
            list.add(TextItem(getString(R.string.unknown), clickListener, tag, value = "3"))
            list.add(TextItem(getString(R.string.cancel), clickListener, "", R.color.gray_9f9f9f))
            list[colorPosition].color = R.color.key_color
            dialog = BottomDialog(this, list)
            dialog?.show()
        }
        binding.userIcon.setOnClickListener {
            val list = mutableListOf<TextItem>()
            val tag = AVATAR
//            list.add(
//                TextItem(
//                    getString(R.string.take_photo),
//                    clickListener,
//                    tag,
//                    value = "onCamera"
//                )
//            )
            list.add(
                TextItem(
                    getString(R.string.choose_from_album),
                    clickListener,
                    tag,
                    value = "onAlbum"
                )
            )
            list.add(TextItem(getString(R.string.cancel), clickListener, "", R.color.gray_9f9f9f))
            dialog = BottomDialog(this, list)
            dialog?.show()
        }
        binding.nameLayout.setOnClickListener {
            dialog = EditDialog(
                this,
                getString(R.string.update_name),
                editMaxLength = 10,
                rightClickListener = {
                    val text = (dialog as EditDialog).getEditText()
                    if (!text.isNullOrEmpty()) {
                        setUserData(USER_NAME, text)
                    }
                })
            dialog?.show()
        }
        binding.signatureLayout.setOnClickListener {
            dialog = EditDialog(
                this,
                getString(R.string.whats_up),
                editMaxLength = 20,
                rightClickListener = {
                    val text = (dialog as EditDialog).getEditText()
                    if (!text.isNullOrEmpty()) {
                        setUserData(SIGN, text)
                    }
                },
                layout = R.layout.dialog_edit_sign
            )
            dialog?.show()
        }
        binding.birthdayLayout.setOnClickListener {
            mDatePicker?.show(binding.birthday.text.toString())
        }
    }

    val clickListener = object : TextItemClickListener {
        override fun click(item: TextItem) {
            dialog?.dismiss()
            if (item.tag.isNullOrEmpty()) {
                return
            }
            if (item.tag.equals(AVATAR)) {
                when (item.value) {
                    "onCamera" -> onCamera()
                    "onAlbum" -> onAlbum()
                }
                return
            }
            if (item.tag.equals(SEX)) {
                getSexSelect(item.value)
            }
            setUserData(item.tag, item.value)
        }
    }

    private fun getSexSelect(item: String) {
        when (item) {
            "2" -> colorPosition = 0
            "1" -> colorPosition = 1
            "3" -> colorPosition = 2
        }
    }

    private fun onAlbum() {
        //打开手机中的相册
        PictureSelector.create(this)
            .openGallery(PictureMimeType.ofImage())
            .imageEngine(GlideEngine.createGlideEngine())
            .maxSelectNum(1)
            .minSelectNum(1)
            .imageSpanCount(4)
            .isEditorImage(true)//是否编辑图片
            .isEnableCrop(true)// 是否裁剪
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

    private fun onCamera() {
        //拍照
        PictureSelector.create(this)
            .openCamera(PictureMimeType.ofImage())
            .imageEngine(GlideEngine.createGlideEngine())// 外部传入图片加载引擎，必传项
            .maxSelectNum(1)// 最大图片选择数量
            .minSelectNum(1)// 最小选择数量
            .closeAndroidQChangeWH(true)//如果图片有旋转角度则对换宽高，默认为true
            .selectionMode(PictureConfig.SINGLE)// 多选 or 单选
            .loadCacheResourcesCallback(GlideCacheEngine.createCacheEngine())// 获取图片资源缓存，主要是解决华为10部分机型在拷贝文件过多时会出现卡的问题，这里可以判断只在会出现一直转圈问题机型上使用
            .isPreviewImage(true)// 是否可预览图片
            .isEnableCrop(true)// 是否裁剪
            .isCompress(true)// 是否压缩
            .compressQuality(60)// 图片压缩后输出质量
            .glideOverride(160, 160)// glide 加载宽高，越小图片列表越流畅，但会影响列表图片浏览的清晰度
            .withAspectRatio(1, 1)// 裁剪比例 如16:9 3:2 3:4 1:1 可自定义
            .hideBottomControls(true)// 是否显示uCrop工具栏，默认不显示
            .freeStyleCropEnabled(true)// |裁剪框是否可拖拽
            .showCropFrame(true)// 是否显示裁剪矩形边框 圆形裁剪时建议设为false
            .showCropGrid(true)// 是否显示裁剪矩形网格 圆形裁剪时建议设为false
            .showCropFrame(true)// 是否显示裁剪矩形边框 圆形裁剪时建议设为false
            .showCropGrid(true)// 是否显示裁剪矩形网格 圆形裁剪时建议设为false
            .cutOutQuality(90)// 裁剪输出质量 默认100
            .minimumCompressSize(100)// 小于多少kb的图片不压缩
            .isAutoScalePreviewImage(true)// 如果图片宽度不能充满屏幕则自动处理成充满模式
            .forResult(PictureConfig.CHOOSE_REQUEST)
    }

    private fun setUserData(key: String, value: String) {
        presenter.appUserEdit(key, value) { _, data ->
            when (key) {
                SEX -> {
                    userInfo.sex = value
                    binding.gender.text = userInfo.getSexType()
                }
                AVATAR -> {
                    ImageLoaderUtils.loadCircle(binding.userIcon, value)
                }
                USER_NAME -> {
                    userInfo.user_name = value
                    binding.userName.text = value
                }
                SIGN -> {
                    userInfo.sign = value
                    binding.signature.text = value
                }
                BIRTHDAY -> {
                    userInfo.birthday = value
                    binding.birthday.text = value
                }
            }
            SpUserInfo.saveUserInfo(userInfo)
        }
    }

    private fun initDatePicker() {
        val beginTimestamp: Long =
            com.gys.play.util.DateFormatUtils.str2Long("1960-01-01", false)
        val endTimestamp = System.currentTimeMillis()

        // 通过时间戳初始化日期，毫秒级别
        mDatePicker = com.gys.play.util.CustomDatePicker(
            this,
            {
                binding.birthday.text = com.gys.play.util.DateFormatUtils.long2Str(it, false)
                setUserData(BIRTHDAY, binding.birthday.text.toString().trim())
            }, beginTimestamp, endTimestamp
        )
        // 不允许点击屏幕或物理返回键关闭
        mDatePicker!!.setCancelable(true)
        // 不显示时和分
        mDatePicker!!.setCanShowPreciseTime(false)
        // 不允许循环滚动
        mDatePicker!!.setScrollLoop(false)
        // 不允许滚动动画
        mDatePicker!!.setCanShowAnim(false)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        var images: List<LocalMedia?>
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                PictureConfig.CHOOSE_REQUEST -> {
                    // 图片选择结果回调
                    images = PictureSelector.obtainMultipleResult(data)


                    val file = File(images!!.get(0).compressPath)
                    val body = HttpUtil.getUploadMultipartBodyPart(file, "image/jpeg")
                    presenter.uploadImage(
                        body,
                        com.gys.play.util.FileMD5Utils.getFileMD5(file)
                    ) { _, data ->
                        if (data.code == 1) {
//                            AppContext.showToast("头像上传成功，耐心等待审核哦")
                            setUserData(AVATAR, data.url)
                        }
//                        else AppContext.showToast("头像上传失败")
                    }

                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

}
