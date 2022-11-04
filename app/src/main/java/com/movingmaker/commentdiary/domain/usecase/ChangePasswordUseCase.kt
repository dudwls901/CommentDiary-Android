package com.movingmaker.commentdiary.domain.usecase

import com.movingmaker.commentdiary.data.remote.request.ChangePasswordRequest
import com.movingmaker.commentdiary.domain.model.UiState
import com.movingmaker.commentdiary.domain.model.mapUiState
import com.movingmaker.commentdiary.domain.model.toUiState
import com.movingmaker.commentdiary.domain.repository.MyPageRepository
import javax.inject.Inject

class ChangePasswordUseCase @Inject constructor(
    private val myPageRepository: MyPageRepository,
) {
    suspend operator fun invoke(changePasswordRequest: ChangePasswordRequest): UiState<String> =
        myPageRepository.changePassword(changePasswordRequest).toUiState().mapUiState { it.result }

}