package com.movingmaker.domain.usecase

import com.movingmaker.domain.model.response.Diary
import com.movingmaker.domain.repository.DiaryRepository
import javax.inject.Inject

class GetAllDiariesUseCase @Inject constructor(
    private val diaryRepository: DiaryRepository
) {
    suspend operator fun invoke(): List<Diary> {
        return diaryRepository.getAllDiaries()
    }

}