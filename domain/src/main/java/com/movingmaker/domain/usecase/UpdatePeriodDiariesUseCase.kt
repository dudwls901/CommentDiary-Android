package com.movingmaker.domain.usecase

import com.movingmaker.domain.model.UiState
import com.movingmaker.domain.model.mapUiState
import com.movingmaker.domain.model.response.Diary
import com.movingmaker.domain.model.toUiState
import com.movingmaker.domain.repository.DiaryRepository
import javax.inject.Inject

class UpdatePeriodDiariesUseCase @Inject constructor(
    private val diaryRepository: DiaryRepository,
) {
    suspend operator fun invoke(date: String): UiState<List<Diary>> =
        diaryRepository.updatePeriodDiaries(date).toUiState().mapUiState { it.result }
}