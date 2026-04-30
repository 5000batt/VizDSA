package com.kjw.vizdsa.feature.array.presentation

import androidx.lifecycle.ViewModel
import com.kjw.vizdsa.feature.array.domain.usecase.AccessElementUseCase
import com.kjw.vizdsa.feature.array.domain.usecase.InitializeArrayUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class ArrayViewModel @Inject constructor(
    private val initializeArrayUseCase: InitializeArrayUseCase,
    private val accessElementUseCase: AccessElementUseCase,
) : ViewModel() {

    // 상태 관리
    private val _uiState = MutableStateFlow(ArrayUiState())
    val uiState: StateFlow<ArrayUiState> = _uiState.asStateFlow()

    // UI 입력 처리
    // 배열 타입 선택
    fun updateArrayType(type: ArrayType) {
        _uiState.update { it.copy(type = type) }
    }

    // 배열 크기 입력
    fun updateSizeInput(size: String) {
        _uiState.update { it.copy(sizeInput = size) }
    }

    // 배열 요소 입력
    fun updateValueInput(value: String) {
        _uiState.update { it.copy(valueInput = value) }
    }

    // 배열 인덱스 입력
    fun updateIndexInput(index: String) {
        _uiState.update { it.copy(indexInput = index) }
    }

    // 배열 동작 입력
    fun updateOperation(operation: ArrayOperation) {
        _uiState.update { it.copy(operation = operation) }
    }

    // 실행 버튼
    fun executeOperation() {
        when (_uiState.value.operation) {
            ArrayOperation.INITIALIZE -> executeInitialize()
            ArrayOperation.ACCESS_ELEMENTAL -> executeUpdate()
            // 나중에 INSERT 등 추가 예쩡
            else -> {}
        }
    }

    // 메세지 초기화
    fun clearMessage() {
        _uiState.update { it.copy(message = "") }
    }

    // 리셋
    fun resetState() {
        _uiState.value = ArrayUiState()
    }

    // 문자열을 List<Int>로 변환하는 헬퍼 함수
    private fun parseInputValues(input: String): List<Int> {
        if (input.isBlank()) return emptyList()

        return try {
            input.split(",").map { it.trim().toInt() }
        } catch (e: NumberFormatException) {
            emptyList()
        }
    }

    // 배열 초기화
    private fun executeInitialize() {
        val currentState = _uiState.value
        val parsedSize = currentState.sizeInput.toIntOrNull() ?: 0
        val parsedValues = parseInputValues(currentState.valueInput)

        initializeArrayUseCase(parsedSize, parsedValues)
            .onSuccess { initializedArray ->
                _uiState.update {
                    it.copy(
                        array = initializedArray,
                        message = "배열을 초기화했습니다."
                    )
                }
            }
            .onFailure { exception ->
                _uiState.update {
                    it.copy(
                        array = emptyList(),
                        message = exception.message ?: "배열 초기화에 실패했습니다."
                    )
                }
            }

    }

    // 배열 요소 접근
    private fun executeUpdate() {
        val currentState = _uiState.value
        val parsedIndex = currentState.indexInput.toIntOrNull()

        if (parsedIndex == null) {
            _uiState.update {
                it.copy(
                    highlightedIndex = null,
                    message = "올바른 인덱스(숫자)를 입력해주세요."
                )
            }
            return
        }

        accessElementUseCase(currentState.array, parsedIndex)
            .onSuccess { (index, value) ->
                _uiState.update {
                    it.copy(
                        highlightedIndex = index,
                        message = if (value != null) {
                            "인덱스 [$index]에 접근했습니다. 값: $value"
                        } else {
                            "인덱스 [$index]에 접근했지만, 값이 비어있습니다 (null)."
                        }
                    )
                }
            }
            .onFailure { exception ->
                _uiState.update {
                    it.copy(
                        highlightedIndex = null,
                        message = exception.message ?: "접근에 실패했습니다."
                    )
                }
            }
    }
}