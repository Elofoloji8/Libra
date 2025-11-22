package com.elo.libra.ui.chatbot

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.elo.libra.viewmodel.ChatbotViewModel
import androidx.compose.ui.platform.LocalContext
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.material3.AssistChip

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatbotScreen(navController: NavHostController) {

    val viewModel: ChatbotViewModel = viewModel()
    val messages = viewModel.messages
    val context = LocalContext.current

    LaunchedEffect(true) {
        viewModel.initLocalDataset(context)
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(title = { Text("Libra Assistant") })
        }
    ) { padding ->

        Column(
            Modifier
                .padding(padding)
                .fillMaxSize()
        ) {

            // Mesajlar
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .padding(16.dp)
            ) {
                items(messages) { msg ->
                    Text(
                        text = msg,
                        modifier = Modifier.padding(4.dp)
                    )
                }
            }
            when (viewModel.step) {

                0 -> {
                    OptionButtons(viewModel.genreOptions) { selected ->
                        viewModel.sendMessage(selected)
                    }
                }

                1 -> {
                    OptionButtons(viewModel.moodOptions) { selected ->
                        viewModel.sendMessage(selected)
                    }
                }

                2 -> {
                    OptionButtons(viewModel.lastReadOptions) { selected ->
                        viewModel.sendMessage(selected)
                    }
                }
            }
        }
    }
}

@Composable
fun OptionButtons(options: List<String>, onSelect: (String) -> Unit) {
    FlowRow(Modifier.padding(8.dp)) {
        options.forEach { option ->
            AssistChip(
                onClick = { onSelect(option) },
                label = { Text(option) },
                modifier = Modifier.padding(4.dp)
            )
        }
    }
}