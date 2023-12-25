package com.movingmaker.domain.usecase

import com.movingmaker.domain.model.UiState
import javax.inject.Inject

class LikeCommentUseCase @Inject constructor(
) {
    suspend operator fun invoke(commentId: Long): UiState<String> = UiState.Success("temp")

}