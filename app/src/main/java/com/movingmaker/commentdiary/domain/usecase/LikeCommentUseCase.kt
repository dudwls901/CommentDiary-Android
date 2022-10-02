package com.movingmaker.commentdiary.domain.usecase

import com.movingmaker.commentdiary.domain.repository.GatherDiaryRepository
import javax.inject.Inject

class LikeCommentUseCase @Inject constructor(
    private val gatherDiaryRepository: GatherDiaryRepository,
) {
    suspend operator fun invoke(commentId: Long) =
        gatherDiaryRepository.likeComment(commentId)

}