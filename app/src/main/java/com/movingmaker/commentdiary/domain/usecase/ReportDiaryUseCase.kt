package com.movingmaker.commentdiary.domain.usecase

import com.movingmaker.commentdiary.data.remote.request.ReportDiaryRequest
import com.movingmaker.commentdiary.domain.repository.ReceivedDiaryRepository
import javax.inject.Inject

class ReportDiaryUseCase @Inject constructor(
    private val receivedDiaryRepository: ReceivedDiaryRepository,
) {
    suspend operator fun invoke(reportDiaryRequest: ReportDiaryRequest) =
        receivedDiaryRepository.reportDiary(reportDiaryRequest)
}