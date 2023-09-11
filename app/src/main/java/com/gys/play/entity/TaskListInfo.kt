package com.gys.play.entity

data class TaskListInfo(
    val day_task: List<WelfareInfo>,
    val newbie_task: List<WelfareInfo>,
    val reward_task: List<WelfareInfo>
)