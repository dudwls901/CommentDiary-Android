package com.movingmaker.domain.usecase

import com.movingmaker.domain.model.UiState
import com.movingmaker.domain.model.response.ReceivedDiary
import javax.inject.Inject

class GetReceivedDiaryUseCase @Inject constructor(
) {
    suspend operator fun invoke(date: String): UiState<ReceivedDiary> = UiState.Success(ReceivedDiary(0,"temp","temp", "", mutableListOf()))

}