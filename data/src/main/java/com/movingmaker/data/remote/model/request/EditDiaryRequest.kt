package com.movingmaker.data.remote.model.request

import com.movingmaker.domain.model.request.EditDiaryModel
import kotlinx.serialization.Serializable

@Serializable
data class EditDiaryRequest(
    val title: String,
    val content: String,
    val deliveryYn: Char,
    //tempYn api에서 제거될 예정
    val tempYn: Char
)

fun EditDiaryModel.toDataModel() = EditDiaryRequest(
    title, content, deliveryYN, tempYN
)