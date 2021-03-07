package com.example.androiddevchallenge

import android.os.CountDownTimer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class TimerViewModel : ViewModel() {

    private val mutableState = MutableLiveData(State.Finished)
    val state: LiveData<State> = mutableState

    private val mutableSeconds = MutableLiveData(50)
    val seconds: LiveData<Int> = mutableSeconds


    fun toggle() {
        when (mutableState.value) {
            State.Started -> {
                resetTimer()
            }
            State.Finished -> {
                startTimer()
            }
        }
    }

    private fun startTimer() {
        mutableState.postValue(State.Started)
        val totalTime: Long = seconds.value!! * 100L
        object : CountDownTimer(totalTime, 100) {
            override fun onTick(millisUntilFinished: Long) {
                mutableSeconds.postValue((millisUntilFinished / 100).toInt())
            }

            override fun onFinish() {
                resetTimer()
            }
        }.start()
    }

    private fun resetTimer() {
        mutableState.postValue(State.Finished)
        mutableSeconds.postValue(50)
    }

    fun isStarted(): Boolean {
        return mutableState.value == State.Started
    }
}

enum class State {
    Started,
    Finished
}