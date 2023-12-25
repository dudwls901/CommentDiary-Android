package com.movingmaker.domain.usecase

import com.movingmaker.domain.model.UiState
import com.movingmaker.domain.model.request.ReportDiaryModel
import javax.inject.Inject

class ReportDiaryUseCase @Inject constructor(
) {
    suspend operator fun invoke(reportDiaryModel: ReportDiaryModel): UiState<String> = UiState.Success("temp")
}