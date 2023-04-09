package com.movingmaker.domain.usecase

import com.movingmaker.domain.model.UiState
import com.movingmaker.domain.model.mapUiState
import com.movingmaker.domain.model.request.LogInModel
import com.movingmaker.domain.model.response.AuthTokens
import com.movingmaker.domain.model.toUiState
import com.movingmaker.domain.repository.MemberRepository
import javax.inject.Inject

class LogInUseCase @Inject constructor(
    private val memberRepository: MemberRepository,
) {
    suspend operator fun invoke(logInModel: LogInModel): UiState<AuthTokens> =
        memberRepository.logIn(logInModel).toUiState().mapUiState { it.result }

}