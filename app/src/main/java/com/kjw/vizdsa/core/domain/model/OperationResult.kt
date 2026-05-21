package com.kjw.vizdsa.core.domain.model

data class OperationResult<T>(
    val data: T,
    val message: String
)
