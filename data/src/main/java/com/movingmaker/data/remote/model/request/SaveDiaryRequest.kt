package com.movingmaker.data.remote.model.request

import com.movingmaker.domain.model.request.SaveDiaryModel
import kotlinx.serialization.Serializable

@Serializable
data class SaveDiaryRequest(
    val title: String,
    val content: String,
    val date: String,
    val deliveryYn: Char,
    //tempYn api에서 제거될 예정
    val tempYn: Char,
)

fun SaveDiaryModel.toDataModel() = SaveDiaryRequest(
    title, content, date, deliveryYN, tempYN
)