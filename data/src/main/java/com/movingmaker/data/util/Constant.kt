package com.movingmaker.data.util


// https://comment-diary.shop/api/v1/ 운영 서버
// http://jwyang.shop:8080/api/v1/ 테스트 서버
const val BASE_URL = "https://comment-diary.shop/api/v1/"

//    const val BASE_URL = "http://jwyang.shop:8080/api/v1/"
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
const val DELIVERY = "delivery"
const val REPORT = "report/"
const val COMMENT = "comment"
const val LIKE = "like"
const val PUSH = "/push"

const val MESSAGE_KEY = "message"
const val ERROR_KEY = "error"
