package com.kjw.vizdsa.core.domain.model

sealed class AlgorithmStep {
    data class Error(val message: String) : AlgorithmStep()
    data class Checking(val index: Int) : AlgorithmStep()
    data class Found(val index: Int) : AlgorithmStep()
    object NotFound : AlgorithmStep()
    object Done : AlgorithmStep()
}