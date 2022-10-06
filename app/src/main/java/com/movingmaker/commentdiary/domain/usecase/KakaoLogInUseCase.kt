package com.movingmaker.commentdiary.domain.usecase

import com.movingmaker.commentdiary.data.model.AuthTokens
import com.movingmaker.commentdiary.data.remote.request.KakaoLoginRequest
import com.movingmaker.commentdiary.domain.model.UiState
import com.movingmaker.commentdiary.domain.model.mapUiState
import com.movingmaker.commentdiary.domain.model.toUiState
import com.movingmaker.commentdiary.domain.repository.ForSignUpRepository
import javax.inject.Inject

class KakaoLogInUseCase @Inject constructor(
    private val forSignUpRepository: ForSignUpRepository,
) {
    suspend operator fun invoke(kakaoLoginRequest: KakaoLoginRequest): UiState<AuthTokens> =
        forSignUpRepository.kakaoLogIn(kakaoLoginRequest).toUiState().mapUiState { it.result }

}