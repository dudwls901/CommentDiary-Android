package com.movingmaker.domain.usecase

import com.movingmaker.domain.model.response.Diary
import com.movingmaker.domain.repository.DiaryRepository
import javax.inject.Inject

class SaveTempDiaryUseCase @Inject constructor(
    private val diaryRepository: DiaryRepository
) {
    suspend operator fun invoke(tempDiary: Diary) {
        diaryRepository.insertTempDiary(tempDiary)
    }
}