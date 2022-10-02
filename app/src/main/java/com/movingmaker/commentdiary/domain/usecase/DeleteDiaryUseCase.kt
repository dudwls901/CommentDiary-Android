package com.movingmaker.commentdiary.domain.usecase

import com.movingmaker.commentdiary.domain.repository.MyDiaryRepository
import javax.inject.Inject

class DeleteDiaryUseCase @Inject constructor(
    private val myDiaryRepository: MyDiaryRepository,
) {
    suspend operator fun invoke(diaryId: Long) =
        myDiaryRepository.deleteDiary(diaryId)

}