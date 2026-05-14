package com.kjw.vizdsa.feature.array.domain.usecase

import com.kjw.vizdsa.core.domain.model.AlgorithmStep
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class InsertDynamicElementUseCase @Inject constructor() {
    operator fun invoke(array: Array<Int?>, targetIndex: Int, insertValue: Int): Flow<AlgorithmStep> = flow {
        val originalSize = array.size
        var currentDataCount = 0

        for (item in array) {
            if (item != null) currentDataCount++
            else break
        }

        // 배열 초기화 여부 확인
        if (array.isEmpty()) {
            emit(AlgorithmStep.Error("먼저 배열을 초기화해주세요."))
            return@flow
        }

        // 인덱스 범위 초과 확인
        if (targetIndex < 0) {
            emit(AlgorithmStep.Error("인덱스가 배열 범위를 벗어났습니다."))
            return@flow
        }

        // 빈 공간(Hole) 건너뛰기 방지 로직
        if (targetIndex > currentDataCount) {
            emit(AlgorithmStep.Error("빈 공간을 건너뛰고 삽입할 수 없습니다. (최대 인덱스: $currentDataCount)"))
            return@flow
        }

        if (array[array.size - 1] != null) {
            // 배열이 가득 찼을 때 (배열의 size를 2배 증가 명령 방출)
            emit(AlgorithmStep.Resized(originalSize * 2))
            delay(500L)
        }

        // Shift 연산 (뒤에서부터 한 칸씩 당겨오며 이동 명령 방출)
        for (i in currentDataCount - 1 downTo targetIndex) {
            if (array[i] != null) {
                emit(AlgorithmStep.Moved(i, i + 1))
                delay(500L)
            }
        }

        // 새로운 값 삽입 명령 방출
        emit(AlgorithmStep.ValueUpdated(targetIndex, insertValue))

        delay(500L)
        emit(AlgorithmStep.Done)
    }
}