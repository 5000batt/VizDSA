package com.kjw.vizdsa.feature.array.presentation

import androidx.lifecycle.ViewModel
import com.kjw.vizdsa.feature.array.domain.usecase.InitializeArrayUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class ArrayViewModel @Inject constructor(
    private val initializeArrayUseCase: InitializeArrayUseCase
) : ViewModel() {

    // 상태 관리
    private val _uiState = MutableStateFlow(ArrayUiState())
    val uiState: StateFlow<ArrayUiState> = _uiState.asStateFlow()

    // UI 입력 처리
    // 배열 크기 입력
    fun updateSizeInput(size: String) {
        _uiState.update { it.copy(sizeInput = size) }
    }

    // 배열 요소 입력
    fun updateValueInput(value: String) {
        _uiState.update { it.copy(valueInput = value) }
    }

    // 배열 동작 입력
    fun updateOperation(operation: ArrayOperation) {
        _uiState.update { it.copy(operation = operation) }
    }

    // 실행 버튼
    fun executeOperation() {
        when (_uiState.value.operation) {
            ArrayOperation.INITIALIZE -> executeInitialize()
            // 나중에 INSERT 등 추가 예쩡
            else -> {}
        }
    }

    // 배열 초기화
    private fun executeInitialize() {
        val currentState = _uiState.value
        val parsedSize = currentState.sizeInput.toIntOrNull() ?: 0
        val valueInput = currentState.valueInput

        initializeArrayUseCase(parsedSize, valueInput)
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
}