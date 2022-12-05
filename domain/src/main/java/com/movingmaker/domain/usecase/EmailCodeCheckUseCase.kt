package com.movingmaker.domain.usecase

import com.movingmaker.domain.model.UiState
import com.movingmaker.domain.model.mapUiState
import com.movingmaker.domain.model.request.EmailCodeCheckModel
import com.movingmaker.domain.model.toUiState
import com.movingmaker.domain.repository.ForSignUpRepository
import javax.inject.Inject

class EmailCodeCheckUseCase @Inject constructor(
    private val forSignUpRepository: ForSignUpRepository,
) {
    suspend operator fun invoke(emailCodeCheckModel: EmailCodeCheckModel): UiState<String> =
        forSignUpRepository.emailCodeCheck(emailCodeCheckModel).toUiState()
            .mapUiState { it.result }
}