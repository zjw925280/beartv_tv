package com.gys.play.entity


interface MoreListener {
    /**
     *@param data  数据
     * @param position 位置
     * @param type  类型比如加载更多及其他的扩展
     */
    fun more(data: BtVideoInfo, position: Int, type: Int)
}