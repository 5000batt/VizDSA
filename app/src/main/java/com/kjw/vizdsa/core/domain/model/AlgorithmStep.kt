package com.kjw.vizdsa.core.domain.model

/**
 * 알고리즘 시각화 과정에서 발생하는 각 단계(상태)와 이벤트를 정의합니다.
 * ViewModel은 이 Step들을 구독(collect)하여 UI 상태와 애니메이션을 업데이트합니다.
 */
sealed class AlgorithmStep {

    // ==========================================
    // 알고리즘 생명주기 및 결과 (Lifecycle & Result)
    // ==========================================

    /**
     * 알고리즘의 모든 시각화 단계가 성공적으로 완료된 상태입니다.
     * @param message 완료 시 UI 하단에 표시할 결과 메시지
     */
    data class Done(val message: String) : AlgorithmStep()

    /**
     * 알고리즘 실행 전/중 유효성 검사 실패 등의 오류가 발생한 상태입니다.
     * @param message 사용자에게 보여줄 에러 메시지
     */
    data class Error(val message: String) : AlgorithmStep()


    // ==========================================
    // 데이터 탐색 및 확인 (Search & Access)
    // ==========================================

    /**
     * 탐색 알고리즘 등에서 특정 인덱스를 탐색(확인) 중인 상태입니다. (애니메이션 하이라이트용)
     * @param index 현재 확인 중인 대상 인덱스
     */
    data class Checking(val index: Int) : AlgorithmStep()

    /**
     * 탐색 알고리즘 등에서 원하는 타겟 데이터를 찾은 상태입니다.
     * @param index 찾은 데이터가 위치한 인덱스
     * @param message 탐색 성공 시 표시할 메시지
     */
    data class Found(val index: Int, val message: String) : AlgorithmStep()

    /**
     * 탐색 알고리즘에서 배열 끝까지 순회했으나 타겟 데이터를 찾지 못한 상태입니다.
     * @param message 탐색 실패 시 표시할 메시지
     */
    data class NotFound(val message: String) : AlgorithmStep()


    // ==========================================
    // 데이터 조작 (Modification)
    // ==========================================

    /**
     * 데이터가 한 인덱스에서 다른 인덱스로 이동(Shift)하는 상태입니다. 주로 삽입/삭제 시 빈 공간을 메울 때 사용됩니다.
     * @param fromIndex 이동을 시작하는 원래 인덱스
     * @param toIndex 이동이 완료되는 목적지 인덱스
     */
    data class Moved(val fromIndex: Int, val toIndex: Int) : AlgorithmStep()

    /**
     * 특정 인덱스의 값이 새롭게 추가되거나, 수정되거나, 삭제(null)된 상태입니다.
     * @param index 값이 변경된 타겟 인덱스
     * @param newValue 변경된 새로운 값 (삭제 시에는 null이 전달됨)
     * @param message 값 변경 후 표시할 상황 설명 메시지
     */
    data class ValueUpdated(val index: Int, val newValue: Int?, val message: String) : AlgorithmStep()

    /**
     * 동적 배열 등에서 요소가 가득 차거나 너무 많이 비어서 배열의 전체 크기를 재조정(Expand/Shrink)하는 상태입니다.
     * @param newSize 변경될 새로운 배열의 전체 크기 (Capacity)
     */
    data class Resized(val newSize: Int) : AlgorithmStep()
}