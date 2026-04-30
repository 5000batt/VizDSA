package com.kjw.vizdsa.feature.array.presentation

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kjw.vizdsa.feature.array.presentation.components.ArrayCell

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArrayScreen(
    uiState: ArrayUiState,
    onTypeChange: (ArrayType) -> Unit,
    onSizeChange: (String) -> Unit,
    onValueChange: (String) -> Unit,
    onOperationChange: (ArrayOperation) -> Unit,
    onExecute: () -> Unit,
    onMessageShow: () -> Unit,
    onReset: () -> Unit
) {
    val context = LocalContext.current
    LaunchedEffect(uiState.message) {
        if (uiState.message.isNotBlank()) {
            Toast.makeText(context, uiState.message, Toast.LENGTH_SHORT).show()
            onMessageShow()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("배열(Array) 시각화", fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))

        // 상단: 컨트롤 패널 영역
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            var typeExpanded by remember { mutableStateOf(false) }
            ExposedDropdownMenuBox(
                expanded = typeExpanded,
                onExpandedChange = { typeExpanded = !typeExpanded },
                modifier = Modifier.weight(0.85f)
            ) {
                OutlinedTextField(
                    value = uiState.type?.name ?: "배열 타입 선택",
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("배열 타입") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = typeExpanded) },
                    modifier = Modifier.menuAnchor(),
                    singleLine = true
                )
                ExposedDropdownMenu(
                    expanded = typeExpanded,
                    onDismissRequest = { typeExpanded = false }
                ) {
                    ArrayType.entries.forEach { type ->
                        DropdownMenuItem(
                            text = { Text(type.name) },
                            onClick = {
                                onTypeChange(type)
                                typeExpanded = false
                            }
                        )
                    }
                }
            }

            var expanded by remember { mutableStateOf(false) }
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded },
                modifier = Modifier.weight(1.15f)
            ) {
                OutlinedTextField(
                    value = uiState.operation?.name ?: "동작 선택",
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("동작") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                    modifier = Modifier.menuAnchor(),
                    singleLine = true
                )
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    ArrayOperation.entries.forEach { operation ->
                        DropdownMenuItem(
                            text = { Text(operation.name) },
                            onClick = {
                                onOperationChange(operation)
                                expanded = false
                            }
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = uiState.sizeInput,
                onValueChange = onSizeChange,
                label = { Text("배열 크기") },
                modifier = Modifier.weight(1f),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            OutlinedTextField(
                value = uiState.valueInput,
                onValueChange = onValueChange,
                label = { Text("배열 요소 (ex: 1,2,3)") },
                modifier = Modifier.weight(2f),
                singleLine = true
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        // 중단: 배열 시각화 영역
        if (uiState.array.isEmpty()) {
            Box(
                modifier = Modifier.weight(1f).fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "초기화된 배열이 없습니다.",
                    color = Color.Gray,
                    fontSize = 16.sp
                )
            }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Adaptive(minSize = 60.dp),
                modifier = Modifier.weight(1f).fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                itemsIndexed(uiState.array) { index, value ->
                    ArrayCell(
                        index = index,
                        value = value,
                        isHighlighted = index == uiState.highlightedIndex
                    )
                }
            }
        }

        // span 테스트 코드
        /*LazyVerticalGrid(
            columns = GridCells.Fixed(3)
        ) {
            // 1. 전체 너비를 차지하는 헤더 (span 3개 차지)
            item(span = { GridItemSpan(maxLineSpan) }) {
                Text("배열 시각화 결과", modifier = Modifier.fillMaxWidth())
            }

            // 2. 일반적인 데이터 셀 (기본 span 1 차지)
            itemsIndexed(uiState.array) { index, value ->
                ArrayCell(
                    index = index,
                    value = value,
                    isHighlighted = index == uiState.highlightedIndex
                )
            }
        }*/

        Spacer(modifier = Modifier.height(16.dp))

        // 하단: 실행 버튼 영역
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Button(
                onClick = onExecute,
                modifier = Modifier
                    .weight(2f)
                    .height(56.dp)
            ) {
                Text("실행", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }

            OutlinedButton(
                onClick = onReset,
                modifier = Modifier
                    .weight(1f)
                    .height(56.dp)
            ) {
                Text("리셋", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Preview(showBackground = true)
@Composable
fun ArrayScreenPreview() {
    // 미리보기를 위한 가짜(Dummy) 데이터와 빈 함수들을 넣어줍니다.
    ArrayScreen(
        uiState = ArrayUiState(
            array = listOf(10, 20, 30, null, null, 40, 50, 60, 70), // 가짜 배열 데이터
            sizeInput = "10",
            operation = ArrayOperation.INITIALIZE,
            valueInput = "10, 20, 30",
            message = "배열이 초기화되었습니다.",
            highlightedIndex = 0
        ),
        onTypeChange = {},
        onSizeChange = {},
        onValueChange = {},
        onOperationChange = {},
        onExecute = {},
        onMessageShow = {},
        onReset = {}
    )
}