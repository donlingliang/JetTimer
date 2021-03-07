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

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.WindowManager
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
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
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.androiddevchallenge.ui.theme.MyTheme

lateinit var context: Context

class MainActivity : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.R)
    @ExperimentalAnimationApi
    override fun onCreate(savedInstanceState: Bundle?) {
        context = this@MainActivity
        super.onCreate(savedInstanceState)
        setContent {
            MyTheme {
                MyApp(context as MainActivity)
            }
        }
    }
}

// Start building your app here!
@RequiresApi(Build.VERSION_CODES.R)
@ExperimentalAnimationApi
@Composable
fun MyApp(context: MainActivity) {

    val backgroundColor = Color(ContextCompat.getColor(context, R.color.background_color))
    val highlight_color = Color(ContextCompat.getColor(context, R.color.highlight_color))

    val displayMetrics = DisplayMetrics()
    val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager

    try {

        context.display?.getRealMetrics(displayMetrics)
    } catch (e: NoSuchMethodError) {
        windowManager.defaultDisplay.getRealMetrics(displayMetrics)
    }

    Surface(color = backgroundColor) {
        HelloScreen(highlight_color, displayMetrics)
    }
}

@ExperimentalAnimationApi
@Composable
fun HelloScreen(color: Color, displayMetrics: DisplayMetrics) {
    val viewModel: TimerViewModel = viewModel()
    Scaffold {
        val seconds: Int by viewModel.seconds.observeAsState(initial = 0)
        val isStarted: State by viewModel.state.observeAsState(initial = State.Finished)

        var width = displayMetrics.widthPixels
        var height = displayMetrics.heightPixels

        Log.d("tag", "width: " + width)
        Log.d("tag", "height: " + height)

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            BackgroundIndicator(width, height, seconds, color = color, isStarted)
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

@ExperimentalAnimationApi
@Composable
fun BackgroundIndicator(
    screenWidth: Int,
    screenHeight: Int,
    seconds: Int,
    color: Color = Color.Cyan,
    isStarted: State
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        val canvas = Canvas(modifier = Modifier.fillMaxWidth()) {
            drawRect(
                color = color,
                topLeft = Offset(0f, (screenHeight / 50) * 1f * (50 - seconds)),
                size = Size(screenWidth.toFloat(), 1f * screenHeight)
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
            .padding(top = 300.dp, bottom = 24.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 8.dp)
        ) {
            Text(
                seconds.toString().padStart(2, '0'),
                modifier = Modifier.alignByBaseline(),
                fontSize = 100.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace
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
        val backgroundColor = Color(ContextCompat.getColor(context, R.color.background_color))
        Box(contentAlignment = Alignment.Center, modifier = Modifier.size(140.dp).padding(40.dp)) {
            FloatingActionButton(backgroundColor = backgroundColor, onClick = onTapFab) {
                AnimatedVisibility(visible = isStarted == State.Finished) {
                }
            }
        }
    }
}
