package com.movingmaker.commentdiary.domain.usecase

import com.movingmaker.commentdiary.data.remote.request.SaveDiaryRequest
import com.movingmaker.commentdiary.domain.model.UiState
import com.movingmaker.commentdiary.domain.model.mapUiState
import com.movingmaker.commentdiary.domain.model.toUiState
import com.movingmaker.commentdiary.domain.repository.MyDiaryRepository
import javax.inject.Inject

class SaveDiaryUseCase @Inject constructor(
    private val myDiaryRepository: MyDiaryRepository,
) {
    suspend operator fun invoke(saveDiaryRequest: SaveDiaryRequest): UiState<Long> =
        myDiaryRepository.saveDiary(saveDiaryRequest).toUiState().mapUiState { it.result }

}