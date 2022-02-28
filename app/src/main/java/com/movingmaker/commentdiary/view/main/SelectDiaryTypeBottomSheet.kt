package com.movingmaker.commentdiary.view.main

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.movingmaker.commentdiary.R
import com.movingmaker.commentdiary.databinding.BottomSheetFragmentWritediaryTypeselectBinding

class SelectDiaryTypeBottomSheet: BottomSheetDialogFragment() {

    private lateinit var binding: BottomSheetFragmentWritediaryTypeselectBinding

    @SuppressLint("ResourceType")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = BottomSheetFragmentWritediaryTypeselectBinding.inflate(inflater)
        val view = inflater.inflate(R.layout.bottom_sheet_fragment_writediary_typeselect,container,false)

        initViews()


        return view
    }

    private fun initViews()= with(binding){

        selectCommentDiary.setOnClickListener {

        }
        selectAloneDiary.setOnClickListener {

        }

        submitButton.setOnClickListener {

        }
    }
}