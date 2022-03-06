package com.movingmaker.commentdiary.view.main.mydiary

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.movingmaker.commentdiary.R
import com.movingmaker.commentdiary.databinding.BottomSheetFragmentWritediaryTypeselectBinding
import com.movingmaker.commentdiary.view.main.mydiary.CalendarWithDiaryFragment.Companion.TAG
import com.movingmaker.commentdiary.viewmodel.mydiary.MyDiaryViewModel

class SelectDiaryTypeBottomSheet: BottomSheetDialogFragment() {

    private lateinit var binding: BottomSheetFragmentWritediaryTypeselectBinding
    private val myDiaryViewModel: MyDiaryViewModel by activityViewModels()

    companion object{
        fun newInstance(): SelectDiaryTypeBottomSheet{
            return SelectDiaryTypeBottomSheet()
        }
    }

    @SuppressLint("ResourceType")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = BottomSheetFragmentWritediaryTypeselectBinding.inflate(inflater,container, false)
        binding.myDiaryviewModel = myDiaryViewModel
        binding.lifecycleOwner = viewLifecycleOwner


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        myDiaryViewModel.setDiaryType(-1)
        initViews()
    }

    @SuppressLint("ResourceAsColor")
    private fun initViews()= with(binding){

        selectAloneDiaryButton.setOnClickListener {
            myDiaryViewModel.setDiaryType(0)
            Toast.makeText(requireContext(), myDiaryViewModel.diaryType.value.toString(), Toast.LENGTH_SHORT).show()
//            selectAloneDiaryButton.setBackgroundColor(R.color.brand_bright_green)
        }
        selectCommentDiaryButton.setOnClickListener {
            myDiaryViewModel.setDiaryType(1)
            Toast.makeText(requireContext(), myDiaryViewModel.diaryType.value.toString(), Toast.LENGTH_SHORT).show()
//            selectAloneDiaryButton.setBackgroundColor(R.color.brand_bright_green)
        }

        submitButton.setOnClickListener {
            //alonediary
            Log.d(TAG, "initViews: ${myDiaryViewModel.diaryType.value}")
            if(myDiaryViewModel.diaryType.value==0){
                dismiss()
            }
            //commentdiary
            else if(myDiaryViewModel.diaryType.value==1){
                dismiss()
            }
        }
    }
}
