package com.movingmaker.data.local.dto

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import com.movingmaker.data.util.COMMENT_DIARY_TABLE
import com.movingmaker.domain.model.response.Comment
import com.movingmaker.domain.model.response.Diary
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.encodeToJsonElement

@Entity(
    tableName = COMMENT_DIARY_TABLE,
//    edit TempDiary 중복 제거
    indices = [
        Index(value = ["userId", "date"], unique = true)
    ]
)
data class DiaryEntity(
    @PrimaryKey(autoGenerate = true)
    val tempDiaryId: Long = 0L,
    val id: Long,
    val userId: Long,
    val title: String,
    val content: String,
    val date: String,
    val deliveryYN: Char,
    val commentList: MutableList<Comment>
) {
    fun toDomainModel(): Diary = Diary(
        id = id,
        userId = userId,
        title = title,
        content = content,
        date = date,
        deliveryYN = deliveryYN,
        commentList = commentList
    )
}

fun Diary.toEntity(): DiaryEntity = DiaryEntity(
    id = id,
    userId = userId,
    title = title,
    content = content,
    date = date,
    deliveryYN = deliveryYN,
    commentList = commentList
)

class CommentListTypeConverter {

    private val json = Json {
        isLenient = true
        ignoreUnknownKeys = true
        coerceInputValues = true
    }

    @TypeConverter
    fun jsonToList(value: String): List<Comment> {
        return json.decodeFromString(ListSerializer(Comment.serializer()), value)
    }

    @TypeConverter
    fun listToJson(type: List<Comment>): String {
        return json.encodeToJsonElement(type).toString()
    }
}