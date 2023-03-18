package com.movingmaker.domain.usecase

import com.movingmaker.domain.model.UiState
import com.movingmaker.domain.model.mapUiState
import com.movingmaker.domain.model.response.Diary
import com.movingmaker.domain.model.toUiState
import com.movingmaker.domain.repository.DiaryRepository
import javax.inject.Inject

class UpdateAllDiariesUseCase @Inject constructor(
    private val diaryRepository: DiaryRepository,
) {
    suspend operator fun invoke(): UiState<List<Diary>> =
        diaryRepository.updateAllDiaries().toUiState().mapUiState { it.result }
}