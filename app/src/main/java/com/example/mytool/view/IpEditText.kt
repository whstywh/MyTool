package com.example.mytool.view

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.KeyEvent
import android.widget.LinearLayout
import android.view.LayoutInflater
import android.widget.EditText
import com.example.mytool.R
import kotlinx.android.synthetic.main.layout_ip_edittext.view.*


/**
 * Created by whstywh on 2019/2/14.
 * description：自定义ip输入框
 */
class IpEditText @JvmOverloads constructor(context: Context, attr: AttributeSet? = null) :
    LinearLayout(context, attr) {

    private var text1: String = ""
    private var text2: String = ""
    private var text3: String = ""
    private var text4: String = ""


    init {
        LayoutInflater.from(context).inflate(R.layout.layout_ip_edittext, this)

        init()
    }

    private fun init() {
        edit1.addTextChangedListener(onText = { p0, _, _, _ ->

            text1 = p0.toString().trim()
            if (text1.length > 2) {
                if (text1.toInt() > 255) {
//                    Toast.makeText(context, "请输入合法的ip地址", Toast.LENGTH_LONG).show()
                } else {
                    edit2.isFocusable = true
                    edit2.requestFocus()
                }
            }
        })

        edit2.addTextChangedListener(onText = { p0, _, _, _ ->

            text2 = p0.toString().trim()
            if (text2.length > 2) {
                if (text2.toInt() > 255) {
//                    Toast.makeText(context, "请输入合法的ip地址", Toast.LENGTH_LONG).show()
                } else {
                    edit3.isFocusable = true
                    edit3.requestFocus()
                }
            }

        })

        edit3.addTextChangedListener(onText = { p0, _, _, _ ->

            text3 = p0.toString().trim()
            if (text3.length > 2) {
                if (text3.toInt() > 255) {
//                    Toast.makeText(context, "请输入合法的ip地址", Toast.LENGTH_LONG).show()
                } else {
                    edit4.isFocusable = true
                    edit4.requestFocus()
                }
            }

        })
        edit4.addTextChangedListener(onText = { p0, _, _, _ ->

            text4 = p0.toString().trim()
            if (text4.length > 2) {
                if (text4.toInt() > 255) {
//                    Toast.makeText(context, "请输入合法的ip地址", Toast.LENGTH_LONG).show()
                }
            }

        })


        edit2.setOnKeyListener { _, keyCode, _ ->
            if (text2.isEmpty()) {
                if (keyCode == KeyEvent.KEYCODE_DEL) {
                    edit1.isFocusable = true
                    edit1.requestFocus()
                }
            }
            return@setOnKeyListener false
        }
        edit3.setOnKeyListener { _, keyCode, _ ->
            if (text3.isEmpty()) {
                if (keyCode == KeyEvent.KEYCODE_DEL) {
                    edit2.isFocusable = true
                    edit2.requestFocus()
                }
            }
            return@setOnKeyListener false
        }
        edit4.setOnKeyListener { _, keyCode, _ ->
            if (text4.isEmpty()) {
                if (keyCode == KeyEvent.KEYCODE_DEL) {
                    edit3.isFocusable = true
                    edit3.requestFocus()
                }
            }
            return@setOnKeyListener false
        }


    }


    private inline fun EditText.addTextChangedListener(
        crossinline after: (p0: Editable?) -> Unit = {},
        crossinline before: (p0: CharSequence?, p1: Int, p2: Int, p3: Int) -> Unit = { _, _, _, _ ->
        },
        crossinline onText: (p0: CharSequence?, p1: Int, p2: Int, p3: Int) -> Unit = { _, _, _, _ -> }
    ) {
        addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                after(p0)
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                before(p0, p1, p2, p3)
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                onText(p0, p1, p2, p3)
            }
        })
    }


    fun getText() = "$text1:$text2:$text3:$text4"


    fun setText(t: String) {
        val s = t.split(":")
        edit1.setText(s[0])
        edit2.setText(s[1])
        edit3.setText(s[2])
        edit4.setText(s[3])
    }

}