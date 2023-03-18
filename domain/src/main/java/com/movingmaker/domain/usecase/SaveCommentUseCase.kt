package com.movingmaker.domain.usecase

import com.movingmaker.domain.model.UiState
import com.movingmaker.domain.model.mapUiState
import com.movingmaker.domain.model.request.SaveCommentModel
import com.movingmaker.domain.model.toUiState
import com.movingmaker.domain.repository.CommentRepository
import javax.inject.Inject

class SaveCommentUseCase @Inject constructor(
    private val commentRepository: CommentRepository,
) {
    suspend operator fun invoke(saveCommentModel: SaveCommentModel): UiState<String> =
        commentRepository.saveComment(saveCommentModel).toUiState().mapUiState { it.result }
}