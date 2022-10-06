package com.movingmaker.commentdiary.domain.usecase

import com.movingmaker.commentdiary.data.remote.request.EmailCodeCheckRequest
import com.movingmaker.commentdiary.domain.model.UiState
import com.movingmaker.commentdiary.domain.model.mapUiState
import com.movingmaker.commentdiary.domain.model.toUiState
import com.movingmaker.commentdiary.domain.repository.ForSignUpRepository
import javax.inject.Inject

class EmailCodeCheckUseCase @Inject constructor(
    private val forSignUpRepository: ForSignUpRepository,
) {
    suspend operator fun invoke(emailCodeCheckRequest: EmailCodeCheckRequest): UiState<Nothing> =
        forSignUpRepository.emailCodeCheck(emailCodeCheckRequest).toUiState()
            .mapUiState { it.result }

}