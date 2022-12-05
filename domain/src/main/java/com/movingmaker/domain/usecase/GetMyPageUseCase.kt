package com.movingmaker.domain.usecase

import com.movingmaker.domain.model.UiState
import com.movingmaker.domain.model.mapUiState
import com.movingmaker.domain.model.response.MyInfo
import com.movingmaker.domain.model.toUiState
import com.movingmaker.domain.repository.MyPageRepository
import javax.inject.Inject

class GetMyPageUseCase @Inject constructor(
    private val myPageRepository: MyPageRepository,
) {
    suspend operator fun invoke(): UiState<MyInfo> =
        myPageRepository.getMyPage().toUiState().mapUiState { it.result }
}