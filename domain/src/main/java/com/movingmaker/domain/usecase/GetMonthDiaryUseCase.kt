package com.movingmaker.domain.usecase

import com.movingmaker.domain.model.UiState
import com.movingmaker.domain.model.mapUiState
import com.movingmaker.domain.model.response.Diary
import com.movingmaker.domain.model.toUiState
import com.movingmaker.domain.repository.GatherDiaryRepository
import javax.inject.Inject

class GetMonthDiaryUseCase @Inject constructor(
    private val getMonthDiaryRepository: GatherDiaryRepository,
) {
    suspend operator fun invoke(date: String): UiState<MutableList<Diary>> =
        getMonthDiaryRepository.getMonthDiary(date).toUiState().mapUiState { it.result }

}