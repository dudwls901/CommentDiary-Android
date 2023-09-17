package com.movingmaker.domain.usecase

import com.movingmaker.domain.model.UiState
import com.movingmaker.domain.model.mapUiState
import com.movingmaker.domain.model.request.KakaoLoginModel
import com.movingmaker.domain.model.response.Login
import com.movingmaker.domain.model.toUiState
import com.movingmaker.domain.repository.MemberRepository
import javax.inject.Inject

class KakaoLogInUseCase @Inject constructor(
    private val memberRepository: MemberRepository,
) {
    suspend operator fun invoke(kakaoLoginModel: KakaoLoginModel): UiState<Login> =
        memberRepository.kakaoLogIn(kakaoLoginModel).toUiState().mapUiState { it.result }

}