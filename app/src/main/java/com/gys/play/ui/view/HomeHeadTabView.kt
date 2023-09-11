package com.gys.play.ui.view

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.viewpager2.widget.ViewPager2
import com.mybase.libb.ext.dp2px
import com.mybase.libb.ext.getAppGlobal
import com.mybase.libb.ext.showGONE
import com.mybase.libb.ext.showINVISIBLE
import com.gys.play.MyApplication
import com.gys.play.R
import com.gys.play.util.KvUserInfo
import com.gys.play.util.boy
import com.gys.play.util.getBoyColor


class HomeHeadTabView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {


    private var position = -1

    private val tvs = mutableListOf<TextView>()

    private var vp2: ViewPager2? = null

    private var ivTag: ImageView? = null

    private var ivWelfare: ImageView? = null
    private var ivVip: ImageView? = null

    private var boyIndex = -1
    private var baoIndex = -1
    private var boyName = ""
    private var girlName = ""

    init {

        if (context is LifecycleOwner) {
            KvUserInfo.lookTypeLd.observe(context) {
                changeLookType(it)
            }
        }
    }


    fun setData(
        vp2: ViewPager2,
        boy: Int,
        bao: Int,
        girlName: String,
        titles: MutableList<String>
    ) {

        removeAllViews()
        tvs.clear()

        if (boy >= 0) {
            this.girlName = girlName
            this.boyName = titles[boy]
        }


        this.vp2 = vp2

        boyIndex = boy
        baoIndex = bao

        titles.forEachIndexed { index, s ->
            val inflate = LayoutInflater.from(context).inflate(R.layout.tab_home_head, null)
            val tvName = inflate.findViewById<TextView>(R.id.tvName)
            tvName.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14f)
            tvName.text = s
            tvName.setTextColor(ContextCompat.getColor(getAppGlobal(), R.color.C_333333))
            tvs.add(tvName)
            if (index == boy) {
                ivTag = inflate.findViewById(R.id.ivTag)
            } else if (index == titles.size - 1) {
                ivWelfare = inflate.findViewById(R.id.ivTag)
                ivWelfare?.setImageResource(R.mipmap.icon_changing_fuli)
            } else if (index == titles.size - 2) {
                ivVip = inflate.findViewById(R.id.ivTag)
                ivVip?.setImageResource(R.mipmap.icon_changing_vip)
            }
            inflate.setOnClickListener {
                if (position != index) {
                    MyApplication.getAnalytics().setIcon("$s 点击量")
                    setIndex(index)
                } else if (position == boyIndex) {
                    val lookBoy = KvUserInfo.lookBoy
                    KvUserInfo.setChangeLook(if (lookBoy) 2 else 1)
                }
            }

            addView(inflate, ViewGroup.LayoutParams(-2, 35f.dp2px()))
        }

        KvUserInfo.lookTypeLd.value?.let {
            changeLookType(it)
        }


        if (bao >= 0)
            tvs[bao].showGONE(KvUserInfo.lookBoy)
        if (boy >= 0)
            tvs[boy].text = if (KvUserInfo.lookBoy) boyName else girlName

    }


    private fun change() {
        tvs.forEachIndexed { index, child ->
            if (position == index) {
                child.typeface = Typeface.defaultFromStyle(Typeface.BOLD)
                child.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 22f)
            } else {
                child.typeface = Typeface.defaultFromStyle(Typeface.NORMAL)
                child.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14f)
            }

            if (boyIndex == index) {  //男女生变色
                child.setTextColor(
                    ContextCompat.getColor(
                        getAppGlobal(),
                        if (position == index) R.color.C_2997FD.boy(R.color.C_FD7BA9) else R.color.C_333333
                    )
                )
                ivTag?.showINVISIBLE(position == index)
                ivTag?.setImageResource(R.mipmap.icon_changing_b.boy(R.mipmap.icon_changing_g))
            }

            if (index == tvs.size - 1) {  //福利中心
                child.setTextColor(
                    ContextCompat.getColor(
                        getAppGlobal(),
                        if (position == index) R.color.C_FF7F40 else R.color.C_333333
                    )
                )
                ivWelfare?.showINVISIBLE(position == index)
            }

            if (index == tvs.size - 2) {  //VIP
                child.setTextColor(
                    ContextCompat.getColor(
                        getAppGlobal(),
                        if (position == index) R.color.C_FCAC0B else R.color.C_333333
                    )
                )
                ivVip?.showINVISIBLE(position == index)
            }


        }
    }

    private fun changeLookType(lookBoy: Boolean) {
        if (boyIndex < 0) {
            return
        }

        tvs[boyIndex].text = if (lookBoy) boyName else girlName
        MyApplication.getAnalytics()
            .setIcon("${if (lookBoy) boyName else girlName} 点击量")
        ivTag?.setImageResource(R.mipmap.icon_changing_b.boy(R.mipmap.icon_changing_g))
        tvs[boyIndex].setTextColor(getBoyColor())
        if (baoIndex >= 0)
            tvs[baoIndex].showGONE(lookBoy)
    }


    fun setIndex(index: Int) {
        if (position != index) {
            position = index
            change()
            try {
                vp2?.setCurrentItem(index, false)
            } catch (err: Throwable) {
                err.printStackTrace()
            }
        }

    }
}