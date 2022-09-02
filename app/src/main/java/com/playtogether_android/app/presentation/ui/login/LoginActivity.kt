package com.playtogether_android.app.presentation.ui.login

import android.content.Intent
import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.Scopes
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.Scope
import com.google.android.gms.tasks.Task
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.AuthErrorCause
import com.kakao.sdk.common.util.Utility
import com.kakao.sdk.user.UserApiClient
import com.playtogether_android.app.BuildConfig
import com.playtogether_android.app.R
import com.playtogether_android.app.databinding.ActivityLoginBinding
import com.playtogether_android.app.presentation.base.BaseActivity
import com.playtogether_android.app.presentation.ui.login.view.LoginTermsActivity
import com.playtogether_android.app.presentation.ui.main.MainActivity
import com.playtogether_android.app.presentation.ui.sign.viewmodel.SignViewModel
import com.playtogether_android.app.util.PlayTogetherSharedPreference
import com.playtogether_android.app.util.shortToast
import com.playtogether_android.data.singleton.PlayTogetherRepository
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import timber.log.Timber


@AndroidEntryPoint
class LoginActivity : BaseActivity<ActivityLoginBinding>(R.layout.activity_login) {
    private val signViewModel: SignViewModel by viewModels()
    private lateinit var client: GoogleSignInClient
    private lateinit var startForActivity: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        var keyHash = Utility.getKeyHash(this)
        Timber.e("kkkkkkkkkk: $keyHash")
        super.onCreate(savedInstanceState)
        startForActivity =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                if (it.resultCode == RESULT_OK) {
                    val task = GoogleSignIn.getSignedInAccountFromIntent(it.data)
                    handleSignInResult(task)
                }else{
                    Timber.e("result : $it")
                    Timber.e("result code: ${it.resultCode}")
                }
            }
        initView()
    }

    private fun handleSignInResult(task: Task<GoogleSignInAccount>) {
        try {
            val account = task.getResult(ApiException::class.java)

            val accessToken = account.idToken
            val refreshToken = account.serverAuthCode
            Timber.e("google access : $accessToken")
            Timber.e("google refresh : $refreshToken")
            PlayTogetherRepository.googleAccessToken = accessToken!!
            if (!refreshToken.isNullOrBlank()) {
                PlayTogetherRepository.googleUserRefreshToken = refreshToken
            }
            startActivity(Intent(this, MainActivity::class.java))
        } catch (e: ApiException) {
            Timber.e("$e")
            shortToast("로그인 실패")
        }
    }

    private fun initView() {
        onClickListener()
    }

    private fun onClickListener() {
        btnKakaoListener()
        btnGoogleListener()
    }

    private fun btnKakaoListener() {
        binding.ivLoginKakao.setOnClickListener {
            setKakaoBtnListener()
        }
    }

    private fun btnGoogleListener() {
        binding.ivLoginGoogle.setOnClickListener {
            val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                /*.requestServerAuthCode(serverClientId)
                .requestIdToken(getString(R.string.server_client_id)*/
                .requestEmail()
                .build()

            client = GoogleSignIn.getClient(this, gso)
            startForActivity.launch(client.signInIntent)
        }
    }

    private fun nextActivity(intent: Intent) {
        startActivity(intent)
    }

    private fun setKakaoBtnListener() {
        val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
            if (error != null) {
                shortToast("로그인 실패")
                getErrorLog(error)
            } else if (token != null) {
                UserApiClient.instance.me { _, error ->
                    PlayTogetherRepository.kakaoAccessToken = token.accessToken
                    Timber.e("kakao token access : ${token.accessToken}")
                    Timber.e("kakao token refresh : ${token.refreshToken}")
                    with(signViewModel) {
                        val isSignup = kakaoLogin()
                        isLogin.observe(this@LoginActivity) {
                            if (it) {
                                Timber.d("testset : ${signViewModel.signData.value}")
                                PlayTogetherSharedPreference.setAccessToken(this@LoginActivity, signViewModel.signData.value ?: "")
                                PlayTogetherSharedPreference.setRefreshToken(this@LoginActivity, signViewModel.signData.value ?: "")
                                if (isSignup) {
                                    val intent =
                                        Intent(this@LoginActivity, MainActivity::class.java)
                                    nextActivity(intent)
                                } else {
                                    val intent =
                                        Intent(this@LoginActivity, LoginTermsActivity::class.java)
                                    nextActivity(intent)
                                }
                            } else {
                                shortToast("로그인 실패")
                            }
                        }
                    }
                }
            } else {
                shortToast("else")
                Timber.e("모르겠다.")
            }
        }

        if (UserApiClient.instance.isKakaoTalkLoginAvailable(this@LoginActivity)) {
            UserApiClient.instance.loginWithKakaoTalk(this@LoginActivity, callback = callback)
        } else {
            UserApiClient.instance.loginWithKakaoAccount(
                this@LoginActivity,
                callback = callback
            )
        }
    }

    private fun getErrorLog(error: Throwable) {
        when {
            error.toString() == AuthErrorCause.AccessDenied.toString() -> {
                Timber.e("접근이 거부 됨(동의 취소)")
            }
            error.toString() == AuthErrorCause.InvalidClient.toString() -> {
                Timber.e("유효하지 않은 앱")
            }
            error.toString() == AuthErrorCause.InvalidGrant.toString() -> {
                Timber.e("인증 수단이 유효하지 않아 인증할 수 없는 상태")
            }
            error.toString() == AuthErrorCause.InvalidRequest.toString() -> {
                Timber.e("요청 파라미터 오류")
            }
            error.toString() == AuthErrorCause.InvalidScope.toString() -> {
                Timber.e("유효하지 않은 scope ID")
            }
            error.toString() == AuthErrorCause.Misconfigured.toString() -> {
                Timber.e("설정이 올바르지 않음(android key hash)")
            }
            error.toString() == AuthErrorCause.ServerError.toString() -> {
                Timber.e("서버 내부 에러")
            }
            error.toString() == AuthErrorCause.Unauthorized.toString() -> {
                Timber.e("앱이 요청 권한이 없음")
            }
            else -> { // Unknown
                Timber.e("기타 에러")
            }
        }
    }
}