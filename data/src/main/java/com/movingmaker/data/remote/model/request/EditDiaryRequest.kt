package com.movingmaker.data.remote.model.request

import com.movingmaker.data.remote.model.RemoteModel
import com.movingmaker.domain.model.request.EditDiaryModel
import kotlinx.serialization.Serializable

@Serializable
data class EditDiaryRequest(
    val title: String,
    val content: String,
    //tempYn api에서 제거될 예정
    val tempYn: Char
) : RemoteModel {
    override fun toDomainModel() = EditDiaryModel(
        title, content, tempYn
    )
}

fun EditDiaryModel.toDataModel() = EditDiaryRequest(
    title, content, tempYN
)