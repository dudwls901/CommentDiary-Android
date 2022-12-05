package com.movingmaker.domain.usecase

import com.movingmaker.domain.model.UiState
import com.movingmaker.domain.model.mapUiState
import com.movingmaker.domain.model.request.EditDiaryModel
import com.movingmaker.domain.model.toUiState
import com.movingmaker.domain.repository.MyDiaryRepository
import javax.inject.Inject

class EditDiaryUseCase @Inject constructor(
    private val myDiaryRepository: MyDiaryRepository,
) {
    suspend operator fun invoke(diaryId: Long, editDiaryModel: EditDiaryModel): UiState<String> =
        myDiaryRepository.editDiary(diaryId, editDiaryModel).toUiState().mapUiState { it.result }

}