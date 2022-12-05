package com.movingmaker.domain.usecase

import com.movingmaker.domain.model.UiState
import com.movingmaker.domain.model.mapUiState
import com.movingmaker.domain.model.request.ReportCommentModel
import com.movingmaker.domain.model.toUiState
import com.movingmaker.domain.repository.GatherDiaryRepository
import javax.inject.Inject

class ReportCommentUseCase @Inject constructor(
    private val gatherDiaryRepository: GatherDiaryRepository,
) {
    suspend operator fun invoke(reportCommentModel: ReportCommentModel): UiState<String> =
        gatherDiaryRepository.reportComment(reportCommentModel).toUiState()
            .mapUiState { it.result }
}