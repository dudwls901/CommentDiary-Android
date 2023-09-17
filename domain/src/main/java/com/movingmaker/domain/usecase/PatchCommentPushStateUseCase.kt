package com.movingmaker.domain.usecase

import com.movingmaker.domain.model.UiState
import com.movingmaker.domain.model.mapUiState
import com.movingmaker.domain.model.toUiState
import com.movingmaker.domain.repository.MyPageRepository
import javax.inject.Inject

class PatchCommentPushStateUseCase @Inject constructor(
    private val myPageRepository: MyPageRepository,
) {
    suspend operator fun invoke(): UiState<Map<String, Boolean>> =
        myPageRepository.patchCommentPushState().toUiState().mapUiState { it.result }
}