package com.gys.play.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import com.android.liba.util.UIHelper
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.*
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.gys.play.R
import com.gys.play.databinding.ActivityPhoneBinding
import com.gys.play.AdjustUtil
import com.gys.play.MyApplication
import com.gys.play.entity.SpUserInfo
import com.gys.play.viewmodel.UserViewModel
import java.util.concurrent.TimeUnit

class PhoneActivity :
    NovelBaseActivity<UserViewModel<PhoneActivity>, ActivityPhoneBinding>(R.layout.activity_phone) {
    val TAG1 = "PhoneActivity_"
    val phoneID = "phone"

    private lateinit var auth: FirebaseAuth

    private var storedVerificationId: String? = ""
    private lateinit var resendToken: PhoneAuthProvider.ForceResendingToken
    private lateinit var callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks
    var type = 0

    var countDownTimer = object : CountDownTimer(60 * 1000, 1000) {
        override fun onTick(millisUntilFinished: Long) {
            binding.getCode.visibility = View.GONE
            binding.codeDd.visibility = View.VISIBLE
            binding.codeDd.text = "${millisUntilFinished / 1000}"

        }

        override fun onFinish() {
            binding.getCode.visibility = View.VISIBLE
            binding.codeDd.visibility = View.GONE
        }
    }

    companion object {
        const val TYPE_LOGIN = 1
        const val TYPE_LINK = 2

        //        const val TYPE_UN_LINK = 3
        private const val TYPE = "TYPE"

        fun login(context: Context) {
            start(context, TYPE_LOGIN)
        }

        fun link(context: Context) {
            start(context, TYPE_LINK)
        }

//        fun unLink(context: Context) {
//            start(context, TYPE_UN_LINK)
//        }

        private fun start(context: Context, type: Int) {
            val intent = Intent(context, PhoneActivity::class.java)
                .putExtra(TYPE, type)
            context.startActivity(intent)
        }
    }


    override fun loadData(savedInstanceState: Bundle?) {

    }

    override fun setListener() {
        binding.getCode.setOnClickListener {
            val phone = binding.editPhone.text.toString().trim()
            startPhoneNumberVerification("+" + phone)
            showProgressDialog()
        }
        binding.sure.setOnClickListener {
            if (storedVerificationId.isNullOrEmpty()) {
                return@setOnClickListener
            }
            val code = binding.editCode.text.toString().trim()
            verifyPhoneNumberWithCode(storedVerificationId, code)
            showProgressDialog()
        }
    }

    private fun link(credential: AuthCredential) {
        auth.currentUser!!.linkWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    UIHelper.showLog(TAG1, "linkWithCredential:success")
                    updateUI()
                } else {
                    task.exception?.run {
                        showToast(message)
                    }
                    // If sign in fails, display a message to the user.
                    UIHelper.showLog(TAG1, "linkWithCredential:failure-" + task.exception)
                }
                dismissProgressDialog()
            }
    }

    // 解除账号关联
    private fun unlink(providerId: String) {
        // [START auth_unlink]
        Firebase.auth.currentUser?.unlink(providerId)
            ?.addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {

                }
            }
        // [END auth_unlink]
    }

    override fun initView() {
        setTitleText(getString(R.string.bind_phone_numbert))
    }

    override fun initData() {
        type = intent.getIntExtra(TYPE, 0)
        // [START initialize_auth]
        // Initialize Firebase Auth
        auth = Firebase.auth
        // [END initialize_auth]

        // Initialize phone auth callbacks
        // [START phone_auth_callbacks]
        callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                // This callback will be invoked in two situations:
                // 1 - Instant verification. In some cases the phone number can be instantly
                //     verified without needing to send or enter a verification code.
                // 2 - Auto-retrieval. On some devices Google Play services can automatically
                //     detect the incoming verification SMS and perform verification without
                //     user action.
                UIHelper.showLog(TAG1, "onVerificationCompleted:$credential")
                login(credential)
            }

            override fun onVerificationFailed(e: FirebaseException) {
                // This callback is invoked in an invalid request for verification is made,
                // for instance if the the phone number format is not valid.
                UIHelper.showLog(TAG1, "onVerificationFailed-" + e)
                dismissProgressDialog()
                showToast(e.message)
                if (e is FirebaseAuthInvalidCredentialsException) {
                    // Invalid request
                } else if (e is FirebaseTooManyRequestsException) {
                    // The SMS quota for the project has been exceeded
                }

                // Show a message and update the UI
            }

            override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken
            ) {
                // The SMS verification code has been sent to the provided phone number, we
                // now need to ask the user to enter the code and then construct a credential
                // by combining the code with a verification ID.
                UIHelper.showLog(TAG1, "onCodeSent:$verificationId")
                UIHelper.showLog(TAG1, "onCodeSent:$token")
                // Save verification ID and resending token so we can use them later
                storedVerificationId = verificationId
                resendToken = token
                countDownTimer.cancel()
                countDownTimer.start()
                dismissProgressDialog()
            }
        }
        // [END phone_auth_callbacks]
    }

    override fun onResume() {
        super.onResume()
        updateUI()
    }
    // [END on_start_check_user]

    private fun startPhoneNumberVerification(phoneNumber: String) {
        // [START start_phone_auth]
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phoneNumber)       // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(this)                 // Activity (for callback binding)
            .setCallbacks(callbacks)          // OnVerificationStateChangedCallbacks
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
        // [END start_phone_auth]
    }

    private fun verifyPhoneNumberWithCode(verificationId: String?, code: String) {
        // [START verify_with_code]
        val credential = PhoneAuthProvider.getCredential(verificationId!!, code)
        UIHelper.showLog(TAG1, "verifyPhoneNumberWithCode: $credential")
        // [END verify_with_code]
        when (type) {
            TYPE_LOGIN -> login(credential)
            TYPE_LINK -> link(credential)
//            TYPE_UN_LINK -> {
//                if (verificationOldPhone) {
//                    editPhone.text.clear()
//                    editCode.text.clear()
//                    verificationOldPhone = false
//                } else {
//                    link(credential)
//                }
//            }
        }
    }

    // [START resend_verification]
    private fun resendVerificationCode(
        phoneNumber: String,
        token: PhoneAuthProvider.ForceResendingToken?
    ) {
        val optionsBuilder = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phoneNumber)       // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(this)                 // Activity (for callback binding)
            .setCallbacks(callbacks)          // OnVerificationStateChangedCallbacks
        if (token != null) {
            optionsBuilder.setForceResendingToken(token) // callback's ForceResendingToken
        }
        PhoneAuthProvider.verifyPhoneNumber(optionsBuilder.build())
    }
    // [END resend_verification]

    // [START sign_in_with_phone]
    private fun login(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    UIHelper.showLog(TAG1, "signInWithCredential:success")
                    login(auth.currentUser)
                } else {
                    // Sign in failed, display a message and update the UI
                    UIHelper.showLog(TAG1, "signInWithCredential:failure-" + task.exception)
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        // The verification code entered was invalid
                        task.exception?.run {
                            showToast(message)
                        }
                    } else {
                        //其他错误
                        task.exception?.run {
                            showToast(getString(R.string.code_error))
                        }
                    }
                    // Update UI
                }
            }
    }
    // [END sign_in_with_phone]
    /**
     * 登录成功直接退出当前页面
     */
    private fun updateUI(user: FirebaseUser? = auth.currentUser) {
        user?.run {
            for (data in providerData) {
                if (data.providerId.equals(phoneID)) {
                    finish()
                }
            }
        }
    }

    private fun login(user: FirebaseUser?) {
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
                            UIHelper.showLog(TAG, "addOnCompleteListener: userInfo ${userInfo} ")
                            finish()
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
}