package com.dan.lightsensor.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel
import com.dan.lightsensor.presentation.compose.component.ComposableActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComposableActivity() {

    @Composable
    override fun Content() {
        val viewModel = viewModel<MainViewModel>()

        val isDark by viewModel.isDark

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxSize()
                .background(if (isDark) Color.Black else Color.White)
        ) {
            Text(
                text = if (isDark) "It's dark!" else "It's light!",
                color = if (isDark) Color.White else Color.Black
            )
        }
    }
}