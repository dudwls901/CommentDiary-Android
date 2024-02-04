package com.movingmaker.domain.usecase

import com.movingmaker.domain.model.UiState
import com.movingmaker.domain.model.response.MyInfo
import javax.inject.Inject

class GetMyPageUseCase @Inject constructor(
) {
    suspend operator fun invoke(): UiState<MyInfo> = UiState.Success(MyInfo("temp","temp",30.0,true))
}