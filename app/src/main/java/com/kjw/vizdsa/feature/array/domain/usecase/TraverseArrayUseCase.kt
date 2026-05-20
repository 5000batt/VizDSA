package com.kjw.vizdsa.feature.array.domain.usecase

import com.kjw.vizdsa.core.domain.model.AlgorithmStep
import com.kjw.vizdsa.core.domain.util.validateIsNotEmpty
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class TraverseArrayUseCase @Inject constructor() {
    operator fun invoke(array: Array<Int?>): Flow<AlgorithmStep> = flow {

        // 유효성 검사
        array.validateIsNotEmpty()?.let { emit(it); return@flow }

        for (i in array.indices) {
            if (array[i] == null) break

            emit(AlgorithmStep.Checking(i))

            delay(500L)
        }

        emit(AlgorithmStep.Done)
    }
}