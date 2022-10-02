package com.movingmaker.commentdiary.domain.usecase

import com.movingmaker.commentdiary.domain.repository.GatherDiaryRepository
import javax.inject.Inject

class GetMonthDiaryUseCase @Inject constructor(
    private val getMonthDiaryRepository: GatherDiaryRepository,
) {
    suspend operator fun invoke(date: String) =
        getMonthDiaryRepository.getMonthDiary(date)

}