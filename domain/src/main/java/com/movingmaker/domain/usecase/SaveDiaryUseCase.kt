package com.movingmaker.domain.usecase

import com.movingmaker.domain.model.UiState
import com.movingmaker.domain.model.mapUiState
import com.movingmaker.domain.model.request.SaveDiaryModel
import com.movingmaker.domain.model.response.DiaryId
import com.movingmaker.domain.model.toUiState
import com.movingmaker.domain.repository.MyDiaryRepository
import javax.inject.Inject

class SaveDiaryUseCase @Inject constructor(
    private val myDiaryRepository: MyDiaryRepository,
) {
    suspend operator fun invoke(saveDiaryModel: SaveDiaryModel): UiState<DiaryId> =
        myDiaryRepository.saveDiary(saveDiaryModel).toUiState().mapUiState { it.result }

}