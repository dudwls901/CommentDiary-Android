package com.movingmaker.domain.model.response

/*
* userId == -1 전송된 일기
* userId == preference.userId 나의 임시 저장 일기
* */
data class Diary(
    val id: Long,
    val userId: Long = -1,
    val title: String,
    val content: String,
    val date: String,
    val deliveryYN: Char,
    val commentList: MutableList<Comment>
) {
    companion object {
        fun buildTempDiary(
            userId: Long,
            title: String,
            content: String,
            date: String,
        ) = Diary(
            id = -1L,
            userId = userId,
            title = title,
            content = content,
            date = date,
            deliveryYN = 'Y',
            commentList = emptyList<Comment>().toMutableList()
        )
    }
}
