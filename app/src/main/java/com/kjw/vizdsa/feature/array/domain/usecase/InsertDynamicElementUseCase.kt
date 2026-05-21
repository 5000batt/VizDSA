package com.kjw.vizdsa.feature.array.domain.usecase

import com.kjw.vizdsa.core.domain.model.AlgorithmStep
import com.kjw.vizdsa.core.domain.util.validateIsNotEmpty
import com.kjw.vizdsa.core.domain.util.validateNoHoles
import com.kjw.vizdsa.core.domain.util.validateIndexBounds
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

        // 유효성 검사
        array.validateIsNotEmpty()?.let { emit(it); return@flow }
        array.validateIndexBounds(targetIndex)?.let { emit(it); return@flow }
        array.validateNoHoles(targetIndex, currentDataCount)?.let { emit(it); return@flow }

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
        emit(AlgorithmStep.ValueUpdated(targetIndex, insertValue, "인덱스 [${targetIndex}]에 ${insertValue}를 삽입했습니다."))

        delay(500L)
        emit(AlgorithmStep.Done("동적 배열 삽입이 완료되었습니다."))
    }
}