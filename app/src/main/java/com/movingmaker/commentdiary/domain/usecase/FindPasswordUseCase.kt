package com.movingmaker.commentdiary.domain.usecase

import com.movingmaker.commentdiary.domain.model.UiState
import com.movingmaker.commentdiary.domain.model.mapUiState
import com.movingmaker.commentdiary.domain.model.toUiState
import com.movingmaker.commentdiary.domain.repository.ForSignUpRepository
import javax.inject.Inject

class FindPasswordUseCase @Inject constructor(
    private val forSignUpRepository: ForSignUpRepository,
) {
    suspend operator fun invoke(email: String): UiState<String> {
        val a =forSignUpRepository.findPassword(email).toUiState().mapUiState { it.result }
        println(a)
        return forSignUpRepository.findPassword(email).toUiState().mapUiState { it.result }
    }

}