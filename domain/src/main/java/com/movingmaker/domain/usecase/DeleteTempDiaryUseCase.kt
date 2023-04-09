package com.movingmaker.domain.usecase

import com.movingmaker.domain.model.response.Diary
import com.movingmaker.domain.repository.DiaryRepository
import javax.inject.Inject

class DeleteTempDiaryUseCase @Inject constructor(
    private val diaryRepository: DiaryRepository
) {
    suspend operator fun invoke(commentDiary: Diary): Int =
        diaryRepository.deleteTempDiary(commentDiary)
}