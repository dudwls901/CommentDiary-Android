package com.movingmaker.commentdiary.domain.usecase

import com.movingmaker.commentdiary.data.model.ReceivedDiary
import com.movingmaker.commentdiary.domain.model.UiState
import com.movingmaker.commentdiary.domain.model.mapUiState
import com.movingmaker.commentdiary.domain.model.toUiState
import com.movingmaker.commentdiary.domain.repository.ReceivedDiaryRepository
import javax.inject.Inject

class GetReceivedDiaryUseCase @Inject constructor(
    private val receivedDiaryRepository: ReceivedDiaryRepository,
) {
    suspend operator fun invoke(date: String): UiState<ReceivedDiary> =
        receivedDiaryRepository.getReceivedDiary(date).toUiState().mapUiState { it.result }

}