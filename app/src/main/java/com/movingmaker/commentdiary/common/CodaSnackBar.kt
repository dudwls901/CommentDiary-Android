package com.movingmaker.commentdiary.common

import android.view.LayoutInflater
import android.view.View
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.google.android.material.snackbar.Snackbar
import com.movingmaker.commentdiary.R
import com.movingmaker.commentdiary.databinding.SnackbarCodaBinding

class CodaSnackBar(view: View, private val message: String) {

    companion object {

        fun make(view: View, message: String) = CodaSnackBar(view, message)
    }

    private val context = view.context
    private val snackbar = Snackbar.make(view, "", Snackbar.LENGTH_SHORT)
    private val snackbarLayout = snackbar.view as Snackbar.SnackbarLayout

    private val inflater = LayoutInflater.from(context)
    private val snackbarBinding: SnackbarCodaBinding = DataBindingUtil.inflate(inflater, R.layout.snackbar_coda, null, false)

    init {
        initView()
        initData()
    }

    private fun initView() {
        with(snackbarLayout) {
//            snackbar.animationMode = ANIMATION_MODE_SLIDE
            snackbar.animationMode = Snackbar.ANIMATION_MODE_FADE
            setPadding(0, 0, 0, 0)
            setBackgroundColor(ContextCompat.getColor(context, android.R.color.transparent))
            snackbar.setBackgroundTint(ContextCompat.getColor(context, android.R.color.transparent))
            addView(snackbarBinding.root, 0)
        }

        snackbarBinding.root.setOnClickListener {
            snackbar.dismiss()
        }
    }

    private fun initData() {
        snackbarBinding.tvSample.text = message
    }

    fun show() {
        snackbar.show()
    }
}