package com.movingmaker.domain.usecase

import com.movingmaker.domain.model.NetworkResult
import com.movingmaker.domain.model.UiState
import com.movingmaker.domain.model.mapUiState
import com.movingmaker.domain.model.request.EditDiaryModel
import com.movingmaker.domain.model.toUiState
import com.movingmaker.domain.repository.DiaryRepository
import javax.inject.Inject

class EditDiaryUseCase @Inject constructor(
    private val diaryRepository: DiaryRepository,
) {

    /**
     * 수정시 현재 달 일기 갱신 -> Room Flow에 collect 가능
     * */
    suspend operator fun invoke(
        diaryId: Long,
        date: String,
        editDiaryModel: EditDiaryModel
    ): UiState<String> {
        val editDiaryResult = diaryRepository.editDiary(diaryId, editDiaryModel)
        when(editDiaryResult){
            is NetworkResult.Success ->{
                diaryRepository.updatePeriodDiaries(date)
            }
            else ->{/*no-op*/}
        }
        return editDiaryResult.toUiState().mapUiState { it.result }
    }
}