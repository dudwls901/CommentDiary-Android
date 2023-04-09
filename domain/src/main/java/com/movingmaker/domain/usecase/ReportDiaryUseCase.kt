package com.movingmaker.domain.usecase

import com.movingmaker.domain.model.UiState
import com.movingmaker.domain.model.mapUiState
import com.movingmaker.domain.model.request.ReportDiaryModel
import com.movingmaker.domain.model.toUiState
import com.movingmaker.domain.repository.ReportRepository
import javax.inject.Inject

class ReportDiaryUseCase @Inject constructor(
    private val reportRepository: ReportRepository,
) {
    suspend operator fun invoke(reportDiaryModel: ReportDiaryModel): UiState<String> =
        reportRepository.reportDiary(reportDiaryModel).toUiState().mapUiState { it.result }
}