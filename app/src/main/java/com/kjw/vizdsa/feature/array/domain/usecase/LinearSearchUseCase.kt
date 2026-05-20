package com.kjw.vizdsa.feature.array.domain.usecase

import com.kjw.vizdsa.core.domain.model.AlgorithmStep
import com.kjw.vizdsa.core.domain.util.validateIsNotEmpty
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class LinearSearchUseCase @Inject constructor() {
    operator fun invoke(array: Array<Int?>, target: Int): Flow<AlgorithmStep> = flow {

        // 유효성 검사
        array.validateIsNotEmpty()?.let { emit(it); return@flow }

        for (i in array.indices) {
            if (array[i] == null) break

            emit(AlgorithmStep.Checking(i))

            delay(500L)

            if (array[i] == target) {
                emit(AlgorithmStep.Found(i))
                return@flow
            }
        }

        emit(AlgorithmStep.NotFound)

        /*
        withIndex() 방식
        for ((index,value) in array.withIndex()) {
            if (value == target) {
                return Result.success(index)
            }
        }
        */

        /*
        코틀린 확장 함수 사용

        값이 배열에 없음
        if (array.none { it == target }) {
            return Result.failure(IllegalArgumentException("${target}은 배열에 존재하지 않습니다."))
        }

        return Result.success(array.indexOf(target))

        array.none {} 함수는 내부적으로 배열을 처음부터 하나씩 뒤지면서 조건에 맞는게 없는지 검사
        array.indexOf() 함수는 내부적으로 배열을 처음부터 뒤지면서 값의 위치를 찾음
        즉 위 로직은 array를 두 번 반복

        val index = array.indexOf(target)

        if (index == -1) {
            return Result.failure(IllegalArgumentException("${target}은(는) 배열에 존재하지 않습니다."))
        }

        return Result.success(index)

        indexOf() 함수는 값을 찾지 못하면 -1을 반환하기 때문에 위와 같이 작성하면 한 번으로 가능
        */
    }
}