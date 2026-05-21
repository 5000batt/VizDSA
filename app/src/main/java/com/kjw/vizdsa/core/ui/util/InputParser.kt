package com.kjw.vizdsa.core.ui.util

/**
 * 인덱스 입력값을 Int로 변환합니다. 실패 시 에러를 띄우고 null을 반환합니다.
 */
fun parseIndexInput(
    input: String,
    onError: (String) -> Unit
): Int? {
    val parsed = input.toIntOrNull()
    if (parsed == null) onError("올바른 인덱스(숫자)를 입력해주세요.")
    return parsed
}

/**
 * 값 입력값을 Int로 변환합니다. 실패 시 에러를 띄우고 null을 반환합니다.
 */
fun parseValueInput(
    input: String,
    onError: (String) -> Unit
): Int? {
    val parsed = input.toIntOrNull()
    if (parsed == null) onError("올바른 값(숫자)을 입력해주세요.")
    return parsed
}

/**
 * 사이즈 입력값을 Int로 변환합니다. 실패 시 에러를 띄우고 null을 반환합니다.
 */
fun parseSizeInput(
    input: String,
    onError: (String) -> Unit
): Int? {
    val parsed = input.toIntOrNull()
    if (parsed == null) onError("올바른 사이즈(숫자)를 입력해주세요.")
    return parsed
}