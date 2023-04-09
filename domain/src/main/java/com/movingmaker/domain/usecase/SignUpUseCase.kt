package com.movingmaker.domain.usecase

import com.movingmaker.domain.model.UiState
import com.movingmaker.domain.model.mapUiState
import com.movingmaker.domain.model.request.SignUpModel
import com.movingmaker.domain.model.toUiState
import com.movingmaker.domain.repository.MemberRepository
import javax.inject.Inject

class SignUpUseCase @Inject constructor(
    private val memberRepository: MemberRepository,
) {
    suspend operator fun invoke(signUpModel: SignUpModel): UiState<String> =
        memberRepository.signUp(signUpModel).toUiState().mapUiState { it.result }
}