package com.movingmaker.domain.usecase

import com.movingmaker.domain.model.response.Diary
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetAllDiariesFlowUseCase @Inject constructor(
) {
    operator fun invoke(): Flow<List<Diary>> = flow { emptyList<Diary>() }
}