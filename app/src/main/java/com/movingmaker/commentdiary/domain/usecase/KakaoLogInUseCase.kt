package com.movingmaker.commentdiary.domain.usecase

import com.movingmaker.commentdiary.data.remote.request.KakaoLoginRequest
import com.movingmaker.commentdiary.domain.repository.ForSignUpRepository
import javax.inject.Inject

class KakaoLogInUseCase @Inject constructor(
    private val forSignUpRepository: ForSignUpRepository,
) {
    suspend operator fun invoke(kakaoLoginRequest: KakaoLoginRequest) =
        forSignUpRepository.kakaoLogIn(kakaoLoginRequest)

}