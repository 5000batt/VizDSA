package com.kjw.vizdsa.core.domain.util

import com.kjw.vizdsa.core.domain.model.AlgorithmStep

/**
 * 배열 초기화시 배열의 크기가 0보다 작은지 확인합니다.
 */
fun Int.validateSize(): AlgorithmStep.Error? {
    if (this <= 0) return AlgorithmStep.Error("배열의 크기는 1 이상이어야 합니다.")
    return null
}

/**
 * 배열 초기화시 배열의 최대크기를 제한합니다.
 */
fun Int.validateMaxSize(): AlgorithmStep.Error? {
    if (this > 100) return AlgorithmStep.Error("시각화를 위해 배열 크기는 100 이하로 입력해주세요.")
    return null
}

/**
 * 배열 초기화시 초기 값이 배열의 크기보다 큰지 확인합니다.
 */
fun List<Int>.validateInitialValuesSize(size: Int): AlgorithmStep.Error? {
    if (this.size > size) return AlgorithmStep.Error("초기 값이 배열의 크기를 넘었습니다.")
    return null
}

/**
 * 배열이 초기화되어 있는지(비어있지 않은지) 검사합니다.
 * @return 비어있다면 Error 반환, 정상이라면 null 반환
 */
fun Array<Int?>.validateIsNotEmpty(): AlgorithmStep.Error? {
    if (this.isEmpty()) return AlgorithmStep.Error("먼저 배열을 초기화해주세요.")
    return null
}

/**
 * 타겟 인덱스가 배열의 유효한 범위(0 ~ size-1) 내에 있는지 검사합니다.
 * @param index 검사할 인덱스
 */
fun Array<Int?>.validateIndexBounds(index: Int): AlgorithmStep.Error? {
    if (index < 0 || index >= this.size) return AlgorithmStep.Error("인덱스가 배열 범위를 벗어났습니다.")
    return null
}

/**
 * 정적 배열이 가득 찼는지 검사합니다.
 * (배열의 연속성을 보장하므로 마지막 요소만 O(1)로 확인)
 */
fun Array<Int?>.validateIsNotFull(): AlgorithmStep.Error? {
    if (this[this.size - 1] != null) return AlgorithmStep.Error("배열이 가득 찼습니다.")
    return null
}

/**
 * 배열 내에 데이터가 존재하는지 검사합니다. (주로 삭제, 접근 시 사용)
 * @param index 확인할 인덱스
 */
fun Array<Int?>.validateElementExists(index: Int): AlgorithmStep.Error? {
    if (this[index] == null) return AlgorithmStep.Error("해당 인덱스는 비어있습니다.")
    return null
}

/**
 * 빈 공간(Hole)을 건너뛰고 삽입하려는지 검사합니다.
 * @param index 삽입하려는 타겟 인덱스
 */
fun Array<Int?>.validateNoHoles(index: Int, currentDataCount: Int): AlgorithmStep.Error? {
    if (index > currentDataCount) return AlgorithmStep.Error("빈 공간을 건너뛰고 삽입할 수 없습니다. (최대 인덱스: $currentDataCount)")
    return null
}

