package com.movingmaker.domain.usecase

import com.movingmaker.domain.model.response.Diary
import com.movingmaker.domain.repository.DiaryRepository
import javax.inject.Inject

class GetPeriodDiariesUseCase @Inject constructor(
    private val diaryRepository: DiaryRepository
) {
    suspend operator fun invoke(date: String): List<Diary> =
        diaryRepository.getPeriodDiaries(date)
}