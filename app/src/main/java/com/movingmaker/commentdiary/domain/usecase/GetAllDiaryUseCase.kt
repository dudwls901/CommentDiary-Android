package com.movingmaker.commentdiary.domain.usecase

import com.movingmaker.commentdiary.data.model.Diary
import com.movingmaker.commentdiary.domain.model.UiState
import com.movingmaker.commentdiary.domain.model.mapUiState
import com.movingmaker.commentdiary.domain.model.toUiState
import com.movingmaker.commentdiary.domain.repository.GatherDiaryRepository
import javax.inject.Inject

class GetAllDiaryUseCase @Inject constructor(
    private val gatherDiaryRepository: GatherDiaryRepository,
) {
    suspend operator fun invoke(): UiState<MutableList<Diary>> =
        gatherDiaryRepository.getAllDiary().toUiState().mapUiState { it.result }

}