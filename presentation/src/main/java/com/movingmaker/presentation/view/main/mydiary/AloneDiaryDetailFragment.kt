package com.movingmaker.presentation.view.main.mydiary

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.movingmaker.presentation.R
import com.movingmaker.presentation.base.BaseFragment
import com.movingmaker.presentation.databinding.FragmentMydiaryAlonediaryDetailBinding
import com.movingmaker.presentation.util.FRAGMENT_NAME
import com.movingmaker.presentation.viewmodel.FragmentViewModel
import com.movingmaker.presentation.viewmodel.mydiary.MyDiaryViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class AloneDiaryDetailFragment :
    BaseFragment<FragmentMydiaryAlonediaryDetailBinding>(R.layout.fragment_mydiary_alonediary_detail) {
    private val myDiaryViewModel: MyDiaryViewModel by activityViewModels()
    private val fragmentViewModel: FragmentViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.vm = myDiaryViewModel
        fragmentViewModel.setCurrentFragment(FRAGMENT_NAME.ALONE_DIARY_DETAIL)

        initViews()
    }

    private fun initViews() = with(binding) {
        deleteButton.setOnClickListener {
            showDialog("delete")
        }
        editButton.setOnClickListener {
            val action =
                AloneDiaryDetailFragmentDirections.actionAloneDiaryDetailFragmentToWriteDiaryFragment()
            findNavController().navigate(action)
        }
    }

    private fun showDialog(deleteOrSave: String) {
        val dialogView = Dialog(requireContext())
        dialogView.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialogView.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialogView.setContentView(R.layout.dialog_mydiary_writediary)
        dialogView.setCancelable(false)

        dialogView.show()


        val submitButton = dialogView.findViewById<Button>(R.id.submitButton)
        val cancelButton = dialogView.findViewById<Button>(R.id.cancelButton)
        val noticeTextView = dialogView.findViewById<TextView>(R.id.noticeTextView)
        when (deleteOrSave) {
            "save" -> {
                noticeTextView.text = getString(R.string.diary_send_warning)
            }
            "delete" -> {
                noticeTextView.text = getString(R.string.diary_delete_warning)
            }
        }
        submitButton.setOnClickListener {
            lifecycleScope.launch {
                when (myDiaryViewModel.deleteDiary()) {
                    true -> {
                        findNavController().navigateUp()
                    }
                    else -> {
                        Timber.e("showDialog: 삭제되지 않음")
                    }
                }
                dialogView.dismiss()
            }
        }
        cancelButton.setOnClickListener {
            dialogView.dismiss()
        }
    }
}