package com.movingmaker.domain.usecase

import com.movingmaker.domain.model.UiState
import com.movingmaker.domain.model.mapUiState
import com.movingmaker.domain.model.toUiState
import com.movingmaker.domain.repository.ForSignUpRepository
import javax.inject.Inject

class FindPasswordUseCase @Inject constructor(
    private val forSignUpRepository: ForSignUpRepository,
) {
    suspend operator fun invoke(email: String): UiState<String> {
        return forSignUpRepository.findPassword(email).toUiState().mapUiState { it.result }
    }
}