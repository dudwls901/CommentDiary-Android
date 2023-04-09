package com.movingmaker.domain.usecase

import com.movingmaker.domain.repository.DiaryRepository
import javax.inject.Inject

class ClearCachedDiariesUseCase @Inject constructor(
    private val diaryRepository: DiaryRepository
) {
    suspend operator fun invoke(){
        diaryRepository.clearCachedDiaries()
    }
}