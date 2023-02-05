package com.movingmaker.presentation.view.main.mydiary

import com.movingmaker.domain.model.response.Diary

sealed interface DiaryState {
    //StateFlow를 위한 initType
    object Init: DiaryState
    //혼자 일기
    data class AloneDiary(val diary: Diary) : DiaryState

    //일기 없음
    data class EmptyDiary(val selectedDate: String) : DiaryState

    //일기 선택하지 않은 경우 (날짜 포커스 해제된 경우)
    object NoneSelectedDiary : DiaryState

    //미래 날짜
    object SelectedFutureDiary: DiaryState

    //코멘트 일기
    sealed interface CommentDiary : DiaryState {
        val diary: Diary

        //일기 작성 후 오늘 + 다음 날
        data class HaveNotCommentInTime(override val diary: Diary) : CommentDiary

        //일기 작성 후 다음 날 코멘트 있는데 열 수 있는 경우
        data class HaveCommentInTimeCanOpen(override val diary: Diary) : CommentDiary

        //일기 작성 후 다음 날 코멘트 있는데 못 여는 경우 (내가 다른 사람의 일기에 코멘트를 달아야만 본인에게 달린 코멘트 열람 가능)
        data class HaveCommentInTimeCannotOpen(override val diary: Diary) : CommentDiary

        //일기 작성 이틀 후
        data class HaveNotCommentOutTime(override val diary: Diary) : CommentDiary
        data class HaveCommentOutTimeCanOpen(override val diary: Diary) : CommentDiary
        //일기 작성 후 다음 날 내가 다른 사람의 일기에 코멘트를 달아야만 본인에게 달린 코멘트 열람 가능
        data class HaveCommentOutTimeCannotOpen(override val diary: Diary) : CommentDiary

        //임시 저장 일기
        //오늘
        data class TempDiaryInTime(override val diary: Diary) : CommentDiary

        //이전 날부터
        data class TempDiaryOutTime(override val diary: Diary) : CommentDiary
    }

}