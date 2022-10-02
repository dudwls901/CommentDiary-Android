package com.movingmaker.commentdiary.domain.usecase

import com.movingmaker.commentdiary.data.remote.request.EmailCodeCheckRequest
import com.movingmaker.commentdiary.domain.repository.ForSignUpRepository
import javax.inject.Inject

class EmailCodeCheckUseCase @Inject constructor(
    private val forSignUpRepository: ForSignUpRepository,
) {
    suspend operator fun invoke(emailCodeCheckRequest: EmailCodeCheckRequest) =
        forSignUpRepository.emailCodeCheck(emailCodeCheckRequest)

}