package com.dhruv.fitgenieai.presentation.ui


import android.R.attr.text
import android.text.TextUtils.split
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField

import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dhruv.fitgenieai.domain.model.ChatMessage
import com.dhruv.fitgenieai.domain.model.Section
import com.dhruv.fitgenieai.presentation.viewModel.ChatUIState
import com.dhruv.fitgenieai.presentation.viewModel.GenerateFitnessPlanViewModel
import dev.jeziellago.compose.markdowntext.MarkdownText


@Composable
fun ChatScreen(
    generateFitnessPlanViewModel: GenerateFitnessPlanViewModel = hiltViewModel()
) {

    val currentUiState by generateFitnessPlanViewModel.uiState.collectAsStateWithLifecycle()
    val messages by generateFitnessPlanViewModel.messages.collectAsStateWithLifecycle()

    var text by remember { mutableStateOf("") }
    val listState = rememberLazyListState()

    val keyboardController = LocalSoftwareKeyboardController.current

    LaunchedEffect(messages.size) {
        listState.animateScrollToItem(0)
    }

    Scaffold(
        topBar = {
        },
        bottomBar = {
            CustomTextField(
                text = text,
                onValueChanged = { text = it },
                onSendClick = {
                    if (text.isNotBlank()) {
                        generateFitnessPlanViewModel.sendMessage(text)
                        text = ""
                        keyboardController?.hide()
                    }
                }
            )
        },
        containerColor = Color(0xFFF7F7F7),
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF8F9FA))
                .padding(paddingValues)
        ) {
            LazyColumn(
                state = listState,
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                reverseLayout = true,
                verticalArrangement = Arrangement.Top
            ) {

                if (currentUiState is ChatUIState.Error) {
                    item {
                        ErrorBubble(
                            errorMessage = (currentUiState as ChatUIState.Error).error,
                            onRetry = {
                            }
                        )
                    }
                }

                if (currentUiState is ChatUIState.Loading) {
                    item {
                        TypingIndicatorBubble()
                    }
                }

                items(messages) { message ->
                    ChatRow(message)
                }

            }
        }
    }
}

@Composable
fun ErrorBubble(errorMessage: String, onRetry: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Surface(
            color = Color(0xFFFFF1F0),
            shape = RoundedCornerShape(12.dp),
            border = BorderStroke(1.dp, Color(0xFFFFA39E))
        ) {
            Column(
                modifier = Modifier.padding(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Connection Error",
                    style = TextStyle(fontWeight = FontWeight.Bold, color = Color(0xFFCF1322))
                )
                Text(
                    text = errorMessage,
                    style = TextStyle(fontSize = 13.sp, color = Color(0xFFCF1322)),
                    textAlign = TextAlign.Center
                )

                TextButton(onClick = onRetry) {
                    Text("Try Again", color = Color(0xFF4096FF))
                }
            }
        }
    }
}

@Composable
fun TypingIndicatorBubble() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.Start
    ) {
        Surface(
            color = Color.White,
            shape = RoundedCornerShape(
                topStart = 16.dp,
                topEnd = 16.dp,
                bottomEnd = 16.dp,
                bottomStart = 0.dp
            ),
            border = BorderStroke(1.dp, Color(0xFFEEEEEE)),
            shadowElevation = 1.dp
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Coach is thinking",
                    style = TextStyle(color = Color.Gray, fontSize = 14.sp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                CircularProgressIndicator(
                    modifier = Modifier.size(16.dp),
                    strokeWidth = 2.dp,
                    color = Color.Black
                )
            }
        }
    }
}

@Composable
fun ChatRow(message: ChatMessage) {
    val isUser = message.isFromUser
    val bubbleShape = RoundedCornerShape(
        topStart = 10.dp,
        topEnd = 10.dp,
        bottomStart = if (isUser) 10.dp else 0.dp,
        bottomEnd = if (isUser) 0.dp else 10.dp
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
        horizontalAlignment = if (isUser) Alignment.End else Alignment.Start
    ) {

        if (message.sections != null) {
            var visible by remember { mutableStateOf(false)}

            LaunchedEffect(visible) {
                visible = true
            }

            Column {
                message.sections.forEachIndexed { index, section ->
                    AnimatedVisibility(
                        visible = visible,
                        enter = fadeIn(
                            animationSpec = tween(
                                durationMillis = 300,
                                delayMillis = index * 100
                            )
                        ) + slideInVertically {  it / 4 }
                    ) {
                        SectionCard(section)
                    }
                }
            }
        } else {
            Surface(
                color = if (isUser) Color(0xFFDCF8C6) else Color(0xFFF1F1F1),
                shape = bubbleShape,
                shadowElevation = 2.dp,
            ) {
                Text(
                    text = message.message,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
                    style = MaterialTheme.typography.bodyMedium.copy(
                        lineHeight = 20.sp,
                        color = Color.Black
                    )
                )
            }
        }
    }
}

@Composable
fun SectionCard(section: Section) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = Color.White,
        shadowElevation = 2.dp,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Title
            Text(
                text = section.title,
                fontWeight = FontWeight.SemiBold,
                fontSize = 15.sp,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(8.dp))

            val bullets = section.content
                .split("\n")
                .filter { it.isNotBlank() }

            Column {
                bullets.forEach { line ->
                    Row(
                        modifier = Modifier.padding(vertical = 4.dp)
                    ) {
                        Text(
                            text = "• ",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        
                        MarkdownText(
                            markdown = line.replace(Regex("^\\s*[-*]\\s*"), ""),
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color.Transparent)
                                .padding(8.dp),
                            style = MaterialTheme.typography.bodyMedium.copy(
                                lineHeight = 20.sp
                            )
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CustomTextField(
    text: String,
    onValueChanged: (String) -> Unit,
    onSendClick: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth().imePadding(),
        tonalElevation = 8.dp,
        shadowElevation = 10.dp,
        color = Color.White
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            BasicTextField(
                value = text,
                onValueChange = onValueChanged,
                modifier = Modifier
                    .weight(1f)
                    .background(Color(0xFFF1F1F1), RoundedCornerShape(24.dp))
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                decorationBox = { innerTextField ->
                    if (text.isEmpty()) {
                        Text("Ask your coach...", color = Color.Gray)
                    }
                    innerTextField()
                }
            )

            Spacer(modifier = Modifier.width(8.dp))

            FloatingActionButton(
                onClick = onSendClick,
                containerColor = Color.Black,
                contentColor = Color.White,
                shape = CircleShape,
                modifier = Modifier.size(48.dp)
            ) {
                Icon(
                    Icons.Default.Send,
                    contentDescription = "Send",
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

