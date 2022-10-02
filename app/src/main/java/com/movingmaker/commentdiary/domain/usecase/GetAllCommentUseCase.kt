package com.movingmaker.commentdiary.domain.usecase

import com.movingmaker.commentdiary.domain.repository.MyPageRepository
import javax.inject.Inject

class GetAllCommentUseCase @Inject constructor(
    private val myPageRepository: MyPageRepository,
) {
    suspend operator fun invoke() =
        myPageRepository.getAllComment()

}