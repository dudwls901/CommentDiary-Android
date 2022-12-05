package com.movingmaker.domain.usecase

import com.movingmaker.domain.model.UiState
import com.movingmaker.domain.model.mapUiState
import com.movingmaker.domain.model.response.ReceivedDiary
import com.movingmaker.domain.model.toUiState
import com.movingmaker.domain.repository.ReceivedDiaryRepository
import javax.inject.Inject

class GetReceivedDiaryUseCase @Inject constructor(
    private val receivedDiaryRepository: ReceivedDiaryRepository,
) {
    suspend operator fun invoke(date: String): UiState<ReceivedDiary> =
        receivedDiaryRepository.getReceivedDiary(date).toUiState().mapUiState { it.result }

}