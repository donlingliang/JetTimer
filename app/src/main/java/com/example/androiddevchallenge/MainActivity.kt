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
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.androiddevchallenge.ui.theme.MyTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.remember as remember

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

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
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
                style = MaterialTheme.typography.h1,
                modifier = Modifier.alignByBaseline()
            )
            Text(
                "s",
                style = MaterialTheme.typography.h1,
                modifier = Modifier.alignByBaseline()
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
        enter = fadeIn(initialAlpha = 0f),
        exit = fadeOut(
            // Overwrites the default animation with tween
            animationSpec = tween(durationMillis = 250)
        )
    ) {
        Box(contentAlignment = Alignment.Center, modifier = Modifier.size(72.dp)) {
            FloatingActionButton(onClick = onTapFab) {
                AnimatedVisibility(visible = isStarted == State.Finished) {
                    Icon(
                        Icons.Default.PlayArrow,
                        contentDescription = "start"
                    )
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
