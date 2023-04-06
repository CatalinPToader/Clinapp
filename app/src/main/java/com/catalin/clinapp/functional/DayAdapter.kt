package com.catalin.clinapp.functional

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.SwitchCompat
import androidx.recyclerview.widget.RecyclerView
import com.catalin.clinapp.R
import com.google.android.material.slider.RangeSlider
import com.google.android.material.snackbar.Snackbar


class DayAdapter : RecyclerView.Adapter<DayAdapter.ViewHolder>() {
    private val days =
        arrayOf("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday")

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
        holder.dayName.text = days[position]
        setHoursText(holder.slider, holder)
        holder.slider.addOnChangeListener(RangeSlider.OnChangeListener { slider, _, _ ->
            setHoursText(slider, holder)
        })
        holder.switch.setOnClickListener {
            val snack = Snackbar.make(
                holder.switch,
                "Didn't save settings yet",
                Snackbar.LENGTH_LONG
            )
            snack.setTextColor(Color.BLUE)
            snack.show()
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

    private fun setHoursText(slider: RangeSlider, holder: ViewHolder) {
        val start = slider.values[0]
        val end = slider.values[1]
        val sb = StringBuilder()
        sb.append(start.toInt()).append(':')
        if (start % 1.0 > 1e-3)
            sb.append("30")
        else
            sb.append("00")
        sb.append('-')
        sb.append(end.toInt()).append(':')
        if (end % 1.0 > 1e-3)
            sb.append("30")
        else
            sb.append("00")

        holder.hoursSelected.text = sb.toString()
    }
}