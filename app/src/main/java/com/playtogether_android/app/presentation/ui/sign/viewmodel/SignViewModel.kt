package com.playtogether_android.app.presentation.ui.sign.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.playtogether_android.data.singleton.PlayTogetherRepository
import com.playtogether_android.domain.model.sign.SignInData
import com.playtogether_android.domain.model.sign.SignInItem
import com.playtogether_android.domain.model.sign.UserInfo
import com.playtogether_android.domain.repository.sign.SignRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class SignViewModel @Inject constructor(
//    val postSignInUseCase: PostSignInUseCase,
    val repository: SignRepository
) : ViewModel() {

    var signData: MutableLiveData<String> = MutableLiveData()

    //로그인 시 필요한 값
    var id = MutableLiveData<String>()
    var pw = MutableLiveData<String>()


    //로그인 request
    var requestSignIn = SignInItem("", "")

    var name = MutableLiveData<String>()


    //로그인
    private var _signIn = MutableLiveData<SignInData>()
    val signIn: LiveData<SignInData>
        get() = _signIn

    //jwt토큰 set
    var signInToken: MutableLiveData<SignInData> = MutableLiveData()

    private val _isLogin = MutableLiveData<Boolean>()
    val isLogin: LiveData<Boolean> = _isLogin

    private val _statusCode = MutableLiveData<Int>()
    val statusCode: LiveData<Int> = _statusCode

    fun tokenChecker(accessToken: String, refreshToken: String) {
        viewModelScope.launch {
            kotlin.runCatching {
                repository.getTokenIssuance(accessToken, refreshToken)
            }.onSuccess {
                _statusCode.value = it.status
            }.onFailure {
                Timber.e("token issuance : ${it.message}")
                when (it) {
                    is retrofit2.HttpException -> {
                        _statusCode.value = it.code()
                    }
                }
            }
        }
    }

    fun signup(body: UserInfo) {
        viewModelScope.launch {
            kotlin.runCatching {
                repository.putSignup(PlayTogetherRepository.userToken, body)
            }.onSuccess {
                _isLogin.value = true
            }.onFailure {
                Timber.e("signup error : $it")
                _isLogin.value = false
            }
        }
    }

    fun kakaoLogin(): Boolean {
        var isSignup = false
        viewModelScope.launch {
            kotlin.runCatching {
                repository.postKakaoLogin()
            }.onSuccess {
                with(PlayTogetherRepository) {
                    kakaoAccessToken = "" // todo 인터셉트 변경 위함
                    kakaoUserToken = it.accessToken
                    kakaoAccessToken = it.accessToken
                    userToken = kakaoUserToken
                    kakaoUserRefreshToken = it.refreshToken
                    kakaoUserlogOut = false
                }
                Timber.e("kakao login access : ${it.accessToken}")
                Timber.e("kakao login refresh : ${it.refreshToken}")
                isSignup = it.isSignup
                signData.value = it.accessToken //signData Set accessToken
                _isLogin.value = true
            }.onFailure {
                Timber.e("kakao login error :${it.message}")
                _isLogin.value = false
            }
        }
        return isSignup
    }

    fun postSignIn(item: SignInItem) {

    }


}