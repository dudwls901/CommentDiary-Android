package com.movingmaker.data.util


// https://comment-diary.shop/api/v1/ 운영 서버
// http://jwyang.shop:8080/api/v1/ 테스트 서버
const val BASE_URL = "https://comment-diary.store/api/v1/"

//    const val BASE_URL = "http://jwyang.shop:8080/api/v1/"
const val EMAIL = "email"
const val EMAIL_CODE_CHECK = "${EMAIL}/confirm"
const val MEMBERS = "members"
const val SIGNUP = "${MEMBERS}/sign-up"
const val AUTH_SIGNUP = "${MEMBERS}/oauth-sign-up"
const val LOG_IN = "${MEMBERS}/email-login"
const val SOCIAL_LOG_IN = "${MEMBERS}/social-login"
const val FIND_PW = "${EMAIL}/password"
const val REISSUE = "${MEMBERS}/reissue"
const val LOG_OUT = "${MEMBERS}/logout"
const val DIARY = "diary"
const val DIARY_MY = "${DIARY}/my"
const val DIARY_ALL = "${DIARY_MY}/all"
const val COMMENT = "comment"
const val COMMENTS = "comments"
const val COMMENTS_ALL = "${COMMENTS}/all"
const val COMMENTS_LIKE = "${COMMENTS}/like"
const val PUSH = "${MEMBERS}/push"
const val DELIVERY = "delivery"
const val REPORT = "report"
const val REPORT_COMMENT = "${REPORT}/${COMMENT}"
const val REPORT_DIARY = "${REPORT}/${DIARY}"

const val DB_NAME = "diary_database"
const val DIARY_TABLE = "diary_table"