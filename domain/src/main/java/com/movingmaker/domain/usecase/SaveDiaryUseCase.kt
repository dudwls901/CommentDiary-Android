package com.movingmaker.domain.usecase

import com.movingmaker.domain.model.UiState
import com.movingmaker.domain.model.request.SaveDiaryModel
import com.movingmaker.domain.model.response.DiaryId
import javax.inject.Inject

class SaveDiaryUseCase @Inject constructor(
) {
    /**
     * 저장시 현재 달 일기 갱신 -> Room Flow에 collect 가능
     * */
    suspend operator fun invoke(saveDiaryModel: SaveDiaryModel): UiState<DiaryId> = UiState.Success(DiaryId(0))
}