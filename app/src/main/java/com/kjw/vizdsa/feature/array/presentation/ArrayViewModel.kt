package com.kjw.vizdsa.feature.array.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kjw.vizdsa.core.domain.model.AlgorithmStep
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

    // 배열 초기화
    private fun executeInitialize() {
        val currentState = _uiState.value
        val parsedSize = currentState.sizeInput.toIntOrNull() ?: 0
        val parsedValues = parseInputValues(currentState.valueInput)

        initializeArrayUseCase(parsedSize, parsedValues)
            .onSuccess { initializedArray ->
                _uiState.update {
                    it.copy(
                        highlightedIndex = null,
                        array = initializedArray,
                        message = "배열을 초기화했습니다."
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

    // 배열 요소 수정
    private fun executeUpdate() {
        val currentState = _uiState.value
        val parsedIndex = currentState.indexInput.toIntOrNull()
        val parsedValue = currentState.valueInput.toIntOrNull()

        if (parsedIndex == null) {
            _uiState.update {
                it.copy(
                    highlightedIndex = null,
                    message = "올바른 인덱스(숫자)를 입력해주세요."
                )
            }
            return
        }

        if (parsedValue == null) {
            _uiState.update {
                it.copy(
                    highlightedIndex = null,
                    message = "올바른 값(숫자)을 입력해주세요."
                )
            }
            return
        }

        updateElementUseCase(currentState.array, parsedIndex, parsedValue)
            .onSuccess { updatedArray ->
                _uiState.update {
                    it.copy(
                        highlightedIndex = parsedIndex,
                        array = updatedArray,
                        message = "인덱스 [$parsedIndex]의 값을 ${parsedValue}로 수정했습니다."
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
        val parsedValue = currentState.valueInput.toIntOrNull()

        if (parsedValue == null) {
            _uiState.update {
                it.copy(
                    highlightedIndex = null,
                    message = "올바른 값(숫자)을 입력해주세요."
                )
            }
            return
        }

        viewModelScope.launch {
            linearSearchUseCase(currentState.array, parsedValue).collect { step ->
                when (step) {
                    is AlgorithmStep.Checking -> {
                        _uiState.update {
                            it.copy(
                                highlightedIndex = step.index
                            )
                        }
                    }

                    is AlgorithmStep.Found -> {
                        _uiState.update {
                            it.copy(
                                highlightedIndex = step.index,
                                message = "값 ${parsedValue}를 찾았습니다. 인덱스: ${step.index}"

                            )
                        }
                    }

                    is AlgorithmStep.NotFound -> {
                        _uiState.update {
                            it.copy(
                                highlightedIndex = null,
                                message = "${parsedValue}은(는) 배열에 존재하지 않습니다."
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

                    is AlgorithmStep.Done -> {}
                    is AlgorithmStep.Moved -> {}
                    is AlgorithmStep.ValueUpdated -> {}
                    is AlgorithmStep.Resized -> {}
                }
            }
        }
    }

    // 배열 순회
    private fun executeTraverse() {
        val currentState = _uiState.value

        viewModelScope.launch {
            traverseArrayUseCase(currentState.array).collect { step ->
                when(step) {
                    is AlgorithmStep.Checking -> {
                        _uiState.update {
                            it.copy(
                                highlightedIndex = step.index
                            )
                        }
                    }

                    is AlgorithmStep.Done -> {
                        _uiState.update {
                            it.copy(
                                highlightedIndex = null,
                                message = "배열 순회를 완료했습니다."
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

                    is AlgorithmStep.Found -> {}
                    is AlgorithmStep.NotFound -> {}
                    is AlgorithmStep.Moved -> {}
                    is AlgorithmStep.ValueUpdated -> {}
                    is AlgorithmStep.Resized -> {}
                }
            }
        }
    }

    private fun executeInsert() {
        val currentState = _uiState.value
        val parsedIndex = currentState.indexInput.toIntOrNull()
        val parsedValue = currentState.valueInput.toIntOrNull()

        if (parsedIndex == null || parsedValue == null) {
            _uiState.update {
                it.copy(
                    highlightedIndex = null,
                    message = "올바른 인덱스와 값(숫자)을 모두 입력해주세요."
                )
            }
            return
        }

        viewModelScope.launch {
            if (currentState.type == ArrayType.STATIC) {
                insertElementUseCase(currentState.array, parsedIndex, parsedValue).collect { step ->
                    when (step) {
                        is AlgorithmStep.Moved -> {
                            // 기존 배열을 복제(clone)해서 새로운 객체로 만듦
                            val newArray = _uiState.value.array.clone()

                            // 데이터 한 칸 뒤로 복사
                            newArray[step.toIndex] = newArray[step.fromIndex]

                            // 기존 자리를 비워두어 빈 공간이 밀려나는 시각적 효과 극대화
                            newArray[step.fromIndex] = null

                            _uiState.update {
                                it.copy(
                                    array = newArray,
                                    highlightedIndex = step.toIndex
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
                                    message = "인덱스 [${step.index}]에 ${step.newValue}를 삽입했습니다."
                                )
                            }
                        }

                        is AlgorithmStep.Done -> {
                            _uiState.update {
                                it.copy(
                                    highlightedIndex = null,
                                    message = "인덱스 [$parsedIndex]에 $parsedValue 삽입이 완료되었습니다."
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

                        is AlgorithmStep.Checking -> {}
                        is AlgorithmStep.Found -> {}
                        is AlgorithmStep.NotFound -> {}
                        is AlgorithmStep.Resized -> {}
                    }
                }
            } else if (currentState.type == ArrayType.DYNAMIC) {
                insertDynamicElementUseCase(currentState.array, parsedIndex, parsedValue).collect { step ->
                    when (step) {
                        is AlgorithmStep.Resized -> {
                            val newArray = _uiState.value.array.clone().copyOf(step.newSize)

                            _uiState.update {
                                it.copy(
                                    array = newArray,
                                    highlightedIndex = null
                                )
                            }
                        }

                        is AlgorithmStep.Moved -> {
                            val newArray = _uiState.value.array.clone()

                            newArray[step.toIndex] = newArray[step.fromIndex]

                            newArray[step.fromIndex] = null

                            _uiState.update {
                                it.copy(
                                    highlightedIndex = step.toIndex,
                                    array = newArray
                                )
                            }
                        }

                        is AlgorithmStep.ValueUpdated -> {
                            val newArray = _uiState.value.array.clone()

                            newArray[step.index] = step.newValue

                            _uiState.update {
                                it.copy(
                                    highlightedIndex = step.index,
                                    array = newArray,
                                    message = "인덱스 [${step.index}]에 ${step.newValue}를 삽입했습니다."
                                )
                            }
                        }

                        is AlgorithmStep.Done -> {
                            _uiState.update {
                                it.copy(
                                    highlightedIndex = null,
                                    message = "동적 배열 삽입이 완료되었습니다."
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

                        is AlgorithmStep.Checking -> {}
                        is AlgorithmStep.Found -> {}
                        is AlgorithmStep.NotFound -> {}
                    }
                }
            }
        }
    }

    private fun executeDelete() {
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

        viewModelScope.launch {
            if (currentState.type == ArrayType.STATIC) {
                deleteElementUseCase(currentState.array, parsedIndex).collect { step ->
                    when (step) {
                        is AlgorithmStep.ValueUpdated -> {
                            val newArray = _uiState.value.array.clone()

                            newArray[parsedIndex] = step.newValue

                            _uiState.update {
                                it.copy(
                                    array = newArray,
                                    highlightedIndex = parsedIndex
                                )
                            }
                        }

                        is AlgorithmStep.Moved -> {
                            val newArray = _uiState.value.array.clone()

                            newArray[step.toIndex] = newArray[step.fromIndex]

                            newArray[step.fromIndex] = null

                            _uiState.update {
                                it.copy(
                                    array = newArray,
                                    highlightedIndex = step.toIndex
                                )
                            }
                        }

                        is AlgorithmStep.Done -> {
                            _uiState.update {
                                it.copy(
                                    highlightedIndex = null,
                                    message = "인덱스 [$parsedIndex]의 삭제가 완료되었습니다."
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

                        is AlgorithmStep.Checking -> {}
                        is AlgorithmStep.Found -> {}
                        is AlgorithmStep.NotFound -> {}
                        is AlgorithmStep.Resized -> {}
                    }
                }
            } else if (currentState.type == ArrayType.DYNAMIC) {
                deleteDynamicElementUseCase(currentState.array, parsedIndex).collect { step ->
                    when (step) {
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
                            val newArray = _uiState.value.array.clone()

                            newArray[step.index] = step.newValue

                            _uiState.update {
                                it.copy(
                                    array = newArray,
                                    highlightedIndex = step.index
                                )
                            }
                        }

                        is AlgorithmStep.Moved -> {
                            val newArray = _uiState.value.array.clone()

                            newArray[step.toIndex] = newArray[step.fromIndex]

                            newArray[step.fromIndex] = null

                            _uiState.update {
                                it.copy(
                                    array = newArray,
                                    highlightedIndex = step.toIndex
                                )
                            }
                        }

                        is AlgorithmStep.Done -> {
                            _uiState.update {
                                it.copy(
                                    highlightedIndex = null,
                                    message = "동적 배열 삭제가 완료되었습니다."
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

                        is AlgorithmStep.Checking -> {}
                        is AlgorithmStep.Found -> {}
                        is AlgorithmStep.NotFound -> {}
                    }
                }
            }
        }
    }
}