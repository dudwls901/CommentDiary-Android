package com.movingmaker.commentdiary.domain.usecase

import com.movingmaker.commentdiary.data.remote.request.SaveDiaryRequest
import com.movingmaker.commentdiary.domain.repository.MyDiaryRepository
import javax.inject.Inject

class SaveDiaryUseCase @Inject constructor(
    private val myDiaryRepository: MyDiaryRepository,
) {
    suspend operator fun invoke(saveDiaryRequest: SaveDiaryRequest) =
        myDiaryRepository.saveDiary(saveDiaryRequest)

}