package com.movingmaker.commentdiary.domain.usecase

import com.movingmaker.commentdiary.data.remote.request.SignUpRequest
import com.movingmaker.commentdiary.domain.model.UiState
import com.movingmaker.commentdiary.domain.model.mapUiState
import com.movingmaker.commentdiary.domain.model.toUiState
import com.movingmaker.commentdiary.domain.repository.ForSignUpRepository
import javax.inject.Inject

class SignUpUseCase @Inject constructor(
    private val forSignUpRepository: ForSignUpRepository,
) {
    suspend operator fun invoke(signUpRequest: SignUpRequest): UiState<String> =
        forSignUpRepository.signUp(signUpRequest).toUiState().mapUiState { it.result }

}