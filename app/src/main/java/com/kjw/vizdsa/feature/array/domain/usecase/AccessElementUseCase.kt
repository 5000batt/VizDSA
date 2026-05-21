package com.kjw.vizdsa.feature.array.domain.usecase

import com.kjw.vizdsa.core.domain.model.OperationResult
import com.kjw.vizdsa.core.domain.util.validateIndexBounds
import com.kjw.vizdsa.core.domain.util.validateIsNotEmpty
import javax.inject.Inject

class AccessElementUseCase @Inject constructor() {
    operator fun invoke(array: Array<Int?>, targetIndex: Int): Result<OperationResult<Int>> {

        // 유효성 검사
        array.validateIsNotEmpty()?.let { return Result.failure(Exception(it.message)) }
        array.validateIndexBounds(targetIndex)?.let { return Result.failure(Exception(it.message)) }

        val resultMessage = if (array[targetIndex] != null) {
            "인덱스 [$targetIndex]에 접근했습니다. 값: ${array[targetIndex]}"
        } else {
            "인덱스 [$targetIndex]에 접근했지만, 값이 비어있습니다 (null)."
        }

        return Result.success(OperationResult(targetIndex, resultMessage))
    }
}