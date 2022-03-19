package com.movingmaker.commentdiary.view.main.receiveddiary

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.activityViewModels
import com.movingmaker.commentdiary.R
import com.movingmaker.commentdiary.base.BaseFragment
import com.movingmaker.commentdiary.databinding.FragmentReceiveddiaryBinding
import com.movingmaker.commentdiary.model.remote.request.EditDiaryRequest
import com.movingmaker.commentdiary.model.remote.request.SaveDiaryRequest
import com.movingmaker.commentdiary.viewmodel.FragmentViewModel
import com.movingmaker.commentdiary.viewmodel.receiveddiary.ReceivedDiaryViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class ReceivedDiaryFragment : BaseFragment(), CoroutineScope {
    override val TAG: String = ReceivedDiaryFragment::class.java.simpleName

    private val job = Job()

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    companion object {
        fun newInstance(): ReceivedDiaryFragment {
            return ReceivedDiaryFragment()
        }
    }

    private lateinit var binding: FragmentReceiveddiaryBinding
    private val fragmentViewModel: FragmentViewModel by activityViewModels()
    private val receivedDiaryViewModel: ReceivedDiaryViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentReceiveddiaryBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding.lifecycleOwner = viewLifecycleOwner
        binding.receivedDiaryviewModel = receivedDiaryViewModel
        initViews()
        observeDatas()
        return binding.root

    }

    @SuppressLint("ResourceAsColor")
    private fun observeDatas() {
        receivedDiaryViewModel.responseGetReceivedDiary.observe(viewLifecycleOwner) {
            if (it.isSuccessful) {
                Log.d(TAG, "observeDatas: ${it.body()!!.result}")
                it.body()?.let { response ->
                    receivedDiaryViewModel.setReceivedDiary(response.result)
                    binding.commentLayout.isVisible = true
                    binding.diaryLayout.isVisible = true
                    binding.noReceivedDiaryYet.isVisible = false
                    //내가 쓴 코멘트가 있는 경우
                    if (response.result.commentList != null) {
                        binding.sendCommentButton.background = ContextCompat.getDrawable(
                            requireContext(),
                            R.drawable.background_ivory_radius_15
                        )
                        binding.sendCommentButton.text = getString(R.string.diary_send_complete)
                        binding.sendCommentButton.setTextColor(R.color.text_brown)
                        binding.sendCommentButton.isEnabled = false
                        binding.commentEditTextView.setText(response.result.commentList[0].content)
                        binding.commentEditTextView.isEnabled = false
                    } else {
                        binding.sendCommentButton.background = ContextCompat.getDrawable(
                            requireContext(),
                            R.drawable.background_pure_green_radius_15
                        )
                        binding.sendCommentButton.text = getString(R.string.diary_send_complete)
                        binding.sendCommentButton.setTextColor(R.color.background_ivory)
                        binding.sendCommentButton.isEnabled = true
                        binding.commentEditTextView.text = null
                        binding.commentEditTextView.isEnabled = true

                    }
                }
            }
            //전달된 일기가 없는경우 404
            else {
                binding.commentLayout.isVisible = false
                binding.diaryLayout.isVisible = false
                binding.noReceivedDiaryYet.isVisible = true
                Log.d(TAG, "observeDatas: receivecd diary 실패")
            }
        }

        receivedDiaryViewModel.responseSaveComment.observe(viewLifecycleOwner) { response ->

            if (response.isSuccessful) {
                Toast.makeText(requireContext(), "코멘트가 저장되었습니다.", Toast.LENGTH_SHORT).show()
                initViews()
            }
            //todo 일기 전송 실패 처리
            else {
                Toast.makeText(requireContext(), "코멘트가 저장되지 않았습니다.", Toast.LENGTH_SHORT).show()
            }
        }

        receivedDiaryViewModel.responseReportDiary.observe(viewLifecycleOwner){ response ->
            if(response.isSuccessful){
                Toast.makeText(requireContext(), "신고가 접수되었습니다.", Toast.LENGTH_SHORT).show()
            }
            //todo 신고 실패 처리
            else{
                Toast.makeText(requireContext(), "신고가 접수되지 않았습니다.", Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun initViews() = with(binding) {
        //받은 일기 조회
        launch(coroutineContext) {
            launch(Dispatchers.IO) {
                receivedDiaryViewModel.setResponseGetReceivedDiary()
            }
        }

        commentEditTextView.addTextChangedListener {
            receivedDiaryViewModel.setCommentTextCount(
                commentEditTextView.text.length
            )
        }

        sendCommentButton.setOnClickListener {
            showSendDialog()
        }

        reportButton.setOnClickListener {
            showReportDialog()
        }
    }

    private fun showSendDialog() {
        val dialogView = Dialog(requireContext())
        dialogView.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialogView.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialogView.setContentView(R.layout.dialog_receiveddiary_send_comment)
        dialogView.setCancelable(false)
        dialogView.show()


        val submitButton = dialogView.findViewById<Button>(R.id.submitButton)
        val cancelButton = dialogView.findViewById<Button>(R.id.cancelButton)

        submitButton.setOnClickListener {
            //코멘트 전송
            launch(coroutineContext) {
                launch(Dispatchers.IO) {
                    receivedDiaryViewModel.setResponseSaveComment(
                        binding.commentEditTextView.text.toString()
                    )
                }
                dialogView.dismiss()
            }
        }
        cancelButton.setOnClickListener {
            dialogView.dismiss()
        }

    }

    private fun showReportDialog() {
        val dialogView = Dialog(requireContext())
        dialogView.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialogView.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialogView.setContentView(R.layout.dialog_common_report)
        dialogView.setCancelable(false)
        dialogView.show()


        val submitButton = dialogView.findViewById<Button>(R.id.submitButton)
        val cancelButton = dialogView.findViewById<Button>(R.id.cancelButton)
        val reportContentEditText = dialogView.findViewById<EditText>(R.id.reportContentEditText)

        submitButton.setOnClickListener {
            val reportContent = reportContentEditText.text.toString()
            if(reportContent.isEmpty()){
                Toast.makeText(requireContext(), "내용 입력해라잉", Toast.LENGTH_SHORT).show()
            }
            //한 글자 이상 입력했으면
            else{
                //신고
                launch(coroutineContext) {
                    launch(Dispatchers.IO) {
                        receivedDiaryViewModel.setResponseReportDiary(reportContent)
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