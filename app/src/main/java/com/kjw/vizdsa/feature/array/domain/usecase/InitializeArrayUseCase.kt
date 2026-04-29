package com.kjw.vizdsa.feature.array.domain.usecase

import javax.inject.Inject

class InitializeArrayUseCase @Inject constructor() {
    operator fun invoke(size: Int, valueInput: String): Result<List<Int?>> {

        // 유효성 검사
        if (size <= 0) {
            return Result.failure(IllegalArgumentException("배열의 크기는 1 이상이어야 합니다."))
        }

        // 입력값 파싱
        val parsedValues = parseInputValues(valueInput)

        // 배열 생성 로직
        val newArray = if (parsedValues.isEmpty()) {
            // 값이 없거나 파싱 실패 시: 0~99 랜덤 값으로 size만큼 채움
            List(size) { (0..99).random() }
        } else {
            // 정상 입력 시: 입력된 값으로 채우고, size보다 입력이 적으면 나머지는 null로 채움
            List(size) { index ->
                if (index < parsedValues.size) parsedValues[index] else null
            }
        }

        return Result.success(newArray)
    }

    // 문자열을 List<Int>로 변환하는 헬퍼 함수
    private fun parseInputValues(input: String): List<Int> {
        if (input.isBlank()) return emptyList()

        return try {
            input.split(",").map { it.trim().toInt() }
        } catch (e: NumberFormatException) {
            emptyList()
        }
    }
}