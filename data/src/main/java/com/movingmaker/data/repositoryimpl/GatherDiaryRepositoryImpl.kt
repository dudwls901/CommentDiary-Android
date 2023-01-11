package com.movingmaker.data.repositoryimpl

import com.movingmaker.data.local.datasource.CommentDiaryLocalDataSource
import com.movingmaker.data.local.dto.toEntity
import com.movingmaker.data.remote.datasource.GatherDiaryDataSource
import com.movingmaker.data.remote.model.request.toDataModel
import com.movingmaker.domain.model.ErrorType
import com.movingmaker.domain.model.NetworkResult
import com.movingmaker.domain.model.request.ReportCommentModel
import com.movingmaker.domain.model.response.BaseResponse
import com.movingmaker.domain.model.response.Diary
import com.movingmaker.domain.repository.GatherDiaryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GatherDiaryRepositoryImpl @Inject constructor(
    private val gatherDiaryDataSource: GatherDiaryDataSource,
    private val commentDiaryLocalDataSource: CommentDiaryLocalDataSource
) : GatherDiaryRepository {

    /**
     * remote 일기 가져오기
     * */
    override suspend fun getRemoteDiaries(date: String): NetworkResult<BaseResponse<MutableList<Diary>>> {
        return try {
            val remoteDiaries = gatherDiaryDataSource.getMonthDiary(date)
            when (remoteDiaries) {
                is NetworkResult.Success -> {
                    clearCommentDiaries()
                    insertCommentDiaries(remoteDiaries.data.result)
                }
                else -> {
                    /*no op*/
                }
            }
            remoteDiaries
        } catch (e: Exception) {
            NetworkResult.Exception(ErrorType.UNKNOWN)
        }
    }

    /**
     * 리모트에서 받아온 일기 로컬에 넣기 (캐싱)
     * */
    override suspend fun insertCommentDiaries(commentDiaries: List<Diary>) {
        commentDiaryLocalDataSource.insertCommentDiaries(*commentDiaries.map { it.toEntity() }
            .toTypedArray())
    }

    /**
     * 화면에 보여질 일기 가져오기
     * */
    override fun getMonthDiary(date: String): Flow<List<Diary>> {
        return commentDiaryLocalDataSource.getPeriodDiaries(date).map { diaryList ->
            diaryList.map {
                it.toDomainModel()
            }
        }
    }

    /**
     * 임시 저장 일기 로컬에 넣기
     * */
    override suspend fun insertTempDiary(tempDiary: Diary) {
        commentDiaryLocalDataSource.insertTempDiary(tempDiary.toEntity())
    }

    /**
     * 로컬 일기 삭제하기
     * */
    override suspend fun deleteCommentDiary(commentDiary: Diary) {
        commentDiaryLocalDataSource.deleteCommentDiary(commentDiary.toEntity())
    }


    /**
     * 로컬 일기 모두 삭제하기
     * */
    override suspend fun clearCommentDiaries() {
        commentDiaryLocalDataSource.clearCommentDiaries()
    }

    /**
     * 리모트 모든 일기 가져오기
     * */
    override suspend fun getAllDiary(): NetworkResult<BaseResponse<MutableList<Diary>>> =
        gatherDiaryDataSource.getAllDiary()

    /**
     * 코멘트 신고하기
     * */
    override suspend fun reportComment(reportCommentModel: ReportCommentModel): NetworkResult<BaseResponse<String>> =
        gatherDiaryDataSource.reportComment(reportCommentModel.toDataModel())


    /**
     * 코멘트 좋아요 누르기
     * */
    override suspend fun likeComment(commentId: Long): NetworkResult<BaseResponse<String>> =
        gatherDiaryDataSource.likeComment(commentId)

}