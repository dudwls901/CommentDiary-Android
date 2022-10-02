package com.movingmaker.commentdiary.domain.usecase

import com.movingmaker.commentdiary.domain.repository.ReceivedDiaryRepository
import javax.inject.Inject

class GetReceivedDiaryUseCase @Inject constructor(
    private val receivedDiaryRepository: ReceivedDiaryRepository,
) {
    suspend operator fun invoke(date: String) =
        receivedDiaryRepository.getReceivedDiary(date)

}