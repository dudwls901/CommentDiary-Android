package com.movingmaker.domain.usecase

import com.movingmaker.domain.model.UiState
import com.movingmaker.domain.model.request.EditDiaryModel
import javax.inject.Inject

class EditDiaryUseCase @Inject constructor(
) {

    /**
     * 수정시 현재 달 일기 갱신 -> Room Flow에 collect 가능
     * */
    suspend operator fun invoke(
        diaryId: Long,
        date: String,
        editDiaryModel: EditDiaryModel
    ): UiState<String> = UiState.Success("temp")
}