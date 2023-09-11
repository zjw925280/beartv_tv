package  com.gys.play.fragment.home

import android.annotation.SuppressLint
import android.content.Intent
import android.view.View
import com.android.liba.context.AppContext
import com.android.liba.ui.base.BindingFragment
import com.firebase.ui.auth.AuthUI
import com.gys.play.AuthorWebActivity
import com.gys.play.Config
import com.gys.play.MyApplication
import com.gys.play.R
import com.gys.play.databinding.FragmentMineBinding
import com.gys.play.entity.SpUserInfo
import com.gys.play.entity.UserInfo
import com.gys.play.ui.*
import com.gys.play.ui.comment.CommentActivity
import com.gys.play.ui.helpfeedback.HelpAndFeedbackActivity
import com.gys.play.ui.video.VideoKv
import com.gys.play.util.ImageLoaderUtils
import com.gys.play.util.KvUserInfo
import com.gys.play.util.LiveDataUtil
import com.gys.play.util.onClick
import com.gys.play.viewmodel.MineViewModel


class MineFragment :
    BindingFragment<MineViewModel<MineFragment>, FragmentMineBinding>() {
    override fun initData() {
//        presenter.appHomePosition { _, data ->
//            data.items?.let {
//                if (it.isEmpty()) {
//                    return@let
//                }
//                val item = it[0]
//                ImageLoaderUtils.loadEndShowImage(binding.mineBanner, item.thumb!!)
//                binding.mineBanner.setOnClickListener {
//                    item.jump(mActivity)
//                }
//            }
//        }
    }

    override fun getLayoutId() = R.layout.fragment_mine
    override fun initView() {
    }

    @SuppressLint("QueryPermissionsNeeded")
    override fun setListener() {
        binding.history.setOnClickListener {
            if (!SpUserInfo.isLogin()) {
                goLogin()
                return@setOnClickListener
            }
            HistoryListActivity.start(context)
        }
        binding.subscribe.setOnClickListener {
            if (!SpUserInfo.isLogin()) {
                goLogin()
                return@setOnClickListener
            }
            HistoryListActivity.start(context, false)
        }
        binding.order.setOnClickListener {
            MyApplication.getAnalytics().setBookCoin("明细点击量")
            if (!SpUserInfo.isLogin()) {
                goLogin()
                return@setOnClickListener
            }
            context?.startActivity(Intent(context, GoldOpenRecordActivity::class.java))
        }
        binding.logOut.setOnClickListener {
            VideoKv.removeLocal()
            MyApplication.getInstance().dataViewModel.isRefreashBookShelf.value = true
            SpUserInfo.clear()
            AuthUI.getInstance().signOut(it.context)
        }
        binding.loginText.setOnClickListener {
            if (!SpUserInfo.isLogin()) {
                goLogin()
            }
        }
//        binding.newVersionTip.visibility =
//            if (SpServiceConfig.isNeedUpdate()) View.VISIBLE else View.GONE
        binding.mineSetting.setOnClickListener {
            activity?.let {
                startActivity(Intent(it, SettingActivity::class.java))
            }
        }
        binding.coinLayout.setOnClickListener {
            MyApplication.getAnalytics().setMine("账户点击量")
            Config.toRecharge(activity)
        }
        binding.vipLayout.setOnClickListener {
            MyApplication.getAnalytics().setMine("vip点击量")
            Config.toVip(activity)
        }
        binding.userImage.setOnClickListener {
            activity?.let {
                if (!SpUserInfo.isLogin()) {
                    goLogin()
                } else {
                    startActivity(Intent(it, UserInfoActivity::class.java))
                }
            }
        }
        binding.loginText.setOnClickListener {
            activity?.let {
                if (!SpUserInfo.isLogin()) {
                    goLogin()
                } else {
                    startActivity(Intent(it, UserInfoActivity::class.java))
                }
            }
        }
//        binding.llAutoPay.setNoDouble {
//        }
        binding.llHelpAndFeedback.setOnClickListener {
            if (!SpUserInfo.isLogin()) {
                goLogin()
                return@setOnClickListener
            }
            activity?.let {
                startActivity(Intent(it, HelpAndFeedbackActivity::class.java))
            }
        }
//        binding.preference.setOnClickListener {
//            if (!SpUserInfo.isLogin()) {
//                goLogin()
//                return@setOnClickListener
//            }
//            MyApplication.getAnalytics().setMine("我的阅读偏好")
//        }
//        binding.customer.setOnClickListener {
//            val serviceEmail = SpServiceConfig.getServiceEmail()
//            if (serviceEmail.isNullOrEmpty()) {
//                return@setOnClickListener
//            }
//            startActivity(
//                Intent(Intent.ACTION_SENDTO)
//                    .setData(Uri.parse("mailto:$serviceEmail"))
//            )
//        }
//
//        binding.registerAuthor.setOnClickListener {
//            if (!SpUserInfo.isLogin()) {
//                goLogin()
//                return@setOnClickListener
//            }
//            MyApplication.getAnalytics().setMine("我要投稿")
//            goAuthorWeb(getString(R.string.register_author), Config.APP_REGISTER)
//        }
//        binding.update.setOnClickListener {
//            goAuthorWeb(getString(R.string.update_jiuwen), Config.APP_BOOKING_LIST)
//        }
//        binding.contributeNew.setOnClickListener {
//            goAuthorWeb(getString(R.string.update_jiuwen), Config.APP_ADD_BOOK)
//        }
//        binding.management.setOnClickListener {
//            goAuthorWeb(getString(R.string.zuoping_manage), Config.APP_MANAGE)
//        }
//        binding.column.setOnClickListener {
//            SpUserInfo.getUserInfo()?.let {
//                if (it.isLogin()) {
//                    activity?.run {
//                        startActivity(
//                            Intent(this, AuthorActivity::class.java)
//                                .putExtra(Config.AUTHOR_ID, it.id)
//                        )
//                    }
//                }
//            }
//        }
        binding.myComment.onClick = {
            CommentActivity.start(mActivity)
               /* activity?.run {
                    startActivity(Intent(this, MyCommentActivity::class.java))
                }*/
        }
        binding.fmMsgLayout.setOnClickListener {
//            if (!islogin()) {
//                goLogin()
//                return@setOnClickListener
//            }
            MyApplication.getAnalytics().setMine("消息")
            activity?.let {
                Config.toMessageCenterPage(it)
            }
        }

        LiveDataUtil.userInfo.observe(this) {
            refreshUserUI()
        }
    }

    private fun goAuthorWeb(title: String, url: String) {
        AuthorWebActivity.start(
            activity,
            title,
            Config.BaseUrl + url + SpUserInfo.checkId()
                    + "&token=" + SpUserInfo.checkToken()
                    + "&os_type=android"
                    + "&lang_type=" + MyApplication.getInstance().getActivityResources()
                .getString(R.string.lang_type)
        )
    }

    private fun goLogin() {
        MyApplication.getAnalytics().setLogOn("登录/注册按钮点击量")
        activity?.let {
            startActivity(Intent(it, LoginActivity::class.java))
        }
    }

    override fun onResume() {
        super.onResume()
//        if (SpServiceConfig.isHideDownload()) {
//            binding.downloadLayout.visibility = View.GONE
//            binding.downloadLayoutLine.visibility = View.GONE
//        } else {
//            binding.downloadLayout.visibility = View.VISIBLE
//            binding.downloadLayoutLine.visibility = View.VISIBLE
//        }
        presenter.getMsgInfo { _, msgInfo ->
            if (msgInfo.num == 0) {//没新消息
                binding.fmMsgLayout.setImageResource(R.mipmap.icon_xinxi)
            } else {    //有消息
                binding.fmMsgLayout.setImageResource(R.mipmap.icon_xinxi1)
            }
        }
        if (SpUserInfo.isLogin()) {
            presenter.getUserInfo { _, userInfo ->
                SpUserInfo.saveUserInfo(userInfo)
                userView(userInfo)
            }
        }
    }

    private fun refreshUserUI() {
        val userinfo = SpUserInfo.getUserInfo()
        binding.vipType.text = getString(R.string.vip_top_text)
        binding.vipTime.text = getString(R.string.watch_blockbuster)
        binding.vipIcon.visibility = View.GONE
        if (userinfo.isLogin()) {
            userView(userinfo)
        } else {
            binding.logOut.visibility = View.GONE
//            binding.loginText2.setText(R.string.mine_login_text2)
            binding.loginText.text = getString(R.string.login_regist)
            ImageLoaderUtils.loadCircle(binding.userImage, R.mipmap.icon_tx_m)
            binding.vipIcon.visibility = View.GONE
//            binding.registerAuthor.visibility = View.VISIBLE
//            binding.authorLayout.visibility = View.GONE
            binding.myComment.visibility = View.GONE
            binding.coinLeftTv.text = getString(R.string.my_coin_left, "0")
        }
    }

    /**
     * 登录的用户状态
     */
    @SuppressLint("StringFormatMatches")
    private fun userView(userInfo: UserInfo) {
        userInfo.run {
            binding.logOut.visibility = View.VISIBLE
            binding.myComment.visibility = View.VISIBLE
            binding.loginText.text = user_name
            if (!avatar.isNullOrEmpty()) {
                ImageLoaderUtils.loadCircle(binding.userImage, avatar)
            }
            if (isVip()) {
                binding.vipIcon.visibility = View.VISIBLE
                binding.vipType.text = "VIP剩餘天數"
                binding.vipTime.text = "${remainingDays()}天"
//                binding.noVipLayout.visibility = View.GONE
//                binding.vipLoginLayout.visibility = View.VISIBLE
//                binding.vipTimeLeft.text = getString(R.string.sign_text5, getVipLeftDay())
            } else {

//                binding.noVipLayout.visibility = View.VISIBLE
//                binding.vipLoginLayout.visibility = View.GONE
//                binding.vipPrice.text =
//                    getString(R.string.vip_day_amount, SpServiceConfig.getVipDayAmount())
//                    getString(R.string.vip_month_amount, SpServiceConfig.getVipMonthAmount())
            }
            binding.coinLeftTv.text = getString(R.string.my_coin_left, userInfo.coins)
        }
        val isPrefer = AppContext.getMMKV().decodeBool(Config.HAS_FREQUENCY, false)
        if (SpUserInfo.isLogin() && getString(R.string.lang_type).equals("3") && !isPrefer) {
            val selFrequency = KvUserInfo.lookType
            var perfer = 0
            when (selFrequency) {
                0 -> perfer = 3
                1 -> perfer = 1
                2 -> perfer = 2
            }
            presenter.appUserEdit("$perfer") { _, data ->
                AppContext.getMMKV().encode(Config.HAS_FREQUENCY, true)
            }
        }
    }
}