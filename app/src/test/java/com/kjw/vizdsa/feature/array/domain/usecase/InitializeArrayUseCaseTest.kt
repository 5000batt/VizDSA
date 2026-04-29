package com.kjw.vizdsa.feature.array.domain.usecase

import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class InitializeArrayUseCaseTest {

    private lateinit var useCase: InitializeArrayUseCase

    @BeforeEach
    fun setUp() {
        useCase = InitializeArrayUseCase()
    }

    @Test
    fun `크기가 0 이하로 주어지면 Result_failure를 반환해야 한다`() {
        val invalidSize = 0
        val dummyInput = ""

        val result = useCase(invalidSize, dummyInput)

        assertTrue(result.isFailure)
        assertEquals("배열의 크기는 1 이상이어야 합니다.", result.exceptionOrNull()?.message)
    }

    @Test
    fun `정상적인 크기와 콤마로 구분된 문자열이 주어지면 올바른 배열이 반환되어야 한다`() {
        // [Given] 준비: 정상적으로 작동해야 하는 완벽한 조건과, 우리가 '정답'으로 기대하는 결과를 준비합니다.
        val validSize = 3
        val validInput = "1, 2, 3"
        val expectedArray = listOf(1, 2, 3) // 테스트가 통과하려면 이 정답지와 완벽히 똑같은 결과가 나와야 합니다.

        // [When] 실행: 준비한 정상 값을 UseCase에 넣고 실행합니다.
        val result = useCase(validSize, validInput)

        // [Then] 검증: 예상대로 완벽한 결과가 나왔는지 채점합니다.
        // 1. 에러 없이 성공(Success) 상태로 돌아왔는지 확인합니다.
        assertTrue(result.isSuccess)

        // 2. Result 안에 포장되어 있는 진짜 데이터(배열)를 꺼내서(getOrNull),
        // 우리가 미리 적어둔 정답지(expectedArray)와 내용물이 완전히 똑같은지 비교합니다.
        assertEquals(expectedArray, result.getOrNull())
    }
}