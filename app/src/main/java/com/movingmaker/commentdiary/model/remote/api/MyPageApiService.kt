package com.movingmaker.commentdiary.model.remote.api

import com.movingmaker.commentdiary.model.remote.Url
import com.movingmaker.commentdiary.model.remote.request.LogInRequest
import com.movingmaker.commentdiary.model.remote.response.IsSuccessResponse
import com.movingmaker.commentdiary.model.remote.response.LogInResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.Header
import retrofit2.http.Headers


interface MyPageApiService {
//    @Headers("Authorization: Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiI1IiwiYXV0aCI6IlJPTEVfVVNFUiIsImV4cCI6MTY0NjE0NDI5Nn0.24CJEkIR4MpPaTjMOCO8iUjozRV3KkVpkNsmn6odndt-rshS-IMgE8OacbqRL3aVLubgn8OqRySztwH6v_N3Yw")
    @DELETE(Url.SIGN)
    suspend fun signOut(): Response<IsSuccessResponse>
}