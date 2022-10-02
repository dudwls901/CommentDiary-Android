package com.movingmaker.commentdiary.domain.usecase

import com.movingmaker.commentdiary.domain.repository.LogOutRepository
import javax.inject.Inject

class LogOutUseCase @Inject constructor(
    private val logOutRepository: LogOutRepository,
) {
    suspend operator fun invoke() =
        logOutRepository.logOut()

}