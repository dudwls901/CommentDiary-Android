package com.movingmaker.domain.usecase

import com.movingmaker.domain.model.UiState
import com.movingmaker.domain.model.mapUiState
import com.movingmaker.domain.model.request.ReportCommentModel
import com.movingmaker.domain.model.toUiState
import com.movingmaker.domain.repository.ReportRepository
import javax.inject.Inject

class ReportCommentUseCase @Inject constructor(
    private val reportRepository: ReportRepository,
) {
    suspend operator fun invoke(reportCommentModel: ReportCommentModel): UiState<String> =
        reportRepository.reportComment(reportCommentModel).toUiState()
            .mapUiState { it.result }
}