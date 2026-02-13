package com.example.gymtracker.util

import android.content.Context
import android.graphics.Color
import androidx.core.content.ContextCompat
import com.example.gymtracker.R
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter

object ChartStyler {

    private fun getTextColor(context: Context): Int =
        ContextCompat.getColor(context, R.color.text_secondary)

    private fun getGridColor(): Int = Color.parseColor("#333333")

    fun styleLineChart(chart: LineChart, context: Context) {
        chart.apply {
            description.isEnabled = false
            legend.isEnabled = false
            setTouchEnabled(true)
            isDragEnabled = true
            setScaleEnabled(false)
            setPinchZoom(false)
            setDrawGridBackground(false)
            setBackgroundColor(Color.TRANSPARENT)
            setNoDataTextColor(getTextColor(context))

            xAxis.apply {
                position = XAxis.XAxisPosition.BOTTOM
                textColor = getTextColor(context)
                gridColor = getGridColor()
                setDrawGridLines(false)
                granularity = 1f
            }

            axisLeft.apply {
                textColor = getTextColor(context)
                gridColor = getGridColor()
                setDrawGridLines(true)
                enableGridDashedLine(5f, 5f, 0f)
            }

            axisRight.isEnabled = false
            animateX(300)
        }
    }

    fun styleBarChart(chart: BarChart, context: Context) {
        chart.apply {
            description.isEnabled = false
            legend.isEnabled = false
            setTouchEnabled(true)
            isDragEnabled = true
            setScaleEnabled(false)
            setPinchZoom(false)
            setDrawGridBackground(false)
            setBackgroundColor(Color.TRANSPARENT)
            setNoDataTextColor(getTextColor(context))
            setFitBars(true)

            xAxis.apply {
                position = XAxis.XAxisPosition.BOTTOM
                textColor = getTextColor(context)
                gridColor = getGridColor()
                setDrawGridLines(false)
                granularity = 1f
            }

            axisLeft.apply {
                textColor = getTextColor(context)
                gridColor = getGridColor()
                setDrawGridLines(true)
                enableGridDashedLine(5f, 5f, 0f)
                axisMinimum = 0f
            }

            axisRight.isEnabled = false
            animateY(300)
        }
    }

    fun createLineDataSet(
        entries: List<Entry>,
        label: String,
        colorRes: Int,
        context: Context
    ): LineDataSet {
        val color = ContextCompat.getColor(context, colorRes)
        return LineDataSet(entries, label).apply {
            this.color = color
            setCircleColor(color)
            lineWidth = 2f
            circleRadius = 3f
            setDrawCircleHole(false)
            setDrawValues(false)
            mode = LineDataSet.Mode.CUBIC_BEZIER
            setDrawFilled(true)
            fillColor = color
            fillAlpha = 30
        }
    }

    fun createBarDataSet(
        entries: List<BarEntry>,
        label: String,
        colorRes: Int,
        context: Context
    ): BarDataSet {
        val color = ContextCompat.getColor(context, colorRes)
        return BarDataSet(entries, label).apply {
            this.color = color
            setDrawValues(false)
        }
    }

    fun setXAxisLabels(chart: LineChart, labels: List<String>) {
        chart.xAxis.valueFormatter = IndexAxisValueFormatter(labels)
    }

    fun setXAxisLabels(chart: BarChart, labels: List<String>) {
        chart.xAxis.valueFormatter = IndexAxisValueFormatter(labels)
    }
}
