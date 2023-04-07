package com.catalin.clinapp.data

import kotlin.properties.Delegates

class Schedule() {
    private var toCompare: Schedule? = null
    var same = true

    var scheduleCompareListener = ArrayList<() -> Unit>()

    var days = listOf(
        DaySchedule("Monday"),
        DaySchedule("Tuesday"),
        DaySchedule("Wednesday"),
        DaySchedule("Thursday"),
        DaySchedule("Friday"),
        DaySchedule("Saturday"),
        DaySchedule("Sunday")
    )

    constructor(data: DataSchedule) : this() {
        val newDays = ArrayList<DaySchedule>()

        for (day in data.days!!) {
            newDays.add(DaySchedule(day))
        }

        this.days = newDays
    }


    fun setCompareSchedule(toCompare: Schedule) {
        this.toCompare = toCompare
    }

    private fun compareDays() {
        if (toCompare == null)
            return

        var same = true

        for (i in days.indices) {
            same = days[i].isEqual(toCompare!!.days[i])

            if (!same)
                break
        }

        if (this.same != same) {
            this.same = same
            scheduleCompareListener.forEach {
                it()
            }
        }
    }

    fun createDataSchedule(): DataSchedule {
        val dataDays = ArrayList<DataDaySchedule>()
        for (day in days)
            dataDays.add(day.toDataDay())

        return DataSchedule(dataDays)
    }

    inner class DaySchedule(
        val name: String? = null,
        timeStart: String? = "07:00",
        timeEnd: String? = "23:00",
        active: Boolean? = false
    ) {

        constructor(data: DataDaySchedule) : this(
            data.name,
            data.timeStart,
            data.timeEnd,
            data.active
        ) {
        }

        var timeStart: String? by Delegates.observable(timeStart) { _, _, _ ->
            compare()
        }

        var timeEnd: String? by Delegates.observable(timeEnd) { _, _, _ ->
            compare()
        }

        var active: Boolean? by Delegates.observable(active) { _, _, _ ->
            compare()
        }

        private fun compare() {
            compareDays()
        }

        fun isEqual(other: DaySchedule): Boolean {
            return (other.name.equals(name)) and (other.timeStart.equals(timeStart)) and (other.timeEnd.equals(
                timeEnd
            )) and (other.active == active)
        }

        fun toDataDay(): DataDaySchedule {
            return DataDaySchedule(name, timeStart, timeEnd, active)
        }
    }
}
