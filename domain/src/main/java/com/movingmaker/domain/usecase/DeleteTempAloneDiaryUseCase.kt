package com.movingmaker.domain.usecase

import javax.inject.Inject

class DeleteTempAloneDiaryUseCase @Inject constructor(
) {
    suspend operator fun invoke(diaryId: Long) {}
}