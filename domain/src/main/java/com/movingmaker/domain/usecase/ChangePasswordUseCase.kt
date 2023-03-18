package com.movingmaker.domain.usecase

import com.movingmaker.domain.model.UiState
import com.movingmaker.domain.model.mapUiState
import com.movingmaker.domain.model.request.ChangePasswordModel
import com.movingmaker.domain.model.toUiState
import com.movingmaker.domain.repository.MemberRepository
import javax.inject.Inject

class ChangePasswordUseCase @Inject constructor(
    private val memberRepository: MemberRepository
) {
    suspend operator fun invoke(changePasswordModel: ChangePasswordModel): UiState<String> =
        memberRepository.changePassword(changePasswordModel).toUiState().mapUiState { it.result }

}