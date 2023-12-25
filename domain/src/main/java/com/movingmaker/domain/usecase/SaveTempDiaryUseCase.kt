package com.movingmaker.domain.usecase

import com.movingmaker.domain.model.response.Diary
import javax.inject.Inject

class SaveTempDiaryUseCase @Inject constructor(
) {
    suspend operator fun invoke(tempDiary: Diary) {}
}