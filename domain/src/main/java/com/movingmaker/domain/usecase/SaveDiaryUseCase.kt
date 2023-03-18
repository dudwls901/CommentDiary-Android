package com.movingmaker.domain.usecase

import com.movingmaker.domain.model.NetworkResult
import com.movingmaker.domain.model.UiState
import com.movingmaker.domain.model.mapUiState
import com.movingmaker.domain.model.request.SaveDiaryModel
import com.movingmaker.domain.model.response.DiaryId
import com.movingmaker.domain.model.toUiState
import com.movingmaker.domain.repository.DiaryRepository
import javax.inject.Inject

class SaveDiaryUseCase @Inject constructor(
    private val diaryRepository: DiaryRepository
) {
    /**
     * 저장시 현재 달 일기 갱신 -> Room Flow에 collect 가능
     * */
    suspend operator fun invoke(saveDiaryModel: SaveDiaryModel): UiState<DiaryId> {
        val saveDiaryResult = diaryRepository.saveDiary(saveDiaryModel)
        when (saveDiaryResult) {
            is NetworkResult.Success -> {
                diaryRepository.updatePeriodDiaries(saveDiaryModel.date)
            }
            else -> { /*no-op*/
            }
        }
        return saveDiaryResult.toUiState().mapUiState {
            it.result
        }
    }
}