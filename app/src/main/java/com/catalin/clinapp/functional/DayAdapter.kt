package com.catalin.clinapp.functional

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.SwitchCompat
import androidx.recyclerview.widget.RecyclerView
import com.catalin.clinapp.R
import com.catalin.clinapp.data.Schedule
import com.google.android.material.slider.RangeSlider


class DayAdapter(private val schedule: Schedule) : RecyclerView.Adapter<DayAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val dayName: TextView
        val hoursSelected: TextView
        val slider: RangeSlider
        val switch: SwitchCompat
        val bgSmall: ImageView
        val bgBig: ImageView

        init {
            dayName = itemView.findViewById(R.id.dayText)
            hoursSelected = itemView.findViewById(R.id.hoursText)
            slider = itemView.findViewById(R.id.hoursSlider)
            switch = itemView.findViewById(R.id.daySwitch)
            bgSmall = itemView.findViewById(R.id.backgroundSmall)
            bgBig = itemView.findViewById(R.id.backgroundBig)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.office_item, parent, false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return 7
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.dayName.text = schedule.days[position].name
        holder.slider.values = listOf(
            getValueFromTimeString(schedule.days[position].timeStart!!),
            getValueFromTimeString(schedule.days[position].timeEnd!!)
        )

        val hoursText = schedule.days[position].timeStart + '-' + schedule.days[position].timeEnd

        holder.hoursSelected.text = hoursText

        holder.switch.isChecked = schedule.days[position].active!!

        holder.slider.addOnChangeListener(RangeSlider.OnChangeListener { slider, _, _ ->
            setHoursText(slider, holder, position)
        })
        holder.switch.setOnClickListener {
            schedule.days[position].active = holder.switch.isChecked
        }
        holder.bgSmall.setOnClickListener {
            it.visibility = View.INVISIBLE
            holder.slider.visibility = View.VISIBLE
            holder.bgBig.visibility = View.VISIBLE
        }
        holder.bgBig.setOnClickListener {
            it.visibility = View.GONE
            holder.slider.visibility = View.GONE
            holder.bgSmall.visibility = View.VISIBLE
        }
    }

    private fun setHoursText(slider: RangeSlider, holder: ViewHolder, position: Int) {
        val start = slider.values[0]
        val end = slider.values[1]
        val startStr = StringBuilder()
        val endStr = StringBuilder()

        buildHourString(start, startStr)
        buildHourString(end, endStr)

        schedule.days[position].timeStart = startStr.toString()
        schedule.days[position].timeEnd = endStr.toString()

        holder.hoursSelected.text = startStr.append('-').append(endStr).toString()
    }

    private fun buildHourString(start: Float, startStr: StringBuilder) {
        val intRep = start.toInt()

        if (intRep < 100)
            startStr.append("0")
        startStr.append(intRep / 10).append(':')
        if (intRep % 10 > 0)
            startStr.append("30")
        else
            startStr.append("00")
    }

    private fun getValueFromTimeString(time: String): Float {
        val splits = time.split(":", limit = 2)
        var timeFloat = splits[0].toFloat() * 10
        if (splits[1].toInt() > 0)
            timeFloat += 5

        return timeFloat
    }
}