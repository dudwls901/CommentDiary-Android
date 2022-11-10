package com.movingmaker.commentdiary.domain.usecase

import com.movingmaker.commentdiary.data.model.MyInfo
import com.movingmaker.commentdiary.domain.model.UiState
import com.movingmaker.commentdiary.domain.model.mapUiState
import com.movingmaker.commentdiary.domain.model.toUiState
import com.movingmaker.commentdiary.domain.repository.MyPageRepository
import javax.inject.Inject

class GetMyPageUseCase @Inject constructor(
    private val myPageRepository: MyPageRepository,
) {
    suspend operator fun invoke(): UiState<MyInfo> =
        myPageRepository.getMyPage().toUiState().mapUiState { it.result }
}