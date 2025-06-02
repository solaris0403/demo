package com.example.view

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import kotlin.math.atan2
import kotlin.math.min

/**
 * 凸包视图控件
 * 使用固定大小队列存储最近的点，并实时计算和显示凸包
 */
class ConvexHullView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    // 使用FixedSizeQueue存储最多100个点
    private val points = FixedSizeQueue<Point>(9)
    // 存储构成凸包的点
    private val hullPoints = mutableListOf<Point>()

    // 绘制普通点的画笔
    private val pointPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        color = Color.BLUE
    }

    // 绘制原点的画笔
    private val originPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        color = Color.RED
    }

    // 绘制凸包边界的画笔
    private val linePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        color = Color.RED
        strokeWidth = 5f
    }

    // 填充凸包区域的画笔
    private val fillPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        color = Color.argb(50, 255, 0, 0)  // 半透明红色
    }

    // 绘制坐标轴的画笔
    private val axisPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        color = Color.BLACK
        strokeWidth = 2f
    }

    // 绘制文本的画笔
    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.BLACK
        textSize = 30f
    }

    // 坐标系边界值
    private var boundaryValue = 10f

    /**
     * 设置坐标系边界值
     * @param value 新的边界值
     */
    fun setBoundary(value: Float) {
        boundaryValue = value
        invalidate()  // 重绘视图
    }

    /**
     * 添加新的点到视图中
     * @param point 要添加的新点
     */
    fun addPoint(point: Point) {
        // 检查点是否在边界内
        if (point.x in -boundaryValue..boundaryValue && point.y in -boundaryValue..boundaryValue) {
            points.add(point)
            calculateConvexHull()  // 重新计算凸包
            invalidate()  // 重绘视图
        }
    }

    /**
     * 清除所有点
     */
    fun clearPoints() {
        points.clear()
        hullPoints.clear()
        invalidate()  // 重绘视图
    }

    /**
     * 计算凸包
     * 使用Graham扫描算法
     */
    private fun calculateConvexHull() {
        val allPoints = listOf(Point(0f, 0f)) + points.getElements()
        if (allPoints.size <= 2) {
            // 如果点少于3个，所有点都是凸包
            hullPoints.clear()
            hullPoints.addAll(allPoints)
            return
        }

        // 找到y坐标最小的点（如果有多个，选择x坐标最小的）
        val start = allPoints.minWith(compareBy({ it.y }, { it.x }))

        // 按照相对于起始点的极角排序所有点
        val sortedPoints = allPoints.sortedWith(Comparator { a, b ->
            val angleA = atan2((a.y - start.y), (a.x - start.x))
            val angleB = atan2((b.y - start.y), (b.x - start.x))
            when {
                angleA < angleB -> -1
                angleA > angleB -> 1
                else -> {
                    // 如果角度相同，按距离排序
                    val distA = (a.x - start.x) * (a.x - start.x) + (a.y - start.y) * (a.y - start.y)
                    val distB = (b.x - start.x) * (b.x - start.x) + (b.y - start.y) * (b.y - start.y)
                    distA.compareTo(distB)
                }
            }
        })

        // Graham扫描算法的主要部分
        hullPoints.clear()
        hullPoints.add(sortedPoints[0])
        hullPoints.add(sortedPoints[1])

        for (i in 2 until sortedPoints.size) {
            while (hullPoints.size >= 2 && !isLeftTurn(
                    hullPoints[hullPoints.size - 2],
                    hullPoints.last(),
                    sortedPoints[i]
                )
            ) {
                hullPoints.removeAt(hullPoints.size - 1)
            }
            hullPoints.add(sortedPoints[i])
        }
    }

    /**
     * 判断三个点是否形成一个左转
     * @return 如果是左转返回true，否则返回false
     */
    private fun isLeftTurn(a: Point, b: Point, c: Point): Boolean {
        return ((b.x - a.x) * (c.y - a.y) - (b.y - a.y) * (c.x - a.x)) > 0
    }

    /**
     * 绘制视图
     */
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // 计算视图的实际大小和偏移量
        val size = min(width, height).toFloat()
        val offsetX = (width - size) / 2
        val offsetY = (height - size) / 2

        // 绘制坐标系
        drawCoordinateSystem(canvas, size, offsetX, offsetY)

        // 绘制凸包或线段
        when (hullPoints.size) {
            0, 1 -> {
                return
            }
            2 -> {
                // 如果只有两个点，画一条线
                val start = hullPoints[0]
                val end = hullPoints[1]
                canvas.drawLine(
                    mapToViewX(start.x, size) + offsetX,
                    mapToViewY(start.y, size) + offsetY,
                    mapToViewX(end.x, size) + offsetX,
                    mapToViewY(end.y, size) + offsetY,
                    linePaint
                )
            }
            else -> {
                // 绘制完整的凸包
                val path = Path()
                path.moveTo(mapToViewX(hullPoints[0].x, size) + offsetX, mapToViewY(hullPoints[0].y, size) + offsetY)
                for (i in 1 until hullPoints.size) {
                    path.lineTo(mapToViewX(hullPoints[i].x, size) + offsetX, mapToViewY(hullPoints[i].y, size) + offsetY)
                }
                path.close()
                canvas.drawPath(path, fillPaint)
                canvas.drawPath(path, linePaint)
            }
        }

        // 绘制所有点
        val allPoints = listOf(Point(0f, 0f)) + points.getElements()
        for (point in allPoints) {
            val x = mapToViewX(point.x, size) + offsetX
            val y = mapToViewY(point.y, size) + offsetY
            if (point.x == 0f && point.y == 0f) {
                canvas.drawCircle(x, y, 12f, originPaint)
            } else {
                canvas.drawCircle(x, y, 10f, pointPaint)
            }
        }
    }

    /**
     * 绘制坐标系
     */
    private fun drawCoordinateSystem(canvas: Canvas, size: Float, offsetX: Float, offsetY: Float) {
        // 绘制x轴和y轴
        canvas.drawLine(offsetX, size / 2 + offsetY, size + offsetX, size / 2 + offsetY, axisPaint)
        canvas.drawLine(size / 2 + offsetX, offsetY, size / 2 + offsetX, size + offsetY, axisPaint)

        // 绘制刻度和标签
        val stepSize = boundaryValue / 5
        for (i in -5..5) {
            if (i == 0) continue  // 跳过原点
            val value = i * stepSize
            val x = mapToViewX(value, size) + offsetX
            val y = mapToViewY(value, size) + offsetY
            // 绘制x轴刻度
            canvas.drawLine(x, size / 2 + offsetY - 10, x, size / 2 + offsetY + 10, axisPaint)
            // 绘制y轴刻度
            canvas.drawLine(size / 2 + offsetX - 10, y, size / 2 + offsetX + 10, y, axisPaint)
            // 绘制x轴标签
            canvas.drawText(String.format("%.1f", value), x, size / 2 + offsetY + 40, textPaint)
            // 绘制y轴标签
            canvas.drawText(String.format("%.1f", value), size / 2 + offsetX - 70, y + 10, textPaint)
        }
    }

    /**
     * 将x坐标从数学坐标系映射到视图坐标系
     */
    private fun mapToViewX(x: Float, size: Float): Float {
        return (x / boundaryValue + 1) * size / 2
    }

    /**
     * 将y坐标从数学坐标系映射到视图坐标系
     */
    private fun mapToViewY(y: Float, size: Float): Float {
        return size - ((y / boundaryValue + 1) * size / 2)
    }

    /**
     * 表示二维平面上的一个点
     */
    data class Point(val x: Float, val y: Float)
}