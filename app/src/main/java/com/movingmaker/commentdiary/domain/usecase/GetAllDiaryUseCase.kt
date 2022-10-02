package com.movingmaker.commentdiary.domain.usecase

import com.movingmaker.commentdiary.domain.repository.GatherDiaryRepository
import javax.inject.Inject

class GetAllDiaryUseCase @Inject constructor(
    private val gatherDiaryRepository: GatherDiaryRepository,
) {
    suspend operator fun invoke() =
        gatherDiaryRepository.getAllDiary()

}