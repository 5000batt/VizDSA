package com.kjw.vizdsa.feature.array.domain.usecase

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

sealed class SearchStep {
    data class Error(val message: String) : SearchStep()
    data class Checking(val index: Int) : SearchStep()
    data class Found(val index: Int) : SearchStep()
    object NotFound : SearchStep()
}

class LinearSearchUseCase @Inject constructor() {
    operator fun invoke(array: Array<Int?>, target: Int): Flow<SearchStep> = flow {

        // 배열 존재 여부 확인
        if (array.isEmpty()) {
            emit(SearchStep.Error("먼저 배열을 초기화해주세요."))
            return@flow
        }

        for (i in array.indices) {
            emit(SearchStep.Checking(i))

            delay(500L)

            if (array[i] == target) {
                emit(SearchStep.Found(i))
                return@flow
            }
        }

        emit(SearchStep.NotFound)

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