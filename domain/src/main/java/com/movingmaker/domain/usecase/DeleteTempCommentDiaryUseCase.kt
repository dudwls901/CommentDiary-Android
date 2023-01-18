package com.movingmaker.domain.usecase

import com.movingmaker.domain.model.response.Diary
import com.movingmaker.domain.repository.GatherDiaryRepository
import javax.inject.Inject

class DeleteTempCommentDiaryUseCase @Inject constructor(
    private val gatherDiaryRepository: GatherDiaryRepository
) {
    suspend operator fun invoke(commentDiary: Diary): Int =
        gatherDiaryRepository.deleteTempCommentDiary(commentDiary)
}