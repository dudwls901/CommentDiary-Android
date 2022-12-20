package com.movingmaker.data.remote.model.request

import com.movingmaker.data.remote.model.RemoteModel
import com.movingmaker.domain.model.request.SaveDiaryModel
import kotlinx.serialization.Serializable

@Serializable
data class SaveDiaryRequest(
    val title: String,
    val content: String,
    val date: String,
    val deliveryYn: Char,
    //tempYn api에서 제거될 예정
    val tempYn: Char = 'N',
) : RemoteModel {
    override fun toDomainModel() = SaveDiaryModel(
        title, content, date, deliveryYn, tempYn
    )
}

fun SaveDiaryModel.toDataModel() = SaveDiaryRequest(
    title, content, date, deliveryYN, tempYN
)