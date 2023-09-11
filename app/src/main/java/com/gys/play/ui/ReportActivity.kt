package com.gys.play.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import com.android.liba.context.AppContext
import com.gys.play.R
import com.gys.play.adapter.ReportAdapter
import com.gys.play.databinding.ActivityReportBinding
import com.mybase.libb.ui.vb.BaseVbActivity

/**activity_report
 * @Description : 描述
 */
class ReportActivity : BaseVbActivity<ActivityReportBinding>() {
    var faGuiList = mutableListOf<String>()
    var quanYiList = mutableListOf<String>()
    var sheQuList = mutableListOf<String>()
    var pbQuList = mutableListOf<String>()
    var faGuiAdapter: ReportAdapter? = null
    var quanYiAdapter: ReportAdapter? = null
    var sheQuAdapter: ReportAdapter? = null
    var pbQuAdapter: ReportAdapter? = null
    var reportContent = ""
    companion object {
        fun start(context: Context) {
            context?.run {
                val intent = Intent(this, ReportActivity::class.java)
                startActivity(intent)
            }
        }
    }
    override fun initView(savedInstanceState: Bundle?) {

        mBind.rvFagui.layoutManager = GridLayoutManager(this, 2)
        mBind.rvQuanyi.layoutManager = GridLayoutManager(this, 2)
        mBind.rvShequ.layoutManager = GridLayoutManager(this, 2)
        mBind.rvPb.layoutManager = GridLayoutManager(this, 2)
        faGuiList = mutableListOf(
            getString(R.string.report_type1_1), getString(R.string.report_type1_2), getString(
                R.string.report_type1_3
            ), getString(R.string.report_type1_4)
        )
        quanYiList =
            mutableListOf(getString(R.string.report_type2_1), getString(R.string.report_type2_2))
        sheQuList = mutableListOf(
            getString(R.string.report_type3_1), getString(R.string.report_type3_2), getString(
                R.string.report_type3_3
            ), getString(R.string.report_type3_4), getString(R.string.report_type3_5), getString(
                R.string.report_type3_6),getString(R.string.report_type3_7),getString(R.string.report_type3_8)
        )
        pbQuList = mutableListOf(
            getString(R.string.report_type3_7),getString(R.string.report_type3_8)
        )
        faGuiAdapter = ReportAdapter(faGuiList, 0)
        mBind.rvFagui.adapter = faGuiAdapter

        quanYiAdapter = ReportAdapter(quanYiList, 1)
        mBind.rvQuanyi.adapter = quanYiAdapter

        sheQuAdapter = ReportAdapter(sheQuList, 2)
        mBind.rvShequ.adapter = sheQuAdapter
        pbQuAdapter = ReportAdapter(pbQuList, 3)
        mBind.rvPb.adapter = pbQuAdapter
        faGuiAdapter?.listener = listener
        quanYiAdapter?.listener = listener
        sheQuAdapter?.listener = listener
        pbQuAdapter?.listener = listener
        mBind.rlBack.setOnClickListener {
            finish()
        }

        mBind.reportBtn.setOnClickListener {
            if (reportContent.equals("")) {
                AppContext.showToast("請選擇舉報內容")
                return@setOnClickListener
            }
            AppContext.showToast(getString(R.string.report_done))
            finish()
        }
    }
    val listener = object : ReportAdapter.OnSetSelClickListener {
        override fun onSelClickListener(type: Int, content: String) {
            when (type) {
                0 -> {
                    quanYiAdapter?.refreash()
                    sheQuAdapter?.refreash()
                    pbQuAdapter?.refreash()
                }
                1 -> {
                    faGuiAdapter?.refreash()
                    sheQuAdapter?.refreash()
                    pbQuAdapter?.refreash()
                }
                2 -> {
                    faGuiAdapter?.refreash()
                    quanYiAdapter?.refreash()
                    pbQuAdapter?.refreash()
                }
                3->{
                    faGuiAdapter?.refreash()
                    quanYiAdapter?.refreash()
                    sheQuAdapter?.refreash()
                }
            }
            reportContent = content
        }
    }
}