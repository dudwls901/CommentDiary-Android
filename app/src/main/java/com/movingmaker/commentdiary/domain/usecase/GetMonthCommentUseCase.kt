package com.movingmaker.commentdiary.domain.usecase

import com.movingmaker.commentdiary.domain.repository.MyPageRepository
import javax.inject.Inject

class GetMonthCommentUseCase @Inject constructor(
    private val myPageRepository: MyPageRepository,
) {
    suspend operator fun invoke(date: String) =
        myPageRepository.getMonthComment(date)
}