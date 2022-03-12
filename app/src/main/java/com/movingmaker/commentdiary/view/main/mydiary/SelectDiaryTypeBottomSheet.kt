package com.movingmaker.commentdiary.view.main.mydiary

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.movingmaker.commentdiary.R
import com.movingmaker.commentdiary.databinding.BottomSheetFragmentWritediaryTypeselectBinding
import com.movingmaker.commentdiary.view.main.mydiary.CalendarWithDiaryFragment.Companion.TAG
import com.movingmaker.commentdiary.viewmodel.mydiary.MyDiaryViewModel

class SelectDiaryTypeBottomSheet(private val selectDiaryTypeListener: SelectDiaryTypeListener)
    : BottomSheetDialogFragment() {

    private lateinit var binding: BottomSheetFragmentWritediaryTypeselectBinding
    private val myDiaryViewModel: MyDiaryViewModel by activityViewModels()


    @SuppressLint("ResourceType")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = BottomSheetFragmentWritediaryTypeselectBinding.inflate(inflater,container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
    }

    @SuppressLint("ResourceAsColor")
    private fun initViews()= with(binding){

        selectAloneDiaryButton.setOnClickListener {
            binding.selectAloneDiaryButton.background = ContextCompat.getDrawable(requireContext(),R.color.brand_bright_green)
            binding.selectCommentDiaryButton.background = ContextCompat.getDrawable(requireContext(),R.color.background_ivory)
            myDiaryViewModel.selectedDiary.value!!.deliveryYN='N'
            selectDiaryTypeListener.onSelectDiaryTypeListener('N')
        }
        selectCommentDiaryButton.setOnClickListener {
            binding.selectAloneDiaryButton.background = ContextCompat.getDrawable(requireContext(),R.color.background_ivory)
            binding.selectCommentDiaryButton.background = ContextCompat.getDrawable(requireContext(),R.color.brand_bright_green)
            myDiaryViewModel.selectedDiary.value!!.deliveryYN='Y'
            selectDiaryTypeListener.onSelectDiaryTypeListener('Y')
        }

        submitButton.setOnClickListener {
            Log.d(TAG, "initViews: ${myDiaryViewModel.deliveryYN.value}")
            if(myDiaryViewModel.selectedDiary.value!!.deliveryYN!=' '){
                dismiss()
            }
        }
    }
}
