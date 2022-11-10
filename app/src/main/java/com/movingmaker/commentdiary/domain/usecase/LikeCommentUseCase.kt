package com.movingmaker.commentdiary.domain.usecase

import com.movingmaker.commentdiary.domain.model.UiState
import com.movingmaker.commentdiary.domain.model.mapUiState
import com.movingmaker.commentdiary.domain.model.toUiState
import com.movingmaker.commentdiary.domain.repository.GatherDiaryRepository
import javax.inject.Inject

class LikeCommentUseCase @Inject constructor(
    private val gatherDiaryRepository: GatherDiaryRepository,
) {
    suspend operator fun invoke(commentId: Long): UiState<String> =
        gatherDiaryRepository.likeComment(commentId).toUiState().mapUiState { it.result }

}