package com.catalin.clinapp.data

data class DataDaySchedule(
    val name: String? = null,
    val timeStart: String? = "07:00",
    val timeEnd: String? = "23:00",
    val active: Boolean? = false
)
