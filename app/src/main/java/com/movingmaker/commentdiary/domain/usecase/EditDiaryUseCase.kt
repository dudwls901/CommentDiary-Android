package com.movingmaker.commentdiary.domain.usecase

import com.movingmaker.commentdiary.data.remote.request.EditDiaryRequest
import com.movingmaker.commentdiary.domain.repository.MyDiaryRepository
import javax.inject.Inject

class EditDiaryUseCase @Inject constructor(
    private val myDiaryRepository: MyDiaryRepository,
) {
    suspend operator fun invoke(diaryId: Long, editDiaryRequest: EditDiaryRequest) =
        myDiaryRepository.editDiary(diaryId, editDiaryRequest)

}