package com.movingmaker.domain.usecase

import com.movingmaker.domain.model.UiState
import com.movingmaker.domain.model.mapUiState
import com.movingmaker.domain.model.response.Diary
import com.movingmaker.domain.model.toUiState
import com.movingmaker.domain.repository.GatherDiaryRepository
import javax.inject.Inject

class GetAllDiaryUseCase @Inject constructor(
    private val gatherDiaryRepository: GatherDiaryRepository,
) {
    suspend operator fun invoke(): UiState<MutableList<Diary>> =
        gatherDiaryRepository.getAllDiary().toUiState().mapUiState { it.result }

}