package com.movingmaker.domain.usecase

import com.movingmaker.domain.model.UiState
import com.movingmaker.domain.model.mapUiState
import com.movingmaker.domain.model.request.ReportDiaryModel
import com.movingmaker.domain.model.toUiState
import com.movingmaker.domain.repository.ReceivedDiaryRepository
import javax.inject.Inject

class ReportDiaryUseCase @Inject constructor(
    private val receivedDiaryRepository: ReceivedDiaryRepository,
) {
    suspend operator fun invoke(reportDiaryModel: ReportDiaryModel): UiState<String> =
        receivedDiaryRepository.reportDiary(reportDiaryModel).toUiState().mapUiState { it.result }
}