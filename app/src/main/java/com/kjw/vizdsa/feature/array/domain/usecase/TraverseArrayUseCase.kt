package com.kjw.vizdsa.feature.array.domain.usecase

import com.kjw.vizdsa.core.domain.model.AlgorithmStep
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class TraverseArrayUseCase @Inject constructor() {
    operator fun invoke(array: Array<Int?>): Flow<AlgorithmStep> = flow {

        // 배열 존재 여부 확인
        if (array.isEmpty()) {
            emit(AlgorithmStep.Error("먼저 배열을 초기화해주세요."))
            return@flow
        }

        for (i in array.indices) {
            emit(AlgorithmStep.Checking(i))

            delay(500L)
        }

        emit(AlgorithmStep.Done)
    }
}