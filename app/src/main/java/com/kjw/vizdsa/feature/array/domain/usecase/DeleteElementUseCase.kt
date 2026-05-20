package com.kjw.vizdsa.feature.array.domain.usecase

import com.kjw.vizdsa.core.domain.model.AlgorithmStep
import com.kjw.vizdsa.core.domain.util.validateIsNotEmpty
import com.kjw.vizdsa.core.domain.util.validateElementExists
import com.kjw.vizdsa.core.domain.util.validateIndexBounds
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class DeleteElementUseCase @Inject constructor() {
    operator fun invoke(array: Array<Int?>, targetIndex: Int) : Flow<AlgorithmStep> = flow {

        // 유효성 검사
        array.validateIsNotEmpty()?.let { emit(it); return@flow }
        array.validateIndexBounds(targetIndex)?.let { emit(it); return@flow }
        array.validateElementExists(targetIndex)?.let { emit(it); return@flow }

        // 삭제 작업 및 방출
        emit(AlgorithmStep.ValueUpdated(targetIndex, null))
        delay(500L)

        // 시프트 작업 및 방출
        for (i in targetIndex until array.size - 1) {
            if (array[i + 1] != null) {
                emit(AlgorithmStep.Moved(i + 1, i))
                delay(500L)
            }
        }

        emit(AlgorithmStep.Done)
    }
}