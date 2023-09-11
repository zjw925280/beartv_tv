package com.gys.play.entity

class RankIndex(
    val most_read: MutableList<BtVideoInfo>,
    val newest: MutableList<BtVideoInfo>,
    val most_favors: MutableList<BtVideoInfo>
)