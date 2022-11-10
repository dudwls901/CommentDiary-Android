package com.movingmaker.commentdiary.domain.usecase

import com.movingmaker.commentdiary.data.model.Comment
import com.movingmaker.commentdiary.domain.model.UiState
import com.movingmaker.commentdiary.domain.model.mapUiState
import com.movingmaker.commentdiary.domain.model.toUiState
import com.movingmaker.commentdiary.domain.repository.MyPageRepository
import javax.inject.Inject

class GetMonthCommentUseCase @Inject constructor(
    private val myPageRepository: MyPageRepository,
) {
    suspend operator fun invoke(date: String): UiState<List<Comment>> =
        myPageRepository.getMonthComment(date).toUiState().mapUiState { it.result }
}