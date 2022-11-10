package com.movingmaker.commentdiary.domain.usecase

import com.movingmaker.commentdiary.domain.model.UiState
import com.movingmaker.commentdiary.domain.model.mapUiState
import com.movingmaker.commentdiary.domain.model.toUiState
import com.movingmaker.commentdiary.domain.repository.MyPageRepository
import javax.inject.Inject

class PatchCommentPushStateUseCase @Inject constructor(
    private val myPageRepository: MyPageRepository,
) {
    suspend operator fun invoke(): UiState<Map<String,Char>> =
        myPageRepository.patchCommentPushState().toUiState().mapUiState { it.result }
}