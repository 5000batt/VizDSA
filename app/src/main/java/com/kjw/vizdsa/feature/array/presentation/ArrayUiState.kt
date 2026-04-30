package com.kjw.vizdsa.feature.array.presentation

enum class ArrayType {
    STATIC, // 정적
    DYNAMIC // 동적
}

enum class ArrayOperation {
    INITIALIZE,      // 초기화
    ACCESS,          // 접근
    UPDATE,          // 수정
    LINEAR_SEARCH,   // 선형 탐색
    TRAVERSE,        // 전체 순회
    INSERT,          // 삽입
    DELETE           // 삭제
}

data class ArrayUiState(
    val array: List<Int?> = emptyList(),                            // 현재 배열 상태
    val sizeInput: String = "",                                     // 배열 크기
    val type: ArrayType? = ArrayType.STATIC,                        // 배열 타입
    val operation: ArrayOperation? = ArrayOperation.INITIALIZE,     // 배열 동작
    val indexInput: String = "",                                    // 배열 요소 인덱스
    val valueInput: String = "",                                    // 배열 요소 값
    val message: String = "",                                       // 안내 메시지
    val highlightedIndex: Int? = null                               // 탐색 중이거나 수정/삽인된 인덱스를 강조
)
