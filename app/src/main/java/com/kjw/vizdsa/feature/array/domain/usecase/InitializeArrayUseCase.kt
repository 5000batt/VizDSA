package com.kjw.vizdsa.feature.array.domain.usecase

import com.kjw.vizdsa.core.domain.util.validateInitialValuesSize
import com.kjw.vizdsa.core.domain.util.validateMaxSize
import com.kjw.vizdsa.core.domain.util.validateSize
import javax.inject.Inject

class InitializeArrayUseCase @Inject constructor() {
    operator fun invoke(size: Int, initialValues: List<Int>): Result<Array<Int?>> {

        // 유효성 검사
        size.validateSize()?.let { return Result.failure(Exception(it.message)) }
        size.validateMaxSize()?.let { return Result.failure(Exception(it.message)) }
        initialValues.validateInitialValuesSize(size)?.let { return Result.failure(Exception(it.message)) }

        // 배열 생성 로직
        val newArray = if (initialValues.isEmpty()) {
            // 값이 없거나 파싱 실패 시: 0~99 랜덤 값으로 size만큼 채움
            Array<Int?>(size) { (0..99).random() }
        } else {
            // 정상 입력 시: 입력된 값으로 채우고, size보다 입력이 적으면 나머지는 null로 채움
            Array(size) { index ->
                if (index < initialValues.size) initialValues[index] else null
            }
        }

        return Result.success(newArray)
    }
}