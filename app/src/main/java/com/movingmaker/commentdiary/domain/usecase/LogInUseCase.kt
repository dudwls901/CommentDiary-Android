package com.movingmaker.commentdiary.domain.usecase

import com.movingmaker.commentdiary.data.remote.request.LogInRequest
import com.movingmaker.commentdiary.domain.repository.ForSignUpRepository
import javax.inject.Inject

class LogInUseCase @Inject constructor(
    private val forSignUpRepository: ForSignUpRepository,
) {
    suspend operator fun invoke(logInRequest: LogInRequest) =
        forSignUpRepository.logIn(logInRequest)

}