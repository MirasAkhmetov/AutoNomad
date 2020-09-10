package com.autonomad.utils

import android.view.KeyEvent
import android.view.View
import android.widget.EditText
import androidx.core.widget.doAfterTextChanged
import kotlinx.android.synthetic.main.layout_uin.view.*

class UinController(uinLayout: View, private var onUinEntered: ((String?) -> Unit)? = null) {

    private val fields: List<EditText> = listOf(
        uinLayout.et_uin_first,
        uinLayout.et_uin_second,
        uinLayout.et_uin_third,
        uinLayout.et_uin_fourth,
        uinLayout.et_uin_fifth,
        uinLayout.et_uin_sixth,
        uinLayout.et_uin_seventh,
        uinLayout.et_uin_eighth,
        uinLayout.et_uin_ninth,
        uinLayout.et_uin_tenth,
        uinLayout.et_uin_eleventh,
        uinLayout.et_uin_twelfth
    )

    private val onEditText: (EditText) -> Unit = { et ->
        et.doAfterTextChanged {
            if (it.toString().length == 1) {
                if (checkUin()) onUinEntered?.invoke(getUin())
                else {
                    onUinEntered?.invoke(null)
                    resetFocus()
                }
            }
        }
        et.setOnKeyListener { v, keyCode, event ->
            val editText = v as? EditText
            if (keyCode == KeyEvent.KEYCODE_DEL && event.action == KeyEvent.ACTION_DOWN) {
                val index = fields.indexOf(editText)
                if (index > 0) {
                    fields[if (index == fields.lastIndex && fields[index].text.isNotEmpty()) index else index - 1].text.clear()
                    resetFocus()
                }
                return@setOnKeyListener true
            }
            false
        }
    }

    init {
        fields.forEach(onEditText)
        resetFocus()
    }

    private var focusedPos: Int? = null
    private fun resetFocus() {
        for ((index, editText) in fields.withIndex()) {
            if (editText.text.isEmpty()) {
                focusedPos = index
                editText.requestFocus()
                break
            }
        }
        fields.forEachIndexed { index, editText ->
            if (index == (focusedPos ?: fields.lastIndex)) editText.onFocusChangeListener = null
            else editText.setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus) fields[focusedPos ?: fields.lastIndex].requestFocus()
            }
        }
    }

    fun getUin(): String? {
        var result = ""
        for (et in fields) {
            result += et.text
        }
        return if (result.length == 12) result else null
    }

    private fun checkUin(): Boolean {
        for (c in fields)
            if (c.text.isEmpty())
                return false
        return true
    }

    @Deprecated("Pass listener to constructor")
    fun setOnUinEntered(onEntered: (String?) -> Unit) {
        onUinEntered = onEntered
    }
}