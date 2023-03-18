package com.movingmaker.domain.usecase

import com.movingmaker.domain.model.UiState
import com.movingmaker.domain.model.mapUiState
import com.movingmaker.domain.model.toUiState
import com.movingmaker.domain.repository.MemberRepository
import javax.inject.Inject

class FindPasswordUseCase @Inject constructor(
    private val memberRepository: MemberRepository,
) {
    suspend operator fun invoke(email: String): UiState<String> {
        return memberRepository.findPassword(email).toUiState().mapUiState { it.result }
    }
}