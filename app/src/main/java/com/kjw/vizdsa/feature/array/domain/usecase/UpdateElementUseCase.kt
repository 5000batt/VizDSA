package com.kjw.vizdsa.feature.array.domain.usecase

import javax.inject.Inject

class UpdateElementUseCase @Inject constructor() {
    operator fun invoke(array: List<Int?>, index: Int, newValue: Int): Result<List<Int?>> {

        // 배열 존재 여부 확인
        if (array.isEmpty()) {
            return Result.failure(IllegalArgumentException("먼저 배열을 초기화해주세요."))
        }

        // 인덱스 범위 초과 확인 (Out of Bounds)
        if (index < 0 || index >= array.size) {
            return Result.failure(IllegalArgumentException("인덱스가 배열 범위를 벗어났습니다."))
        }

        /*
        실제 자료구조에서는 0(1)로 해당 메모리만 덮어씌지만,
        Jetpack Compose가 상태 변화를 감지(Recomposition)할 수 있도록
        새로운 배열 객체를 복사하여 반환합니다.
        */
        val updatedArray = array.toMutableList()
        updatedArray[index] = newValue

        return Result.success(updatedArray)
    }
}