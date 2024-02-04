package com.movingmaker.domain.usecase

import com.movingmaker.domain.model.UiState
import com.movingmaker.domain.model.request.SaveCommentModel
import com.movingmaker.domain.model.response.SavedComment
import javax.inject.Inject

class SaveCommentUseCase @Inject constructor(
) {
    suspend operator fun invoke(saveCommentModel: SaveCommentModel): UiState<SavedComment> = UiState.Success(SavedComment("temp","0"))
}