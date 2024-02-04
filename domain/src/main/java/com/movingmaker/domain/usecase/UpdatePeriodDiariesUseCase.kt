package com.movingmaker.domain.usecase

import com.movingmaker.domain.model.UiState
import com.movingmaker.domain.model.response.Diary
import javax.inject.Inject

class UpdatePeriodDiariesUseCase @Inject constructor(
) {
    suspend operator fun invoke(date: String): UiState<List<Diary>> = UiState.Success(emptyList())
}