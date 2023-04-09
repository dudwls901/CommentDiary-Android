package com.movingmaker.domain.usecase

import com.movingmaker.domain.repository.DiaryRepository
import javax.inject.Inject

class DeleteTempAloneDiaryUseCase @Inject constructor(
    private val diaryRepository: DiaryRepository
) {
    suspend operator fun invoke(diaryId: Long) {
        diaryRepository.deleteDiary(diaryId)
    }
}