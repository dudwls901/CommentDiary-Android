package com.movingmaker.domain.usecase

import com.movingmaker.domain.model.response.Diary
import javax.inject.Inject

class DeleteTempDiaryUseCase @Inject constructor(
) {
    suspend operator fun invoke(commentDiary: Diary): Int = 0
}