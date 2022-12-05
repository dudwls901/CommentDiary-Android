package com.movingmaker.domain.model

enum class ErrorType {
    //네트워크 문제
    NETWORK,

    //요청 시간 초과
    TIMEOUT,
    SERVER_ERROR,

    //세션 만료
//    SESSION_EXPIRED,
    //알 수 없는 다른 문제
    UNKNOWN
}

fun ErrorType.getErrorMessage(): String = when (this) {
    ErrorType.NETWORK -> "네트워크 에러"
    ErrorType.TIMEOUT -> "요청 시간 초과"
    ErrorType.UNKNOWN -> "알 수 없는 문제 발생"
    ErrorType.SERVER_ERROR -> "서버 에러 발생"
}