package com.movingmaker.domain.usecase

import com.movingmaker.domain.model.UiState
import com.movingmaker.domain.model.mapUiState
import com.movingmaker.domain.model.request.EmailCodeCheckModel
import com.movingmaker.domain.model.toUiState
import com.movingmaker.domain.repository.MemberRepository
import javax.inject.Inject

class CheckEmailCodeUseCase @Inject constructor(
    private val memberRepository: MemberRepository,
) {
    suspend operator fun invoke(emailCodeCheckModel: EmailCodeCheckModel): UiState<String> =
        memberRepository.checkEmailCode(emailCodeCheckModel).toUiState()
            .mapUiState { it.result }
}