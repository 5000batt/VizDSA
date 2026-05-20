package com.kjw.vizdsa.feature.array.domain.usecase

import com.kjw.vizdsa.core.domain.util.validateIndexBounds
import com.kjw.vizdsa.core.domain.util.validateIsNotEmpty
import javax.inject.Inject

class UpdateElementUseCase @Inject constructor() {
    operator fun invoke(array: Array<Int?>, targetIndex: Int, newValue: Int): Result<Array<Int?>> {

        // 유효성 검사
        array.validateIsNotEmpty()?.let { return Result.failure(Exception(it.message)) }
        array.validateIndexBounds(targetIndex)?.let { return Result.failure(Exception(it.message)) }

        array[targetIndex] = newValue

        return Result.success(array)
    }
}