package com.movingmaker.domain.usecase

import com.movingmaker.domain.model.UiState
import javax.inject.Inject

class DeleteDiaryUseCase @Inject constructor(
) {
    /**
     * 삭제시 현재 달 일기 갱신 -> Room Flow에 collect 가능
     * */
    suspend operator fun invoke(
        diaryId: Long,
        date: String
    ): UiState<String> = UiState.Success("temp")
}