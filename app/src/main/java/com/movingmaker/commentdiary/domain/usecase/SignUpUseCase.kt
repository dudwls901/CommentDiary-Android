package com.movingmaker.commentdiary.domain.usecase

import com.movingmaker.commentdiary.data.remote.request.SignUpRequest
import com.movingmaker.commentdiary.domain.repository.ForSignUpRepository
import javax.inject.Inject

class SignUpUseCase @Inject constructor(
    private val forSignUpRepository: ForSignUpRepository,
) {
    suspend operator fun invoke(signUpRequest: SignUpRequest) =
        forSignUpRepository.signUp(signUpRequest)

}