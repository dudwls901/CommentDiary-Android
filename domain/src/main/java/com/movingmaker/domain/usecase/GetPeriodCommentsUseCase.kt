package com.movingmaker.domain.usecase

import com.movingmaker.domain.model.UiState
import com.movingmaker.domain.model.response.Comment
import javax.inject.Inject

class GetPeriodCommentsUseCase @Inject constructor(
) {
    suspend operator fun invoke(date: String): UiState<List<Comment>> = UiState.Success(emptyList())
}