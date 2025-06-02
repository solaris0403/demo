package com.example.view

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sin
import kotlin.math.sqrt

class RadarChartView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var centerX = 0f
    private var centerY = 0f
    private var radius = 0f

    private val axisCount = 16 // 8个固定轴
    private val maxDataPoints = 300 // 最大数据点数量
    private val accelerationData = Array(axisCount) { ArrayDeque<Float>(maxDataPoints) }
    private val updateCounters = IntArray(axisCount) // 跟踪每个轴的更新计数
    private var maxAcceleration = 10f // 默认最大加速度为10 m/s²
    private var lastUpdatedAxisIndex = -1 // 跟踪最后更新的轴索引

    private val dataPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        color = Color.argb(100, 0, 150, 255) // 半透明蓝色
    }
    private val linePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        color = Color.BLUE
        strokeWidth = 3f
    }
    private val axisPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        color = Color.GRAY
        strokeWidth = 1f
    }
    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.BLACK
        textSize = 30f
        textAlign = Paint.Align.CENTER
    }
    private val latestPointPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        color = Color.RED
    }

    fun updateAcceleration(lateral: Float, longitudinal: Float) {
        val magnitude = sqrt(lateral * lateral + longitudinal * longitudinal)
        var angle = atan2(longitudinal, lateral)

        if (angle < 0) angle += 2 * Math.PI.toFloat()

        val closestAxisIndex = ((angle + Math.PI / axisCount) / (2 * Math.PI / axisCount)).toInt() % axisCount
        val safeIndex = closestAxisIndex.coerceIn(0, axisCount - 1)

        // 更新数据和计数器
        if (accelerationData[safeIndex].size >= maxDataPoints) {
            accelerationData[safeIndex].removeFirst()
        }
        accelerationData[safeIndex].addLast(magnitude)
        updateCounters[safeIndex] = 0
        lastUpdatedAxisIndex = safeIndex // 更新最后更新的轴索引

        // 增加其他轴的计数器，如果超过300则设置为0
        for (i in updateCounters.indices) {
            if (i != safeIndex) {
                updateCounters[i]++
                if (updateCounters[i] >= maxDataPoints) {
                    accelerationData[i].clear()
                    accelerationData[i].addLast(0f)
                    updateCounters[i] = 0
                }
            }
        }

        maxAcceleration = max(maxAcceleration, magnitude)
        invalidate()
    }

    fun setMaxAcceleration(max: Float) {
        maxAcceleration = max
        invalidate()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        centerX = w / 2f
        centerY = h / 2f
        radius = min(w, h) / 2f * 0.8f
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        drawWeb(canvas)
        drawAxis(canvas)
        drawAcceleration(canvas)
        drawLabels(canvas)
    }

    private fun drawWeb(canvas: Canvas) {
        val webCount = 5
        for (i in 1..webCount) {
            val path = Path()
            val currentRadius = radius / webCount * i
            for (j in 0 until axisCount) {
                val angle = Math.PI * 2 / axisCount * j - Math.PI / 2
                val x = centerX + currentRadius * cos(angle).toFloat()
                val y = centerY + currentRadius * sin(angle).toFloat()
                if (j == 0) path.moveTo(x, y) else path.lineTo(x, y)
            }
            path.close()
            canvas.drawPath(path, axisPaint)
        }
    }

    private fun drawAxis(canvas: Canvas) {
        for (i in 0 until axisCount) {
            val angle = Math.PI * 2 / axisCount * i - Math.PI / 2
            val endX = centerX + radius * cos(angle).toFloat()
            val endY = centerY + radius * sin(angle).toFloat()
            canvas.drawLine(centerX, centerY, endX, endY, axisPaint)
        }
    }

    private fun drawAcceleration(canvas: Canvas) {
        val path = Path()
        accelerationData.forEachIndexed { index, data ->
            val latestAcceleration = data.lastOrNull() ?: 0f
            val normalizedMagnitude = (latestAcceleration / maxAcceleration).coerceIn(0f, 1f)
            val angle = Math.PI * 2 / axisCount * index - Math.PI / 2
            val x = centerX + radius * normalizedMagnitude * cos(angle).toFloat()
            val y = centerY + radius * normalizedMagnitude * sin(angle).toFloat()
            if (index == 0) path.moveTo(x, y) else path.lineTo(x, y)

            // 只在最后更新的轴上绘制红点
            if (index == lastUpdatedAxisIndex) {
                canvas.drawCircle(x, y, 10f, latestPointPaint)
            }
        }
        path.close()
        canvas.drawPath(path, dataPaint)
        canvas.drawPath(path, linePaint)
    }

    private fun drawLabels(canvas: Canvas) {
        val labels = arrayOf("前", "前右", "右", "后右", "后", "后左", "左", "前左")
        labels.forEachIndexed { index, label ->
            val angle = Math.PI * 2 / axisCount * index - Math.PI / 2
            val x = centerX + (radius + 40) * cos(angle).toFloat()
            val y = centerY + (radius + 40) * sin(angle).toFloat() + 10
            canvas.drawText(label, x, y, textPaint)
        }
    }
}