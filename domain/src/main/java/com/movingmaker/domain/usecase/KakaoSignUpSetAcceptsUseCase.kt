package com.movingmaker.domain.usecase

import com.movingmaker.domain.model.UiState
import com.movingmaker.domain.model.mapUiState
import com.movingmaker.domain.model.request.KakaoSignUpModel
import com.movingmaker.domain.model.toUiState
import com.movingmaker.domain.repository.ForSignUpRepository
import javax.inject.Inject

class KakaoSignUpSetAcceptsUseCase @Inject constructor(
    private val signUpRepository: ForSignUpRepository,
) {
    suspend operator fun invoke(kakaoSignUpModel: KakaoSignUpModel): UiState<String> =
        signUpRepository.kakaoSignUpSetAccepts(kakaoSignUpModel).toUiState()
            .mapUiState { it.result }

}