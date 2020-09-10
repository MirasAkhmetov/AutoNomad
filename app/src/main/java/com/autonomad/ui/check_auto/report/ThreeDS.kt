package com.autonomad.ui.check_auto.report

interface ThreeDS {
    fun error()
    fun success(md: String?, paRes: String?)
}