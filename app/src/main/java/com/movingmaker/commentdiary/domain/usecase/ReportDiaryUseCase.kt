package com.movingmaker.commentdiary.domain.usecase

import com.movingmaker.commentdiary.data.remote.request.ReportDiaryRequest
import com.movingmaker.commentdiary.domain.model.UiState
import com.movingmaker.commentdiary.domain.model.mapUiState
import com.movingmaker.commentdiary.domain.model.toUiState
import com.movingmaker.commentdiary.domain.repository.ReceivedDiaryRepository
import javax.inject.Inject

class ReportDiaryUseCase @Inject constructor(
    private val receivedDiaryRepository: ReceivedDiaryRepository,
) {
    suspend operator fun invoke(reportDiaryRequest: ReportDiaryRequest): UiState<String> =
        receivedDiaryRepository.reportDiary(reportDiaryRequest).toUiState().mapUiState { it.result }
}