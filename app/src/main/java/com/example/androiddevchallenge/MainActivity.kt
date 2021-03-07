/*
 * Copyright 2021 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.androiddevchallenge

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.androiddevchallenge.ui.theme.MyTheme

class MainActivity : AppCompatActivity() {
    @ExperimentalAnimationApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyTheme {
                MyApp()
            }
        }
    }
}

// Start building your app here!
@ExperimentalAnimationApi
@Composable
fun MyApp() {
    Surface(color = MaterialTheme.colors.background) {
        HelloScreen()
    }
}

@ExperimentalAnimationApi
@Composable
fun HelloScreen() {
    val viewModel: TimerViewModel = viewModel()
    Scaffold {
        val seconds: Int by viewModel.seconds.observeAsState(initial = 0)
        val isStarted: State by viewModel.state.observeAsState(initial = State.Finished)

        var width = 1440

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            BackgroundIndicator(width, seconds)
            CountdownNumber(seconds)
            Control(
                onTapFab = {
                    viewModel.toggle()
                },
                isStarted
            )
        }
    }
}

@Composable
fun BackgroundIndicator(screenWidth: Int, seconds: Int, color: Color = Color.Cyan) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        val canvas = Canvas(modifier = Modifier.fillMaxWidth()) {
            drawRect(color = color, size = Size(screenWidth.toFloat(), 2400f * seconds / 150))
        }
    }
}

@Composable
fun CountdownNumber(
    seconds: Int = 0
) {
    Row(
        modifier = Modifier
            .padding(top = 24.dp, bottom = 24.dp)
            .height(140.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        Row(
            verticalAlignment = Alignment.Bottom,
            modifier = Modifier.padding(horizontal = 8.dp)
        ) {
            Text(
                seconds.toString().padStart(2, '0'),
                modifier = Modifier.alignByBaseline(),
                fontSize = 100.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@ExperimentalAnimationApi
@Composable
fun Control(
    onTapFab: () -> Unit = {},
    isStarted: State
) {
    AnimatedVisibility(
        visible = isStarted == State.Finished,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        Box(contentAlignment = Alignment.Center, modifier = Modifier.size(72.dp)) {
            FloatingActionButton(onClick = onTapFab) {
                AnimatedVisibility(visible = isStarted == State.Finished) {
                }
            }
        }
    }
}

@ExperimentalAnimationApi
@Preview("Dark Theme", widthDp = 360, heightDp = 640)
@Composable
fun DarkPreview() {
    MyTheme(darkTheme = true) {
        MyApp()
    }
}
