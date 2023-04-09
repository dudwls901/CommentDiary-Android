package com.movingmaker.domain.usecase

import com.movingmaker.domain.model.UiState
import com.movingmaker.domain.model.mapUiState
import com.movingmaker.domain.model.response.Comment
import com.movingmaker.domain.model.toUiState
import com.movingmaker.domain.repository.CommentRepository
import javax.inject.Inject

class GetPeriodCommentsUseCase @Inject constructor(
    private val commentRepository: CommentRepository,
) {
    suspend operator fun invoke(date: String): UiState<List<Comment>> =
        commentRepository.getPeriodComments(date).toUiState().mapUiState { it.result }
}