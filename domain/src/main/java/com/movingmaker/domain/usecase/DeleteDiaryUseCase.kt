package com.movingmaker.domain.usecase

import com.movingmaker.domain.model.UiState
import com.movingmaker.domain.model.mapUiState
import com.movingmaker.domain.model.toUiState
import com.movingmaker.domain.repository.MyDiaryRepository
import javax.inject.Inject

class DeleteDiaryUseCase @Inject constructor(
    private val myDiaryRepository: MyDiaryRepository,
) {
    suspend operator fun invoke(diaryId: Long): UiState<String> =
        myDiaryRepository.deleteDiary(diaryId).toUiState().mapUiState { it.result }

}