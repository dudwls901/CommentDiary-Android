package com.movingmaker.data.remote.api

import com.movingmaker.data.remote.model.request.ChangePasswordRequest
import com.movingmaker.data.remote.model.request.EditDiaryRequest
import com.movingmaker.data.remote.model.request.ReportCommentRequest
import com.movingmaker.data.remote.model.request.ReportDiaryRequest
import com.movingmaker.data.remote.model.request.SaveCommentRequest
import com.movingmaker.data.remote.model.request.SaveDiaryRequest
import com.movingmaker.data.remote.model.response.BaseResponse
import com.movingmaker.data.remote.model.response.CommentResponse
import com.movingmaker.data.remote.model.response.DiaryIdResponse
import com.movingmaker.data.remote.model.response.DiaryResponse
import com.movingmaker.data.remote.model.response.MyInfoResponse
import com.movingmaker.data.remote.model.response.ReceivedDiaryResponse
import com.movingmaker.data.remote.model.response.SavedCommentResponse
import com.movingmaker.data.util.COMMENTS
import com.movingmaker.data.util.COMMENTS_ALL
import com.movingmaker.data.util.COMMENTS_LIKE
import com.movingmaker.data.util.DELIVERY
import com.movingmaker.data.util.DIARY
import com.movingmaker.data.util.DIARY_ALL
import com.movingmaker.data.util.DIARY_MY
import com.movingmaker.data.util.MEMBERS
import com.movingmaker.data.util.PUSH
import com.movingmaker.data.util.REPORT_COMMENT
import com.movingmaker.data.util.REPORT_DIARY
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface BearerApiService{

    /*
   * MEMBER
   * */

    @PATCH(PUSH)
    suspend fun patchCommentPushState(): Response<BaseResponse<Map<String, Boolean>>>

    @DELETE(MEMBERS)
    suspend fun signOut(): Response<BaseResponse<String>>

    @GET(MEMBERS)
    suspend fun getMyPage(): Response<BaseResponse<MyInfoResponse>>

    @PATCH(MEMBERS)
    suspend fun changePassword(@Body changePasswordRequest: ChangePasswordRequest): Response<BaseResponse<String>>

    /*
    * COMMENT
    * */

    @POST(COMMENTS)
    suspend fun saveComment(@Body saveCommentRequest: SaveCommentRequest): Response<BaseResponse<SavedCommentResponse>>

    @PATCH("$COMMENTS_LIKE/{commentId}")
    suspend fun likeComment(@Path("commentId") commentId: Long): Response<BaseResponse<String>>

    @GET(COMMENTS_ALL)
    suspend fun getAllComments(): Response<BaseResponse<List<CommentResponse>>>

    //ymd format이면 하루 코멘트, ym format이면 한 달 코멘트
    @GET(COMMENTS)
    suspend fun getPeriodComments(@Query("date") date: String): Response<BaseResponse<List<CommentResponse>>>

    /*
    * DIARY
    * */

    @POST(DIARY)
    suspend fun saveDiary(@Body saveDiaryRequest: SaveDiaryRequest): Response<BaseResponse<DiaryIdResponse>>

    @PATCH("${DIARY}/{diaryId}")
    suspend fun editDiary(
        @Path("diaryId") diaryId: Long,
        @Body editDiaryRequest: EditDiaryRequest
    ): Response<BaseResponse<String>>

    @GET(DIARY_ALL)
    suspend fun getAllDiaries(): Response<BaseResponse<List<DiaryResponse>>>

    /**
     * date = ym인 경우 한 달 일기, date = ymd인 경우 하루 일기
     * */
    @GET(DIARY_MY)
    suspend fun getPeriodDiaries(@Query("date") date: String): Response<BaseResponse<List<DiaryResponse>>>

    @DELETE("${DIARY}/{diaryId}")
    suspend fun deleteDiary(@Path("diaryId") diaryId: Long): Response<BaseResponse<String>>

    @GET("${DIARY}/{diaryId}")
    suspend fun getDiary(@Path("diaryId") diaryId: Long): Response<BaseResponse<DiaryResponse>>


    /*
    * DELIVERY
    * */

    @GET(DELIVERY)
    suspend fun getReceivedDiary(@Query("date") date: String): Response<BaseResponse<ReceivedDiaryResponse>>

    /*
    * REPORT
    * */

    @POST(REPORT_COMMENT)
    suspend fun reportComment(@Body reportCommentRequest: ReportCommentRequest): Response<BaseResponse<String>>

    @POST(REPORT_DIARY)
    suspend fun reportDiary(@Body reportDiaryRequest: ReportDiaryRequest): Response<BaseResponse<String>>

}