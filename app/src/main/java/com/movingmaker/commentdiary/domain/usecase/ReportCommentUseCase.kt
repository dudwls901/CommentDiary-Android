package com.movingmaker.commentdiary.domain.usecase

import com.movingmaker.commentdiary.data.remote.request.ReportCommentRequest
import com.movingmaker.commentdiary.domain.repository.GatherDiaryRepository
import javax.inject.Inject

class ReportCommentUseCase @Inject constructor(
    private val gatherDiaryRepository: GatherDiaryRepository,
) {
    suspend operator fun invoke(reportCommentRequest: ReportCommentRequest) =
        gatherDiaryRepository.reportComment(reportCommentRequest)

}