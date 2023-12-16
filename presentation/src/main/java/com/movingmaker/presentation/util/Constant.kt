package com.movingmaker.presentation.util

const val KAKAO = "KAKAO"
const val EMAIL = "EMAIL"
const val EMPTY_TOKEN = "Empty Token"
const val EMPTY_TYPE = "Empty Type"
const val DEVICE_TOKEN = "deviceToken"
const val ACCESS_TOKEN = "accessToken"
const val REFRESH_TOKEN = "refreshToken"
const val ACCESS_TOKEN_EXPIRES_IN = "accessTokenExpiresIn"
const val USER_ID = "userId"
const val EMPTY_USER = -221L
const val LOGIN_TYPE = "loginType"
const val TERMS_URL =
    "https://glittery-silk-987.notion.site/fb0c6c765a7a411c9362dc8d102c95e0"
const val POLICY_URL = "https://www.notion.so/59a704f6702f4382b9398fa3b4a0d780"
const val DIARY_CONTENT_MINIMUM_LENGTH = 100

enum class FRAGMENT_NAME {
    //main
    MY_PAGE, MY_ACCOUNT, SIGN_OUT, TERMS, SENDED_COMMENT_LIST, CHANGE_PASSWORD,
    PUSHALARM_ONOFF, RECEIVED_DIARY, CALENDAR_WITH_DIARY, GATHER_DIARY, WRITE_DIARY, COMMENT_DIARY_DETAIL, ALONE_DIARY_DETAIL,

    //onboarding
    LOGIN, SIGNUP_TERMS, KAKAO_TERMS
}

enum class DIARY_TYPE {
    ALONE_DIARY, COMMENT_DIARY
}
