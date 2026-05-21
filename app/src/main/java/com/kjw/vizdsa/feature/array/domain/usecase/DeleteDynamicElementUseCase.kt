package com.kjw.vizdsa.feature.array.domain.usecase

import com.kjw.vizdsa.core.domain.model.AlgorithmStep
import com.kjw.vizdsa.core.domain.util.validateIsNotEmpty
import com.kjw.vizdsa.core.domain.util.validateElementExists
import com.kjw.vizdsa.core.domain.util.validateIndexBounds
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

        // 유효성 검사
        array.validateIsNotEmpty()?.let { emit(it); return@flow }
        array.validateIndexBounds(targetIndex)?.let { emit(it); return@flow }
        array.validateElementExists(targetIndex)?.let { emit(it); return@flow }

        // 삭제 작업 및 방출
        emit(AlgorithmStep.ValueUpdated(targetIndex, null, "인덱스 [$targetIndex]의 값이 삭제되었습니다."))
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

        emit(AlgorithmStep.Done("동적 배열 삭제가 완료되었습니다."))
    }
}