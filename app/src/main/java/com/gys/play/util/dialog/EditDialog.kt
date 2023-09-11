package com.gys.play.util.dialog

import android.content.Context
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import com.jccppp.dialog.JustDialog
import com.jccppp.dialog.addView
import com.gys.play.R

class EditDialog(
    context: Context,
    val title: String? = null,
    val content: String? = null,
    val leftText: String? = null,
    val rightText: String? = null,
    val editMaxLength: Int = 0,
    val leftClickListener: View.OnClickListener? = null,
    val rightClickListener: View.OnClickListener? = null,
    val layout: Int = R.layout.dialog_edit_name
) : JustDialog(context) {

    init {

        addView(layout).setWidth(0.83f)

        initContentView()
    }

    var edit: EditText? = null
    fun initContentView(){
        val tvTitle = view.findViewById<TextView>(R.id.tvTitle)
        if (title.isNullOrBlank()) {
            tvTitle.visibility = View.GONE
        } else {
            tvTitle.text = title
        }
        initEdit(view)
        val tvLeft = view.findViewById<TextView>(R.id.tv_left)
        leftText?.let { tvLeft?.text = it }
        tvLeft?.let {
            it.setOnClickListener {
                dismiss()
                leftClickListener?.let { it.onClick(tvLeft) }
            }
        }
        val tvRight = view.findViewById<TextView>(R.id.tv_right)
        rightText?.let { tvRight?.text = it }
        tvRight?.let {
            it.setOnClickListener {
                dismiss()
                rightClickListener?.let { it.onClick(tvLeft) }
            }
        }
    }

    private fun initEdit(view: View) {
        val clear = view.findViewById<ImageView>(R.id.clear)
        val editLength = view.findViewById<TextView>(R.id.editLength)
        clear?.visibility = View.INVISIBLE
        edit = view.findViewById<EditText>(R.id.edit)
        edit?.setText(content)
        edit?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
                clear?.visibility = if (p0.isNullOrEmpty()) View.INVISIBLE else View.VISIBLE
                editLength?.text = "${p0?.length}"
            }
        })
        clear?.setOnClickListener { edit?.setText("") }
        edit?.setFilters(arrayOf<InputFilter>(InputFilter.LengthFilter(editMaxLength)))

    }

    fun getEditText() = edit?.text.toString()

}