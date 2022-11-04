package com.movingmaker.commentdiary.domain.usecase

import com.movingmaker.commentdiary.data.model.Diary
import com.movingmaker.commentdiary.domain.model.UiState
import com.movingmaker.commentdiary.domain.model.mapUiState
import com.movingmaker.commentdiary.domain.model.toUiState
import com.movingmaker.commentdiary.domain.repository.GatherDiaryRepository
import javax.inject.Inject

class GetMonthDiaryUseCase @Inject constructor(
    private val getMonthDiaryRepository: GatherDiaryRepository,
) {
    suspend operator fun invoke(date: String): UiState<MutableList<Diary>> =
        getMonthDiaryRepository.getMonthDiary(date).toUiState().mapUiState { it.result }

}