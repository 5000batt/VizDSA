package com.kjw.vizdsa.feature.array.domain.usecase

import com.kjw.vizdsa.core.domain.model.AlgorithmStep
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class InsertElementUseCase @Inject constructor() {
    operator fun invoke(array: Array<Int?>, targetIndex: Int, insertValue: Int) : Flow<AlgorithmStep> = flow {

        // 배열 초기화 여부 확인
        if (array.isEmpty()) {
            emit(AlgorithmStep.Error("먼저 배열을 초기화해주세요."))
            return@flow
        }

        // 인덱스 범위 초과 확인
        if (targetIndex < 0 || targetIndex >= array.size) {
            emit(AlgorithmStep.Error("인덱스가 배열 범위를 벗어났습니다."))
            return@flow
        }

        // 배열 가득 참 확인
        // 자료구조의 배열은 연속성을 가지므로 마지막 요소만 검사하여 O(1)로 처리
        if (array[array.size - 1] !=  null) {
            emit(AlgorithmStep.Error("배열이 가득 찼습니다."))
            return@flow
        }

        // 빈 공간(Hole) 건너뛰기 방지 로직
        // 현재 데이터 개수를 파악하여, 데이터가 없는 인덱스를 건너뛰고 삽입하는 것을 방지
        var currentDataCount = 0
        for (item in array) {
            if (item != null) currentDataCount++
            else break
        }

        if (targetIndex > currentDataCount) {
            emit(AlgorithmStep.Error("빈 공간을 건너뛰고 삽입할 수 없습니다. (최대 인덱스: $currentDataCount)"))
            return@flow
        }

        // Shift 연산 (뒤에서부터 한 칸씩 당겨오며 이동 명령 방출)
        for (i in (array.size - 2) downTo targetIndex) {
            if(array[i] != null) {
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