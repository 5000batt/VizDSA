package com.kjw.vizdsa.feature.array.domain.usecase

import com.kjw.vizdsa.core.domain.model.AlgorithmStep
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class DeleteDynamicElementUseCase @Inject constructor() {
    operator fun invoke(array: Array<Int?>, targetIndex: Int): Flow<AlgorithmStep> = flow {
        val originalSize = array.size
        var currentDataCount = 0
        for (item in array) {
           if (item != null) currentDataCount++
           else break
        }

        // 빈 배열 예외 처리
        if (array.isEmpty()) {
            emit(AlgorithmStep.Error("먼저 배열을 초기화해주세요."))
            return@flow
        }

        // 인덱스 범위 초과 확인
        if (targetIndex < 0 || targetIndex >= array.size) {
            emit(AlgorithmStep.Error("인덱스가 배열 범위를 벗어났습니다."))
            return@flow
        }

        // 삭제할 요소가 없는 경우
        if (array[targetIndex] == null) {
            emit(AlgorithmStep.Error("해당 인덱스는 이미 비어있습니다."))
            return@flow
        }

        // 삭제 작업 및 방출
        emit(AlgorithmStep.ValueUpdated(targetIndex, null))
        delay(500L)

        // 시프트 작업 및 방출
        for (i in targetIndex until currentDataCount - 1) {
            if (array[i + 1] != null) {
                emit(AlgorithmStep.Moved(i + 1, i))
                delay(500L)
            }
        }

        // 배열 축소 및 방출
        val MIN_CAPACITY = 4 // 유지할 최소 크기

        if ((currentDataCount - 1) <= originalSize / 4 && originalSize > MIN_CAPACITY) {
            val newSize = maxOf(originalSize / 2, MIN_CAPACITY)
            emit(AlgorithmStep.Resized(newSize))
            delay(500L)
        }

        emit(AlgorithmStep.Done)
    }
}