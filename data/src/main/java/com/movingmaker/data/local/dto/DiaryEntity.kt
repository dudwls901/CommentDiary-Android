package com.movingmaker.data.local.dto

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.movingmaker.data.util.COMMENT_DIARY_TABLE
import com.movingmaker.domain.model.response.Comment

@Entity(tableName = COMMENT_DIARY_TABLE)
data class DiaryEntity(
    @PrimaryKey val id: Long,
    val userId: Long,
    val title: String,
    val content: String,
    val date: String,
    val deliveryYN: Char,
    val commentList: MutableList<Comment>
) {
//    fun toDomainModel(): CoronaCenter = CoronaCenter(
//        id = id,
//        address = address,
//        centerName = centerName,
//        centerType = parseToCenterTypeEnum(centerType),
//        facilityName = facilityName,
//        lat = lat.toDouble(),
//        lng = lng.toDouble(),
//        phoneNumber = phoneNumber,
//        updatedAt = updatedAt
//    )
}

//fun CoronaCenter.toEntity(): CoronaCenterEntity = CoronaCenterEntity(
//    id = id,
//    address = address,
//    centerName = centerName,
//    centerType = centerType.type,
//    facilityName = facilityName,
//    lat = lat.toString(),
//    lng = lng.toString(),
//    phoneNumber = phoneNumber,
//    updatedAt = updatedAt
//)