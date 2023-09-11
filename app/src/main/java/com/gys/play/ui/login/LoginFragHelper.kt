package com.gys.play.ui.login

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.jccppp.start.AcCallBackHelper
import com.jccppp.start.jk.IAcCallBack
import com.jccppp.start.launchAc
import com.gys.play.entity.UserInfo
import com.gys.play.ui.LoginActivity
import com.gys.play.util.KvUserInfo


class LoginFragHelper : Fragment(), IAcCallBack by AcCallBackHelper() {

    companion object {

        private val TAG = "LoginFragHelper"

        fun load(activity: FragmentActivity, call: ((UserInfo) -> Unit)) {
            val supportFragmentManager = activity.supportFragmentManager

            val findFragment = supportFragmentManager.findFragmentByTag(TAG)

            if (findFragment is LoginFragHelper) {
                findFragment.call = call
                findFragment.goLogo()
            } else {
                supportFragmentManager.beginTransaction()
                    .add(LoginFragHelper().also { it.call = call }, TAG)
                    .commitAllowingStateLoss()
            }
        }

        fun withLogin(activity: FragmentActivity, next: () -> Unit) {
            if(KvUserInfo.isLogin()){
                next()
            }else{
                load(activity){
                    next()
                }
            }
        }

    }

    var call: ((UserInfo) -> Unit)? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initAcCallBackHelper()
        goLogo()

    }

    fun goLogo() {
        launchAc<LoginActivity>{
            result { code, data ->
                if (code == AppCompatActivity.RESULT_OK)
                    call?.invoke(data!!.getSerializableExtra("user") as UserInfo)

                parentFragmentManager.beginTransaction().remove(this@LoginFragHelper)
                    .commitAllowingStateLoss()
            }
        }
    }

}