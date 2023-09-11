package com.gys.play.util

import com.android.liba.util.MyLiveData
import com.gys.play.entity.UserInfo

object LiveDataUtil {

    val mainActivityTabIndex = MyLiveData<Int>()
    val mainActivityTabVisible = MyLiveData<Boolean>()
    val mainHomeGoTop = MyLiveData<Boolean>()
    val mainHomeGoTopClick = MyLiveData<Boolean>()
    val isVip = MyLiveData<Boolean>()
    val isLogin = MyLiveData<Boolean>()
    val categoryHeadHeight = MyLiveData<Int>()
    val userInfo = MyLiveData(UserInfo())
    val homeTabType = MyLiveData<Int>()//0:新人必读，1：男生，2：女生，3：劲爆，4：VIP，5：福利中心，6：分类
}