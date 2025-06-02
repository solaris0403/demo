package com.example.view

/**
 * 固定容量队列
 * 当队列达到最大容量时，添加新元素会自动移除最旧的元素
 * @param T 队列中元素的类型
 * @param capacity 队列的最大容量
 */
class FixedSizeQueue<T>(private val capacity: Int) {
    private val deque: ArrayDeque<T> = ArrayDeque(capacity)

    /**
     * 添加元素到队列
     * 如果队列已满，会先移除最旧的元素
     * @param element 要添加的元素
     */
    fun add(element: T) {
        if (deque.size >= capacity) {
            deque.removeFirst()
        }
        deque.addLast(element)
    }

    /**
     * 获取队列中的所有元素
     * @return 包含所有元素的列表
     */
    fun getElements(): List<T> {
        return deque.toList()
    }

    /**
     * 获取队列当前的大小
     * @return 队列中元素的数量
     */
    fun size(): Int {
        return deque.size
    }

    /**
     * 检查队列是否为空
     * @return 如果队列为空则返回true，否则返回false
     */
    fun isEmpty(): Boolean {
        return deque.isEmpty()
    }

    /**
     * 检查队列是否已满
     * @return 如果队列已达到最大容量则返回true，否则返回false
     */
    fun isFull(): Boolean {
        return deque.size == capacity
    }

    /**
     * 清空队列
     */
    fun clear() {
        deque.clear()
    }
}