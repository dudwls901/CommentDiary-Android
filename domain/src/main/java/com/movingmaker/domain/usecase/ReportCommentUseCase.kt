package com.movingmaker.domain.usecase

import com.movingmaker.domain.model.UiState
import com.movingmaker.domain.model.request.ReportCommentModel
import javax.inject.Inject

class ReportCommentUseCase @Inject constructor(
) {
    suspend operator fun invoke(reportCommentModel: ReportCommentModel): UiState<String> = UiState.Success("temp")
}