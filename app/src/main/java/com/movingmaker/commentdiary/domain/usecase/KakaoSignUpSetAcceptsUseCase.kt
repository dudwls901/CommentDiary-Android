package com.movingmaker.commentdiary.domain.usecase

import com.movingmaker.commentdiary.data.remote.request.KakaoSignUpRequest
import com.movingmaker.commentdiary.domain.repository.MyPageRepository
import javax.inject.Inject

class KakaoSignUpSetAcceptsUseCase @Inject constructor(
    private val myPageRepository: MyPageRepository,
) {
    suspend operator fun invoke(kakaoSignUpRequest: KakaoSignUpRequest) =
        myPageRepository.kakaoSignUpSetAccepts(kakaoSignUpRequest)

}