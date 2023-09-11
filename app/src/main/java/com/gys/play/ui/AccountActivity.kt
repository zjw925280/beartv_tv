package com.gys.play.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.android.liba.util.UIHelper
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.gys.play.R
import com.gys.play.SpServiceConfig
import com.gys.play.databinding.ActivityAccountBinding
import com.gys.play.util.dialog.SimpleDialog
import com.gys.play.viewmodel.UserViewModel

class AccountActivity :
    NovelBaseActivity<UserViewModel<AccountActivity>, ActivityAccountBinding>(R.layout.activity_account) {
    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient
    val RC_SIGN_IN = 9001
    private lateinit var callbackManager: CallbackManager
    private lateinit var facebookLoginManager: LoginManager

    var googleLogin = false
    var facebookLogin = false
    var phoneLogin = false
    val googleProviderID = "google.com"
    val facebookProviderID = "facebook.com"
    val phoneID = "phone"

    override fun loadData(savedInstanceState: Bundle?) {

    }

    override fun setListener() {
        binding.googleLogin.setOnClickListener {
            if (googleLogin) {
                SimpleDialog(this,
                    getString(R.string.top_tip),
                    getString(R.string.unbind_account, "Google"),
                    getString(R.string.cancel),
                    getString(R.string.done),
                    rightClickListener = { unlink(googleProviderID) }).show()
            } else {
                googleLogin()
            }
        }
        binding.facebookLogin.setOnClickListener {
            if (facebookLogin) {
                SimpleDialog(
                    this,
                    getString(R.string.top_tip),
                    getString(R.string.unbind_account, "Facebook"),
                    getString(R.string.cancel),
                    getString(R.string.done),
                    rightClickListener = {
                        unlink(facebookProviderID)
                    }).show()
            } else {
                toFacebook()
            }
        }
        binding.phoneLogin.setOnClickListener {
            //跳转手机号登录界面
            if (phoneLogin) {
                SimpleDialog(
                    this,
                    getString(R.string.top_tip),
                    getString(R.string.unbind_phone),
                    getString(R.string.cancel),
                    getString(R.string.done),
                    rightClickListener = {
                        unlink(phoneID)
                    }).show()
            } else {
                PhoneActivity.link(this)
            }
        }
    }

    /**
     * 调用facebook 登录
     */
    private fun toFacebook() {
        val accessToken = AccessToken.getCurrentAccessToken()
        val isLoggedIn = accessToken != null && !accessToken.isExpired
        if (isLoggedIn) {
            //判断Facebook是否登录  如果登录先退出
            try {
                facebookLoginManager.logOut()
                AccessToken.setCurrentAccessToken(null)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        //调用facebook登录
        facebookLoginManager.logInWithReadPermissions(
            this,
            listOf("public_profile", "email")
        )
    }

    /**
     * google 登录
     */
    private fun googleLogin() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    private fun updateUI(user: FirebaseUser?) {
        userInfo()
    }

    override fun initView() {
        setTitleText(getString(R.string.account_security))
        if (SpServiceConfig.isHidePhone()) {
            binding.phoneLogin.visibility = View.GONE
        }
        if (SpServiceConfig.isHideFacebook()) {
            binding.facebookLogin.visibility = View.GONE
        }
    }

    override fun initData() {
        auth = Firebase.auth

        //配置谷歌服务
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)

//        buttonFacebookLogin = binding.facebookLoginButton
        // Initialize Facebook Login button
        callbackManager = CallbackManager.Factory.create()
//        buttonFacebookLogin.setReadPermissions("email", "public_profile")
        facebookLoginManager = LoginManager.getInstance()
        facebookLoginManager.registerCallback(callbackManager, object :
            FacebookCallback<LoginResult> {
            override fun onSuccess(loginResult: LoginResult) {
                UIHelper.showLog(TAG, "facebook:onSuccess:$loginResult")
                handleFacebookAccessToken(loginResult.accessToken)
            }

            override fun onCancel() {
                UIHelper.showLog(TAG, "facebook:onCancel")
            }

            override fun onError(error: FacebookException) {
                UIHelper.showLog(TAG, "facebook:onError-" + error)
            }
        })
    }

    // [START auth_with_facebook]
    private fun handleFacebookAccessToken(token: AccessToken) {
        UIHelper.showLog(TAG, "handleFacebookAccessToken:$token")

        val credential = FacebookAuthProvider.getCredential(token.token)
        auth.currentUser!!.linkWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    UIHelper.showLog(TAG, "linkWithCredential:success")
                    val user = auth.currentUser
                    updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    UIHelper.showLog(TAG, "linkWithCredential:failure-" + task.exception)
                    task.exception?.let {
                        showToast(it.message)
                    }
                    updateUI(null)
                    facebookLoginManager.logOut()
                }
            }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        //RC_SIGN_IN 谷歌登录id
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)!!
                UIHelper.showLog(TAG, "firebaseAuthWithGoogle:" + account.id)
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                UIHelper.showLog(TAG, "Google sign in failed-" + e)
                showToast("Google sign in failed ${e.message}")

            }
        } else {
            //facebook 登录
            callbackManager.onActivityResult(requestCode, resultCode, data)
        }
    }

    // [START auth_with_google]
    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)

        auth.currentUser?.linkWithCredential(credential)
            ?.addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    UIHelper.showLog(TAG, "linkWithCredential:success")
                    val user = auth.currentUser
                    updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    UIHelper.showLog(TAG, "linkWithCredential:failure-" + task.exception)
                    task.exception?.let {
                        showToast(it.message)
                    }
                    updateUI(null)
                }
            }
    }

    override fun onResume() {
        super.onResume()
        userInfo()
    }

    fun userInfo() {
        binding.phoneAccount.text = getString(R.string.unbound)
        binding.googleAccount.text = getString(R.string.unbound)
        binding.facebookAccount.text = getString(R.string.unbound)
        binding.phoneAccount.setTextColor(resources.getColor(R.color.key_color))
        binding.googleAccount.setTextColor(resources.getColor(R.color.C_333333))
        binding.facebookAccount.setTextColor(resources.getColor(R.color.key_color))
        googleLogin = false
        facebookLogin = false
        phoneLogin = false
        Firebase.auth.currentUser?.let {
            UIHelper.showLog(TAG, "userInfo uid    :${it.uid} }")
            UIHelper.showLog(TAG, "userInfo IdToken: ${it.getIdToken(false)}")
//            UIHelper.showLog(TAG, "userInfo IdToken: ${it.getIdToken(true)}")
            binding.userID.text = it.uid
            for (data in it.providerData) {
                UIHelper.showLog(TAG, "userInfo: ${data.providerId}")
                when (data.providerId) {
                    googleProviderID -> {
                        googleLogin = true
                        binding.googleAccount.text = data.email
                        binding.googleAccount.setTextColor(resources.getColor(R.color.gray_9f9f9f))
                    }
                    facebookProviderID -> {
                        facebookLogin = true
                        binding.facebookAccount.text = data.email
                        binding.facebookAccount.setTextColor(resources.getColor(R.color.gray_9f9f9f))
                    }
                    phoneID -> {
                        phoneLogin = true
                        binding.phoneAccount.text = data.phoneNumber
                        binding.phoneAccount.setTextColor(resources.getColor(R.color.gray_9f9f9f))
                    }
                }
            }
        }
    }


    // 解除账号关联
    private fun unlink(providerId: String) {
        // [START auth_unlink]
        Firebase.auth.currentUser?.let {
            var bindingNumber = 0
            for (data in it.providerData) {
                UIHelper.showLog(TAG, "userInfo: ${data.providerId}")
                when (data.providerId) {
                    googleProviderID -> bindingNumber++
                    facebookProviderID -> {
                        if (!SpServiceConfig.isHideFacebook()) {
                            bindingNumber++
                        }
                    }
                    phoneID -> {
                        if (!SpServiceConfig.isHidePhone()) {
                            bindingNumber++
                        }
                    }
                }
            }
            if (bindingNumber <= 1) {
                SimpleDialog(
                    this,
                    getString(R.string.top_tip),
                    getString(R.string.cant_unbunding),
                    "",
                    getString(R.string.done),
                ).show()
            } else {
                it.unlink(providerId)
                    ?.addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            if (providerId.equals(facebookProviderID)) {
                                facebookLoginManager.logOut()
                            }
                            userInfo()
                        }
                    }
                // [END auth_unlink]
            }
        }
    }
}