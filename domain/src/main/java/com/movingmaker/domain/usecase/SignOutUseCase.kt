package com.movingmaker.domain.usecase

import com.movingmaker.domain.model.UiState
import javax.inject.Inject

class SignOutUseCase @Inject constructor(
) {
    suspend operator fun invoke(): UiState<String> = UiState.Success("temp")
}