package com.kjw.vizdsa.feature.array.domain.usecase

import javax.inject.Inject

class UpdateElementUseCase @Inject constructor() {
    operator fun invoke(array: Array<Int?>, index: Int, newValue: Int): Result<Array<Int?>> {

        // 배열 존재 여부 확인
        if (array.isEmpty()) {
            return Result.failure(IllegalArgumentException("먼저 배열을 초기화해주세요."))
        }

        // 인덱스 범위 초과 확인 (Out of Bounds)
        if (index < 0 || index >= array.size) {
            return Result.failure(IllegalArgumentException("인덱스가 배열 범위를 벗어났습니다."))
        }

        array[index] = newValue

        return Result.success(array)
    }
}