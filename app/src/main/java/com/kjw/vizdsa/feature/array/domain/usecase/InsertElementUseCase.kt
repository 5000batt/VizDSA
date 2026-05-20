package com.kjw.vizdsa.feature.array.domain.usecase

import com.kjw.vizdsa.core.domain.model.AlgorithmStep
import com.kjw.vizdsa.core.domain.util.validateIsNotEmpty
import com.kjw.vizdsa.core.domain.util.validateIsNotFull
import com.kjw.vizdsa.core.domain.util.validateIndexBounds
import com.kjw.vizdsa.core.domain.util.validateNoHoles
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class InsertElementUseCase @Inject constructor() {
    operator fun invoke(array: Array<Int?>, targetIndex: Int, insertValue: Int) : Flow<AlgorithmStep> = flow {

        var currentDataCount = 0
        for (item in array) {
            if (item != null) currentDataCount++
            else break
        }

        // 유효성 검사
        array.validateIsNotEmpty()?.let { emit(it); return@flow }
        array.validateIndexBounds(targetIndex)?.let { emit(it); return@flow }
        array.validateNoHoles(targetIndex, currentDataCount)?.let { emit(it); return@flow }
        array.validateIsNotFull()?.let { emit(it); return@flow }

        // Shift 연산 (뒤에서부터 한 칸씩 당겨오며 이동 명령 방출)
        for (i in (array.size - 2) downTo targetIndex) {
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