package com.kjw.vizdsa.feature.array.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kjw.vizdsa.core.domain.model.AlgorithmStep
import com.kjw.vizdsa.core.ui.util.parseIndexInput
import com.kjw.vizdsa.core.ui.util.parseSizeInput
import com.kjw.vizdsa.core.ui.util.parseValueInput
import com.kjw.vizdsa.feature.array.domain.usecase.AccessElementUseCase
import com.kjw.vizdsa.feature.array.domain.usecase.DeleteDynamicElementUseCase
import com.kjw.vizdsa.feature.array.domain.usecase.DeleteElementUseCase
import com.kjw.vizdsa.feature.array.domain.usecase.InitializeArrayUseCase
import com.kjw.vizdsa.feature.array.domain.usecase.InsertDynamicElementUseCase
import com.kjw.vizdsa.feature.array.domain.usecase.InsertElementUseCase
import com.kjw.vizdsa.feature.array.domain.usecase.LinearSearchUseCase
import com.kjw.vizdsa.feature.array.domain.usecase.TraverseArrayUseCase
import com.kjw.vizdsa.feature.array.domain.usecase.UpdateElementUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ArrayViewModel @Inject constructor(
    private val initializeArrayUseCase: InitializeArrayUseCase,
    private val accessElementUseCase: AccessElementUseCase,
    private val updateElementUseCase: UpdateElementUseCase,
    private val linearSearchUseCase: LinearSearchUseCase,
    private val traverseArrayUseCase: TraverseArrayUseCase,
    private val insertElementUseCase: InsertElementUseCase,
    private val insertDynamicElementUseCase: InsertDynamicElementUseCase,
    private val deleteElementUseCase: DeleteElementUseCase,
    private val deleteDynamicElementUseCase: DeleteDynamicElementUseCase
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
        _uiState.update { it.copy(
            operation = operation,
            sizeInput = "",
            valueInput = "",
            indexInput = "",
            highlightedIndex = null
        ) }
    }

    // 실행 버튼
    fun executeOperation() {
        when (_uiState.value.operation) {
            ArrayOperation.INITIALIZE -> executeInitialize()
            ArrayOperation.ACCESS_ELEMENT -> executeAccess()
            ArrayOperation.UPDATE_ELEMENT -> executeUpdate()
            ArrayOperation.LINEAR_SEARCH -> executeLinearSearch()
            ArrayOperation.TRAVERSE_ARRAY -> executeTraverse()
            ArrayOperation.INSERT_ELEMENT -> executeInsert()
            ArrayOperation.DELETE_ELEMENT -> executeDelete()
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

    // 에러 메세지 공용 함수
    private fun showError(message: String) {
        _uiState.update {
            it.copy(highlightedIndex = null, message = message)
        }
    }

    // 배열 초기화
    private fun executeInitialize() {
        val currentState = _uiState.value

        val parsedSize = parseSizeInput(
            input = currentState.sizeInput,
            onError = { showError(it) }
        ) ?: return

        val parsedValues = parseInputValues(currentState.valueInput)

        initializeArrayUseCase(parsedSize, parsedValues)
            .onSuccess { result ->
                _uiState.update {
                    it.copy(
                        highlightedIndex = null,
                        array = result.data,
                        message = result.message
                    )
                }
            }
            .onFailure { exception ->
                _uiState.update {
                    it.copy(
                        array = emptyArray(),
                        message = exception.message ?: "배열 초기화에 실패했습니다."
                    )
                }
            }

    }

    // 배열 요소 접근
    private fun executeAccess() {
        val currentState = _uiState.value

        val parsedIndex = parseIndexInput(
            input = currentState.indexInput,
            onError = { showError(it) }
        ) ?: return

        accessElementUseCase(currentState.array, parsedIndex)
            .onSuccess { result ->
                _uiState.update {
                    it.copy(
                        highlightedIndex = result.data,
                        message = result.message
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

    // 배열 요소 수정
    private fun executeUpdate() {
        val currentState = _uiState.value

        val parsedIndex = parseIndexInput(
            input = currentState.indexInput,
            onError = { showError(it) }
        ) ?: return

        val parsedValue = parseValueInput(
            input = currentState.valueInput,
            onError = { showError(it) }
        ) ?: return

        updateElementUseCase(currentState.array, parsedIndex, parsedValue)
            .onSuccess { result ->
                _uiState.update {
                    it.copy(
                        highlightedIndex = parsedIndex,
                        array = result.data,
                        message = result.message
                    )
                }
            }

            .onFailure { exception ->
                _uiState.update {
                    it.copy(
                        highlightedIndex = null,
                        message = exception.message ?: "수정에 실패했습니다."
                    )
                }
            }
    }

    // 선형 탐색(순차 검색)
    private fun executeLinearSearch() {
        val currentState = _uiState.value

        val parsedValue = parseValueInput(
            input = currentState.valueInput,
            onError = { showError(it) }
        ) ?: return

        viewModelScope.launch {
            linearSearchUseCase(currentState.array, parsedValue).collect { step ->
                handleAlgorithmStep(step)
            }
        }
    }

    // 배열 순회
    private fun executeTraverse() {
        val currentState = _uiState.value

        viewModelScope.launch {
            traverseArrayUseCase(currentState.array).collect { step ->
                handleAlgorithmStep(step)
            }
        }
    }

    private fun executeInsert() {
        val currentState = _uiState.value

        val parsedIndex = parseIndexInput(
            input = currentState.indexInput,
            onError = { showError(it) }
        ) ?: return

        val parsedValue = parseValueInput(
            input = currentState.valueInput,
            onError = { showError(it) }
        ) ?: return

        viewModelScope.launch {
            if (currentState.type == ArrayType.STATIC) {
                insertElementUseCase(currentState.array, parsedIndex, parsedValue).collect { step ->
                    handleAlgorithmStep(step)
                }
            } else if (currentState.type == ArrayType.DYNAMIC) {
                insertDynamicElementUseCase(currentState.array, parsedIndex, parsedValue).collect { step ->
                    handleAlgorithmStep(step)
                }
            }
        }
    }

    private fun executeDelete() {
        val currentState = _uiState.value

        val parsedIndex = parseIndexInput(
            input = currentState.indexInput,
            onError = { showError(it) }
        ) ?: return

        viewModelScope.launch {
            if (currentState.type == ArrayType.STATIC) {
                deleteElementUseCase(currentState.array, parsedIndex).collect { step ->
                    handleAlgorithmStep(step)
                }
            } else if (currentState.type == ArrayType.DYNAMIC) {
                deleteDynamicElementUseCase(currentState.array, parsedIndex).collect { step ->
                    handleAlgorithmStep(step)
                }
            }
        }
    }

    private fun handleAlgorithmStep(step: AlgorithmStep) {
        when (step) {
            is AlgorithmStep.Checking -> {
                _uiState.update {
                    it.copy(
                        highlightedIndex = step.index
                    )
                }
            }

            is AlgorithmStep.Error -> {
                _uiState.update {
                    it.copy(
                        highlightedIndex = null,
                        message = step.message
                    )
                }
            }

            is AlgorithmStep.Moved -> {
                // 기존 배열을 복제(clone)해서 새로운 객체로 만듦
                val newArray = _uiState.value.array.clone()

                // 데이터 이동
                newArray[step.toIndex] = newArray[step.fromIndex]

                // 기존 자리를 비워두어 빈 공간이 밀려나는 시각적 효과
                newArray[step.fromIndex] = null

                _uiState.update {
                    it.copy(
                        array = newArray,
                        highlightedIndex = step.toIndex
                    )
                }
            }

            is AlgorithmStep.Resized -> {
                val newArray = _uiState.value.array.clone().copyOf(step.newSize)

                _uiState.update {
                    it.copy(
                        array = newArray,
                        highlightedIndex = null
                    )
                }
            }

            is AlgorithmStep.ValueUpdated -> {
                // 기존 배열을 복제(clone)해서 새로운 객체로 만듦
                val newArray = _uiState.value.array.clone()

                // 타겟 인덱스에 새로운 값 덮어쓰기
                newArray[step.index] = step.newValue

                _uiState.update {
                    it.copy(
                        array = newArray,
                        highlightedIndex = step.index,
                        message = step.message
                    )
                }
            }

            is AlgorithmStep.Found -> {
                _uiState.update {
                    it.copy(
                        highlightedIndex = step.index,
                        message = step.message

                    )
                }
            }

            is AlgorithmStep.NotFound -> {
                _uiState.update {
                    it.copy(
                        highlightedIndex = null,
                        message = step.message
                    )
                }
            }

            is AlgorithmStep.Done -> {
                _uiState.update {
                    it.copy(
                        highlightedIndex = null,
                        message = step.message
                    )
                }
            }
        }
    }
}