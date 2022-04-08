package com.movingmaker.commentdiary.view.main.gatherdiary

import android.annotation.SuppressLint
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import com.movingmaker.commentdiary.CodaApplication
import com.movingmaker.commentdiary.R
import com.movingmaker.commentdiary.base.BaseFragment
import com.movingmaker.commentdiary.databinding.FragmentGatherdiaryCommentdiaryDetailBinding
import com.movingmaker.commentdiary.global.CodaSnackBar
import com.movingmaker.commentdiary.model.remote.RetrofitClient
import com.movingmaker.commentdiary.model.remote.request.ReportCommentRequest
import com.movingmaker.commentdiary.util.DateConverter
import com.movingmaker.commentdiary.viewmodel.FragmentViewModel
import com.movingmaker.commentdiary.viewmodel.gatherdiary.GatherDiaryViewModel
import com.movingmaker.commentdiary.viewmodel.mydiary.MyDiaryViewModel
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class CommentDiaryDetailFragment : BaseFragment(), CoroutineScope, OnCommentSelectListener {

    override val TAG: String = CommentDiaryDetailFragment::class.java.simpleName

    private lateinit var binding: FragmentGatherdiaryCommentdiaryDetailBinding

    private val fragmentViewModel: FragmentViewModel by activityViewModels()
    private val myDiaryViewModel: MyDiaryViewModel by activityViewModels()
    private val gatherDiaryViewModel: GatherDiaryViewModel by activityViewModels()
    private lateinit var commentListAdapter: CommentListAdapter
    private val job = Job()
    private var reportedCommentId = -1L
    private var likedCommentId = -1L
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    companion object {
        fun newInstance(): CommentDiaryDetailFragment {
            return CommentDiaryDetailFragment()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentGatherdiaryCommentdiaryDetailBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding.myDiaryviewModel = myDiaryViewModel
        binding.lifecycleOwner = viewLifecycleOwner
        observeDatas()
        initViews()
        initToolBar()
        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun observeDatas() {

        myDiaryViewModel.responseGetDayComment.observe(viewLifecycleOwner) { response ->
            //하루 코멘트 가져오기
            if (response.isSuccessful) {
                myDiaryViewModel.setHaveDayMyComment(response.body()!!.result.isNotEmpty())
            } else {
                response.errorBody()?.let{ errorBody->
                    RetrofitClient.getErrorResponse(errorBody)?.let {
                        if (it.status == 401) {
                            Toast.makeText(requireContext(), "다시 로그인해 주세요.", Toast.LENGTH_SHORT)
                                .show()
                            CodaApplication.getInstance().logOut()
                        } else {
                            CodaSnackBar.make(binding.root, "코멘트를 읽는 데 실패하였습니다.").show()
                        }
                    }
                }
            }
        }

        gatherDiaryViewModel.responseLikeComment.observe(viewLifecycleOwner) { response ->
            binding.loadingBar.isVisible = false
            if (response.isSuccessful) {
                if (likedCommentId != -1L) {
                    myDiaryViewModel.likeLocalComment(likedCommentId)
                    commentListAdapter.notifyDataSetChanged()
                }
            }
            else{
                response.errorBody()?.let{ errorBody->
                    RetrofitClient.getErrorResponse(errorBody)?.let {
                        if (it.status == 401) {
                            Toast.makeText(requireContext(), "다시 로그인해 주세요.", Toast.LENGTH_SHORT)
                                .show()
                            CodaApplication.getInstance().logOut()
                        }
                    }
                }
            }
        }

        gatherDiaryViewModel.responseReportComment.observe(viewLifecycleOwner) { response ->
            binding.loadingBar.isVisible = false
            //신고 성공한 경우
            if (response.isSuccessful) {
//                CodaSnackBar.make(binding.root, "신고가 접수되었습니다.").show()
                //신고한 코멘트 삭제해서 갱신
                //어댑터 갱신
                if (reportedCommentId != -1L) {
                    myDiaryViewModel.deleteLocalReportedComment(reportedCommentId)
                    commentListAdapter.notifyDataSetChanged()
                }
            } else {
                response.errorBody()?.let{ errorBody->
                    RetrofitClient.getErrorResponse(errorBody)?.let {
                        if (it.status == 401) {
                            Toast.makeText(requireContext(), "다시 로그인해 주세요.", Toast.LENGTH_SHORT)
                                .show()
                            CodaApplication.getInstance().logOut()
                        } else {
                            CodaSnackBar.make(binding.root, "차단/신고가 접수되지 않았습니다.").show()                        }
                    }
                }
            }
        }

        myDiaryViewModel.selectedDiary.observe(viewLifecycleOwner) { diary ->
            commentListAdapter.submitList(diary.commentList)
        }

        myDiaryViewModel.haveDayMyComment.observe(viewLifecycleOwner) {
            val diary = myDiaryViewModel.selectedDiary.value!!
            val codaToday = DateConverter.getCodaToday()
            val selectedDate = DateConverter.ymdToDate(diary.date)

            //코멘트 없는 경우
            if (diary.commentList?.isEmpty() == true || diary.commentList == null) {
                binding.goToWriteCommentButton.isVisible = false
                binding.goToWriteCommentTextView.isVisible = false
                binding.noWriteCommentTextView.isVisible = false
                binding.recyclerView.isVisible = false
                if (selectedDate <= codaToday.minusDays(2)) {
                    //이틀이 지나 영영 코멘트를 받을 수 없음
                    binding.emptyCommentTextView.isVisible = true
                    binding.diaryUploadServerYetTextView.isVisible = false
                    binding.sendCompleteTextView.isVisible = false
                } else {
                    //아직 코멘트를 받지 못한 경우
                    binding.emptyCommentTextView.isVisible = false
                    binding.diaryUploadServerYetTextView.isVisible = true
                    binding.sendCompleteTextView.isVisible = true
                }
            }
            //코멘트 있는 경우 리사이클러뷰 띄우기
            else {
                binding.emptyCommentTextView.isVisible = false
                binding.diaryUploadServerYetTextView.isVisible = false
                binding.sendCompleteTextView.isVisible = false
                //내가 코멘트를 작성 한 경우
                if (myDiaryViewModel.haveDayMyComment.value == true) {
                    binding.recyclerView.isVisible = true
                    binding.goToWriteCommentButton.isVisible = false
                    binding.goToWriteCommentTextView.isVisible = false
                    binding.noWriteCommentTextView.isVisible = false
                } else {
                    //내가 코멘트를 작성 안 한 경우
                    //                    Log.d(TAG, "observeDatas: detail 6 ${selectedDate} ${codaToday.minusDays(2)}")
                    if (selectedDate <= codaToday.minusDays(2)) {
                        binding.goToWriteCommentButton.isVisible = false
                        binding.goToWriteCommentTextView.isVisible = false
                        binding.noWriteCommentTextView.isVisible = true
                    } else {
                        binding.goToWriteCommentButton.isVisible = true
                        binding.goToWriteCommentTextView.isVisible = true
                        binding.noWriteCommentTextView.isVisible = false
                    }
                    binding.recyclerView.isVisible = false
                }
            }
        }
    }

    private fun initViews() = with(binding) {

        binding.goToWriteCommentButton.setOnClickListener {
            fragmentViewModel.setBeforeFragment("commentDiaryDetail")
            fragmentViewModel.setFragmentState("receivedDiary")
        }
        commentListAdapter = CommentListAdapter(this@CommentDiaryDetailFragment)
        commentListAdapter.setHasStableIds(true)
        binding.recyclerView.adapter = commentListAdapter
    }

    private fun initToolBar() = with(binding) {

        backButton.setOnClickListener {
            if (fragmentViewModel.beforeFragment.value == "writeDiary") {
                fragmentViewModel.setFragmentState("myDiary")
            } else if (fragmentViewModel.beforeFragment.value == "gatherDiary") {
                fragmentViewModel.setFragmentState("gatherDiary")
            } else {
                fragmentViewModel.setFragmentState("myDiary")
            }
        }
    }

    override fun onHeartClickListener(commentId: Long) {
        likedCommentId = commentId
        launch(coroutineContext) {
            binding.loadingBar.isVisible = true
            withContext(Dispatchers.IO) {
                gatherDiaryViewModel.setResponseLikeComment(commentId)
            }
        }
    }

    override fun onReportClickListener(commentId: Long) {
        showReportDialog(commentId)
    }

    override fun onBlockClickLinstener(commentId: Long) {
        showBlockDialog(commentId)
    }

    private fun showBlockDialog(commentId: Long) {
        val dialogView = Dialog(requireContext())
        dialogView.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialogView.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialogView.setContentView(R.layout.dialog_common_block)
        dialogView.setCancelable(false)
        dialogView.show()

        val submitButton = dialogView.findViewById<Button>(R.id.submitButton)
        val cancelButton = dialogView.findViewById<Button>(R.id.cancelButton)

        submitButton.setOnClickListener {
            //차단, 신고하기 api에 내용 널로 올리기
            reportedCommentId = commentId
            launch(coroutineContext) {
                binding.loadingBar.isVisible = true
                withContext(Dispatchers.IO) {
                    gatherDiaryViewModel.setResponseReportComment(
                        ReportCommentRequest(
                            id = commentId,
                            ""
                        )
                    )
                }
                dialogView.dismiss()
            }
        }
        cancelButton.setOnClickListener {
            dialogView.dismiss()
        }
    }

    private fun showReportDialog(commentId: Long) {
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
            if (reportContent.isEmpty()) {
                val shake: Animation = AnimationUtils.loadAnimation(requireContext(), R.anim.shake)
                reportContentEditText.startAnimation(shake)
            }
            //한 글자 이상 입력했으면
            else {
                //신고
                //로컬에서 코멘트 삭제
                reportedCommentId = commentId
                launch(coroutineContext) {
                    binding.loadingBar.isVisible = true
                    withContext(Dispatchers.IO) {
                        gatherDiaryViewModel.setResponseReportComment(
                            ReportCommentRequest(
                                id = commentId,
                                content = reportContent
                            )
                        )
                    }
                    dialogView.dismiss()
                }
            }
        }
        cancelButton.setOnClickListener {
            dialogView.dismiss()
        }
    }

}