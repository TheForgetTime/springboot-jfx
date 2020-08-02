package top.lhx.springbootjfx.util

import javafx.scene.Cursor
import javafx.scene.Node
import javafx.stage.Stage

object DrawUtil {
    private var xOffset = 0.0
    private var yOffset = 0.0
    private var isTop // 是否处于上边界移动窗口状态
            = false

    //窗体拉伸属性
    private var isRight // 是否处于右边界调整窗口状态
            = false
    private var isBottomRight // 是否处于右下角调整窗口状态
            = false
    private var isBottom // 是否处于下边界调整窗口状态
            = false
    private const val RESIZE_WIDTH// 判定是否为调整窗口状态的范围与边界距离
            = 5
    private const val MIN_WIDTH  // 窗口最小宽度
            = 800.0
    private const val MIN_HEIGHT  // 窗口最小高度
            = 600.0

    fun addDrawFunc(stage: Stage, root: Node) {
        root.setOnMousePressed { event ->
            xOffset = event.sceneX
            yOffset = event.sceneY
        }
        root.setOnMouseMoved { event ->
            event.consume()
            val x: Double = event.sceneX
            val y: Double = event.sceneY
            val width: Double = stage.width
            val height: Double = stage.height
            var cursorType: Cursor = Cursor.DEFAULT // 鼠标光标初始为默认类型，若未进入调整窗口状态，保持默认类型
            // 先将所有调整窗口状态重置
            isBottom = false
            isBottomRight = isBottom
            isRight = isBottomRight
            isTop = false
            if (y >= height - RESIZE_WIDTH) {
                when {
                    x <= RESIZE_WIDTH -> { // 左下角调整窗口状态
                    }
                    x >= width - RESIZE_WIDTH -> { // 右下角调整窗口状态
                        isBottomRight = true
                        cursorType = Cursor.SE_RESIZE
                    }
                    else -> { // 下边界调整窗口状态
                        isBottom = true
                        cursorType = Cursor.S_RESIZE
                    }
                }
            } else if (x >= width - RESIZE_WIDTH) { // 右边界调整窗口状态
                isRight = true
                cursorType = Cursor.E_RESIZE
            } else if (y <= 3) {
                isTop = true
                cursorType = Cursor.MOVE
            }
            // 最后改变鼠标光标
            root.cursor = cursorType
        }
        root.setOnMouseDragged { event ->
            val x: Double = event.sceneX
            val y: Double = event.sceneY
            // 保存窗口改变后的x、y坐标和宽度、高度，用于预判是否会小于最小宽度、最小高度
            var nextX: Double = stage.x
            var nextY: Double = stage.y
            var nextWidth: Double = stage.width
            var nextHeight: Double = stage.height
            if (isTop) {
                nextX = event.screenX - xOffset
                nextY = event.screenY - yOffset
            }
            if (isRight || isBottomRight) { // 所有右边调整窗口状态
                nextWidth = x
            }
            if (isBottomRight || isBottom) { // 所有下边调整窗口状态
                nextHeight = y
            }
            if (nextWidth <= MIN_WIDTH) { // 如果窗口改变后的宽度小于最小宽度，则宽度调整到最小宽度
                nextWidth = MIN_WIDTH
            }
            if (nextHeight <= MIN_HEIGHT) { // 如果窗口改变后的高度小于最小高度，则高度调整到最小高度
                nextHeight = MIN_HEIGHT
            }
            // 最后统一改变窗口的x、y坐标和宽度、高度，可以防止刷新频繁出现的屏闪情况
            stage.x = nextX
            stage.y = nextY
            stage.width = nextWidth
            stage.height = nextHeight
        }
    }
}
