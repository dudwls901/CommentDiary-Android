package com.movingmaker.commentdiary.common.util

object RetrofitHeaderCondition {
    const val BEARER = "bearer"
    const val ONE_HEADER = "oneHeader"
    const val TWO_HEADER = "twoHeader"
    const val NO_HEADER ="noHeader"
}
object Constant{
    const val KAKAO = "KAKAO"
    const val EMAIL = "EMAIL"
    const val SUCCESS_CODE = 200
}

object Url {
    //    const val CODA_BASE_URL = "https://comment-diary.shop/api/v1/"
    // https://comment-diary.shop/api/v1/ 운영 서버
    // http://jwyang.shop:8080/api/v1/ 테스트 서버
//    const val BASE_URL = "https://comment-diary.shop/api/v1/"
    const val BASE_URL = "http://jwyang.shop:8080/api/v1/"
    const val SEND_EMAIL_CODE = "email"
    const val EMAIL_CODE_CHECK = "email/confirm"
    const val MEMBERS = "members"
    const val SIGNUP = "members/sign-up"
    const val AUTH_SIGNUP = "/oauth-sign-up"
    const val LOG_IN = "members/login"
    const val AUTH_LOG_IN = "members/auth-login"
    const val FIND_PW = "email/password"
    const val MONTH_DIARY = "diary/main"
    const val REISSUE = "members/reissue"
    const val LOG_OUT = "/logout"
    const val DIARY = "diary"
    const val MY = "/my"
    const val ALL = "/all"
    const val DELIVERY ="delivery"
    const val REPORT ="report/"
    const val COMMENT = "comment"
    const val LIKE = "like"
    const val PUSH = "/push"

    const val TERMS_URL =
        "https://glittery-silk-987.notion.site/fb0c6c765a7a411c9362dc8d102c95e0"
    const val POLICY_URL = "https://www.notion.so/59a704f6702f4382b9398fa3b4a0d780"
}

enum class FRAGMENT_NAME{
    //main
    MY_PAGE,MY_ACCOUNT,SIGN_OUT,TERMS,SENDED_COMMENT_LIST,CHANGE_PASSWORD,
    PUSHALARM_ONOFF,RECEIVED_DIARY,CALENDAR_WITH_DIARY,GATHER_DIARY,WRITE_DIARY,COMMENT_DIARY_DETAIL,ALONE_DIARY_DETAIL,

    //onboarding
    LOGIN, LOGIN_BEFORE, FIND_PASSWORD, SIGNUP_PASSWORD, SIGNUP_CODE, SIGNUP_EMAIL, SIGNUP_SUCCESS, SIGNUP_TERMS, KAKAO_TERMS
}

enum class DIARY_TYPE{
    ALONE_DIARY,COMMENT_DIARY
}

enum class RESPONSE_STATE{
    OKAY,
    FAIL
}