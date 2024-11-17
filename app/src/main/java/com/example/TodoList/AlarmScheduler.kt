package com.example.TodoList

interface AlarmScheduler {
    fun schedule(item: Alarm)
    fun cancel(item: Alarm)
}