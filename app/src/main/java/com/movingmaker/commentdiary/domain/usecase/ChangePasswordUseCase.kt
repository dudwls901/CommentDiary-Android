package com.movingmaker.commentdiary.domain.usecase

import com.movingmaker.commentdiary.data.remote.request.ChangePasswordRequest
import com.movingmaker.commentdiary.domain.repository.MyPageRepository
import javax.inject.Inject

class ChangePasswordUseCase @Inject constructor(
    private val myPageRepository: MyPageRepository,
) {
    suspend operator fun invoke(changePasswordRequest: ChangePasswordRequest) =
        myPageRepository.changePassword(changePasswordRequest)

}