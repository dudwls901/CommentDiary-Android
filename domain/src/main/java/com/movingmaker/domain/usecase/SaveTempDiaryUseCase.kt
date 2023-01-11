package com.movingmaker.domain.usecase

import com.movingmaker.domain.model.response.Diary
import com.movingmaker.domain.repository.GatherDiaryRepository
import javax.inject.Inject

class SaveTempDiaryUseCase @Inject constructor(
    private val gatherDiaryRepository: GatherDiaryRepository
) {
    suspend operator fun invoke(tempDiary: Diary) {
        gatherDiaryRepository.insertTempDiary(tempDiary)
    }
}