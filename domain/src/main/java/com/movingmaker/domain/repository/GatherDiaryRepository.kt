package com.movingmaker.domain.repository

import com.movingmaker.domain.model.NetworkResult
import com.movingmaker.domain.model.request.ReportCommentModel
import com.movingmaker.domain.model.response.BaseResponse
import com.movingmaker.domain.model.response.Diary
import kotlinx.coroutines.flow.Flow

interface GatherDiaryRepository {

    suspend fun getRemoteDiaries(date: String): NetworkResult<BaseResponse<MutableList<Diary>>>

    fun getMonthDiary(date: String): Flow<List<Diary>>

    suspend fun getAllDiary(): NetworkResult<BaseResponse<MutableList<Diary>>>

    suspend fun reportComment(reportCommentModel: ReportCommentModel): NetworkResult<BaseResponse<String>>

    suspend fun likeComment(commentId: Long): NetworkResult<BaseResponse<String>>

    suspend fun insertTempDiary(tempDiary: Diary)

    suspend fun deleteCommentDiary(commentDiary: Diary)

    suspend fun clearCommentDiaries()

    suspend fun insertCommentDiaries(commentDiaries: List<Diary>)
}