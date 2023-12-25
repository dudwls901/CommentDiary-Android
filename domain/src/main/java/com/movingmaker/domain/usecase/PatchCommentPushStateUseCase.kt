package com.movingmaker.domain.usecase

import com.movingmaker.domain.model.UiState
import javax.inject.Inject

class PatchCommentPushStateUseCase @Inject constructor(
) {
    suspend operator fun invoke(): UiState<Map<String, Boolean>> = UiState.Success(mapOf())
}