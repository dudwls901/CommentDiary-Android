package com.movingmaker.domain.usecase

import com.movingmaker.domain.model.UiState
import com.movingmaker.domain.model.request.ChangePasswordModel
import javax.inject.Inject

class ChangePasswordUseCase @Inject constructor(
) {
    suspend operator fun invoke(changePasswordModel: ChangePasswordModel): UiState<String> =
        UiState.Success("temp")
}