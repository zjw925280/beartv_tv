package com.gys.play.adapter

interface BookShelfListener {
    fun selectBookNum(allNum: Int, selCount: Int, idMap: HashMap<Int, Int>)
}