package com.movingmaker.domain.usecase

import com.movingmaker.domain.model.UiState
import com.movingmaker.domain.model.mapUiState
import com.movingmaker.domain.model.toUiState
import com.movingmaker.domain.repository.CommentRepository
import javax.inject.Inject

class LikeCommentUseCase @Inject constructor(
    private val commentRepository: CommentRepository,
) {
    suspend operator fun invoke(commentId: Long): UiState<String> =
        commentRepository.likeComment(commentId).toUiState().mapUiState { it.result }

}