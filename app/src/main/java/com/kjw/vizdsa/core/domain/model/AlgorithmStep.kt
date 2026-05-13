package com.kjw.vizdsa.core.domain.model

sealed class AlgorithmStep {
    data class Error(val message: String) : AlgorithmStep()
    data class Checking(val index: Int) : AlgorithmStep()
    data class Found(val index: Int) : AlgorithmStep()
    data class Moved(val fromIndex: Int, val toIndex: Int) : AlgorithmStep()
    data class ValueUpdated(val index: Int, val newValue: Int?) : AlgorithmStep()
    object NotFound : AlgorithmStep()
    object Done : AlgorithmStep()
}