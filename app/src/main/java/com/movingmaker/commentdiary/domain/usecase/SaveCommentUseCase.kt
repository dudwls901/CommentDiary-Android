package com.movingmaker.commentdiary.domain.usecase

import com.movingmaker.commentdiary.data.remote.request.SaveCommentRequest
import com.movingmaker.commentdiary.domain.model.UiState
import com.movingmaker.commentdiary.domain.model.mapUiState
import com.movingmaker.commentdiary.domain.model.toUiState
import com.movingmaker.commentdiary.domain.repository.ReceivedDiaryRepository
import javax.inject.Inject

class SaveCommentUseCase @Inject constructor(
    private val receivedDiaryRepository: ReceivedDiaryRepository,
) {
    suspend operator fun invoke(saveCommentRequest: SaveCommentRequest): UiState<String> =
        receivedDiaryRepository.saveComment(saveCommentRequest).toUiState().mapUiState { it.result }

}