package com.movingmaker.domain.usecase

import com.movingmaker.domain.model.UiState
import com.movingmaker.domain.model.mapUiState
import com.movingmaker.domain.model.request.KakaoLoginModel
import com.movingmaker.domain.model.response.AuthTokens
import com.movingmaker.domain.model.toUiState
import com.movingmaker.domain.repository.ForSignUpRepository
import javax.inject.Inject

class KakaoLogInUseCase @Inject constructor(
    private val forSignUpRepository: ForSignUpRepository,
) {
    suspend operator fun invoke(kakaoLoginModel: KakaoLoginModel): UiState<AuthTokens> =
        forSignUpRepository.kakaoLogIn(kakaoLoginModel).toUiState().mapUiState { it.result }

}