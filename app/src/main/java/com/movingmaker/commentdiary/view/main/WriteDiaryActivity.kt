package com.movingmaker.commentdiary.view.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.movingmaker.commentdiary.databinding.ActivityWritediaryWritediaryBinding

class WriteDiaryActivity: AppCompatActivity() {

    private lateinit var binding: ActivityWritediaryWritediaryBinding
    private lateinit var diaryTypeBottomSheet: SelectDiaryTypeBottomSheet
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityWritediaryWritediaryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        diaryTypeBottomSheet = SelectDiaryTypeBottomSheet()
        diaryTypeBottomSheet.show(supportFragmentManager,"selectDiaryBottomSheet")


    }
}