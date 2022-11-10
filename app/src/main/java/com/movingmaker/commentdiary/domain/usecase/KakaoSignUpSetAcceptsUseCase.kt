package com.movingmaker.commentdiary.domain.usecase

import com.movingmaker.commentdiary.data.remote.request.KakaoSignUpRequest
import com.movingmaker.commentdiary.domain.model.UiState
import com.movingmaker.commentdiary.domain.model.mapUiState
import com.movingmaker.commentdiary.domain.model.toUiState
import com.movingmaker.commentdiary.domain.repository.ForSignUpRepository
import javax.inject.Inject

class KakaoSignUpSetAcceptsUseCase @Inject constructor(
    private val signUpRepository: ForSignUpRepository,
) {
    suspend operator fun invoke(kakaoSignUpRequest: KakaoSignUpRequest): UiState<String> =
        signUpRepository.kakaoSignUpSetAccepts(kakaoSignUpRequest).toUiState().mapUiState { it.result }

}