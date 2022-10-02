package com.movingmaker.commentdiary.domain.usecase

import com.movingmaker.commentdiary.domain.repository.ForSignUpRepository
import javax.inject.Inject

class SendEmailCodeUseCase @Inject constructor(
    private val forSignUpRepository: ForSignUpRepository,
) {
    suspend operator fun invoke(email: String) =
        forSignUpRepository.sendEmailCode(email)

}