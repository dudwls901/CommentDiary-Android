package com.movingmaker.domain.usecase

import com.movingmaker.domain.model.response.Diary
import com.movingmaker.domain.repository.GatherDiaryRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetMonthDiaryUseCase @Inject constructor(
    private val getMonthDiaryRepository: GatherDiaryRepository,
) {
    operator fun invoke(date: String): Flow<List<Diary>> {
        return getMonthDiaryRepository.getMonthDiary(date)
    }

}