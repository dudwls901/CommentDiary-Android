package com.movingmaker.commentdiary.domain.usecase

import com.movingmaker.commentdiary.domain.model.UiState
import com.movingmaker.commentdiary.domain.model.mapUiState
import com.movingmaker.commentdiary.domain.model.toUiState
import com.movingmaker.commentdiary.domain.repository.MyDiaryRepository
import javax.inject.Inject

class DeleteDiaryUseCase @Inject constructor(
    private val myDiaryRepository: MyDiaryRepository,
) {
    suspend operator fun invoke(diaryId: Long): UiState<String> =
        myDiaryRepository.deleteDiary(diaryId).toUiState().mapUiState { it.result }

}