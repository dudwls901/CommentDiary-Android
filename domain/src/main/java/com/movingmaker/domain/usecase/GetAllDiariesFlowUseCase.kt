package com.movingmaker.domain.usecase

import com.movingmaker.domain.model.response.Diary
import com.movingmaker.domain.repository.DiaryRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllDiariesFlowUseCase @Inject constructor(
    private val diaryRepository: DiaryRepository
) {
    operator fun invoke(): Flow<List<Diary>> {
        return diaryRepository.getAllDiariesFlow()
    }
}