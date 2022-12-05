package com.movingmaker.presentation.view.main.gatherdiary

import com.movingmaker.domain.model.response.Diary

interface OnDiarySelectListener {
    fun onDiarySelectListener(diary: Diary)
}