package com.gys.play.ui

import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.provider.Settings
import android.util.Base64
import android.view.View
import android.widget.Toast
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
import com.gys.play.*
import com.gys.play.databinding.ActivityLoginBinding
import com.gys.play.entity.SpUserInfo
import com.gys.play.viewmodel.UserViewModel
import com.linecorp.linesdk.LineApiResponseCode
import com.linecorp.linesdk.Scope
import com.linecorp.linesdk.auth.LineAuthenticationParams
import com.linecorp.linesdk.auth.LineLoginApi
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.util.*


class LoginActivity :
    NovelBaseActivity<UserViewModel<LoginActivity>, ActivityLoginBinding>(R.layout.activity_login) {
    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var loginIntent: Intent
    val RC_SIGN_IN = 9001
    val LINE_SIGN_IN = 9002
    val LINE_CHANNEL_ID = "1657034541"
    private lateinit var callbackManager: CallbackManager
    var isLaunchGo = false
    private lateinit var facebookLoginManager: LoginManager

    override fun onResume() {
        super.onResume()
        auth = Firebase.auth
        if (islogin() && isLogin()) {
            updateUI(auth.currentUser)
        }
//        facebookKeyHash()
    }

    //facebook 散列密钥
    private fun facebookKeyHash() {
        try {
            val info: PackageInfo = packageManager.getPackageInfo(
                packageName,
                PackageManager.GET_SIGNATURES
            )
            for (signature in info.signatures) {
                val md: MessageDigest = MessageDigest.getInstance("SHA")
                md.update(signature.toByteArray())
                UIHelper.showLog("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT))
            }
        } catch (e: PackageManager.NameNotFoundException) {
        } catch (e: NoSuchAlgorithmException) {
        }
    }

    private fun islogin(): Boolean {
        return auth.currentUser != null
    }

    override fun loadData(savedInstanceState: Bundle?) {
        isLaunchGo = intent.getBooleanExtra(Config.IS_LAUNCH_GO, false)
    }

    override fun setListener() {

        binding.anonymously.setOnClickListener {
            MyApplication.getAnalytics().setLogOn("快速注册点击量")
            showProgressDialog()
//            anonymouslyLogin()
            emailLogin()
        }
        binding.googleLogin.setOnClickListener {
            MyApplication.getAnalytics().setLogOn("google登录点击量")
            showProgressDialog()
            googleLogin()
        }
        binding.facebookLogin.setOnClickListener {
            MyApplication.getAnalytics().setLogOn("facebook登录点击量")
            showProgressDialog()
            toFacebook()
        }
        binding.phoneLogin.setOnClickListener {
            MyApplication.getAnalytics().setLogOn("手机号登录点击量")
            //跳转手机号登录界面
            PhoneActivity.login(this)
        }
        binding.lineLogin.setOnClickListener {
            toLine()
        }
        binding.back.setOnClickListener {
            checkLang()
        }
        binding.tvUser.setOnClickListener {
            Config.toUserRulePage(this)
        }
        binding.tvPrivacy.setOnClickListener {
            Config.toPrivacyPage(this)
        }
        binding.tvThirdUser.setOnClickListener {
            WebActivity.start(
                this@LoginActivity,
                "",
                Config.BaseUrl + Config.APP_USHARE + MyApplication.getInstance()
                    .getActivityResources()
                    .getString(R.string.lang_type)
            )
        }
        binding.accountServiceRule.setOnClickListener {
            Config.toAccountServiceRulePage(this)
        }
        binding.ugcRule.setOnClickListener {
            Config.toUGCRulePage(this)
        }
        binding.softRule.setOnClickListener {
            Config.toSoftRule(this)
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

    private fun toLine() {
        startActivityForResult(loginIntent, LINE_SIGN_IN)
    }

    private fun emailLogin() {
        val androidId: String = Settings.Secure.getString(
            contentResolver,
            Settings.Secure.ANDROID_ID
        )
        UIHelper.showLog(TAG, "onCreate: androidId $androidId")
        auth.createUserWithEmailAndPassword("$androidId@account.com", "account$androidId")
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    UIHelper.showLog(TAG, "createUserWithEmail:success")
                    val user = auth.currentUser
                    updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    UIHelper.showLog(TAG, "createUserWithEmail:failure-" + task.exception)
                    signInEmail(androidId)
                }
            }
    }

    private fun signInEmail(androidId: String) {
        auth.signInWithEmailAndPassword("$androidId@account.com", "account$androidId")
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    UIHelper.showLog(TAG, "signInWithEmail:success")
                    val user = auth.currentUser
                    updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    UIHelper.showLog(TAG, "signInWithEmail:failure-" + task.exception)
                    showToast("Authentication failed.")
                    updateUI(null)
                }
            }
    }

    /**
     * 匿名登录
     */
    private fun anonymouslyLogin() {
        auth.signInAnonymously()
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    UIHelper.showLog(TAG, "signInAnonymously:success")
                    val user = auth.currentUser
                    updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    UIHelper.showLog(TAG, "signInAnonymously:failure-" + task.exception)
                    showToast("Authentication failed.")
                    updateUI(null)
                }
            }
    }

    private fun updateUI(user: FirebaseUser?) {
        UIHelper.showLog(TAG, "updateUI: $user")
        user?.run {
            getIdToken(true)?.addOnCompleteListener {
                UIHelper.showLog(TAG, "addOnCompleteListener: $it")
                UIHelper.showLog(TAG, "addOnCompleteListener: isSuccessful ${it.isSuccessful()} ")
                if (it.isSuccessful()) {
                    val idToken = it.getResult().getToken()
                    UIHelper.showLog(TAG, "addOnCompleteListener: idToken ${idToken} ")
                    idToken?.let {
                        presenter.getLoginOauth(uid, idToken, { _, userInfo ->
                            if (userInfo.is_new == 1) {
                                AdjustUtil.register()
                            }
                            MyApplication.getInstance().dataViewModel.isRefreashBookShelf.value =
                                true
                            SpUserInfo.saveUserInfo(userInfo)
                            setResult(RESULT_OK, Intent().also { it.putExtra("user", userInfo) })
                            checkLang()
                            UIHelper.showLog(TAG, "addOnCompleteListener: userInfo ${userInfo} ")

                        }, { _, error ->
                            UIHelper.showLog(TAG, "addOnCompleteListener: error ${error} ")
                            showToast(error.message)
                            dismissProgressDialog()
                        })
                        return@addOnCompleteListener
                    }
                } else {
                    FirebaseAuth.getInstance().signOut()
                    dismissProgressDialog()
                }
            }
        } ?: let {
            dismissProgressDialog()
        }
    }

    var isCheckLang = false
    private fun checkLang() {
        UIHelper.showLog(TAG, "checkLang: " + isCheckLang)
        if (isCheckLang) {
            return
        }
        isCheckLang = true
        if (isLaunchGo) {
            dismissProgressDialog()
            binding.loginLoading.visibility = View.VISIBLE
            Handler().postDelayed({
                startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                finish()
            }, 500)
        } else {
            finish()
        }
    }

    override fun initView() {
        MyApplication.getAnalytics().setLogOn("登录页面曝光")
        if (SpServiceConfig.isHidePhone()) {
            binding.phoneLoginLayout.visibility = View.GONE
        }
        if (SpServiceConfig.isHideFacebook()) {
            binding.facebookLayout.visibility = View.GONE
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

        //line配置
        loginIntent = LineLoginApi.getLoginIntent(
            this, LINE_CHANNEL_ID, LineAuthenticationParams.Builder()
                .scopes(Arrays.asList(Scope.PROFILE))
                .build()
        )
        facebookLoginManager = LoginManager.getInstance()
        // Initialize Facebook Login button
        callbackManager = CallbackManager.Factory.create()
//        buttonFacebookLogin.setReadPermissions("email", "public_profile")
        LoginManager.getInstance().registerCallback(callbackManager, object :
            FacebookCallback<LoginResult> {
            override fun onSuccess(loginResult: LoginResult) {
                UIHelper.showLog(TAG, "facebook:onSuccess:$loginResult")
                handleFacebookAccessToken(loginResult.accessToken)
            }

            override fun onCancel() {
                UIHelper.showLog(TAG, "facebook:onCancel")
                dismissProgressDialog()
            }

            override fun onError(error: FacebookException) {
                UIHelper.showLog(TAG, "facebook:onError-" + error)
                dismissProgressDialog()
            }
        })
    }

    // [START auth_with_facebook]
    private fun handleFacebookAccessToken(token: AccessToken) {
        UIHelper.showLog(TAG, "handleFacebookAccessToken:$token")

        val credential = FacebookAuthProvider.getCredential(token.token)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    UIHelper.showLog(TAG, "signInWithCredential:success")
                    val user = auth.currentUser
                    updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    UIHelper.showLog(TAG, "signInWithCredential:failure-" + task.exception)
                    Toast.makeText(
                        baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT
                    ).show()
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
                dismissProgressDialog()
            }
        } else if (requestCode == LINE_SIGN_IN) {
            val result = LineLoginApi.getLoginResultFromIntent(data)
            when (result.responseCode) {
                LineApiResponseCode.SUCCESS -> {
                    var usrId = ""
                    result.lineProfile?.run {
                        usrId = this.userId
                    }
                    var token = ""
                    result.lineCredential?.run {
                        token = this.accessToken.tokenString
                    }
                }
            }
        } else {
            //facebook 登录
            callbackManager.onActivityResult(requestCode, resultCode, data)
        }
    }

    // [START auth_with_google]
    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    UIHelper.showLog(TAG, "signInWithCredential:success")
                    val user = auth.currentUser
                    updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    UIHelper.showLog(TAG, "signInWithCredential:failure-" + task.exception)
                    updateUI(null)
                }
            }
    }
}