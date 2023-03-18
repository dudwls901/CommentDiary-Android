package com.movingmaker.domain.usecase

import com.movingmaker.domain.model.NetworkResult
import com.movingmaker.domain.model.UiState
import com.movingmaker.domain.model.mapUiState
import com.movingmaker.domain.model.toUiState
import com.movingmaker.domain.repository.DiaryRepository
import javax.inject.Inject

class DeleteDiaryUseCase @Inject constructor(
    private val diaryRepository: DiaryRepository,
) {
    /**
     * 삭제시 현재 달 일기 갱신 -> Room Flow에 collect 가능
     * */
    suspend operator fun invoke(
        diaryId: Long,
        date: String
    ): UiState<String> {
        val deleteDiaryResult = diaryRepository.deleteDiary(diaryId)
        when(deleteDiaryResult){
            is NetworkResult.Success->{
                diaryRepository.updatePeriodDiaries(
                    date
                )
            }
            else -> {/*no-op*/}
        }
        return deleteDiaryResult.toUiState().mapUiState { it.result }
    }

}