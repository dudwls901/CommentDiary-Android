package com.movingmaker.domain.usecase

import com.movingmaker.domain.model.UiState
import com.movingmaker.domain.model.mapUiState
import com.movingmaker.domain.model.request.SaveCommentModel
import com.movingmaker.domain.model.toUiState
import com.movingmaker.domain.repository.ReceivedDiaryRepository
import javax.inject.Inject

class SaveCommentUseCase @Inject constructor(
    private val receivedDiaryRepository: ReceivedDiaryRepository,
) {
    suspend operator fun invoke(saveCommentModel: SaveCommentModel): UiState<String> =
        receivedDiaryRepository.saveComment(saveCommentModel).toUiState().mapUiState { it.result }
}