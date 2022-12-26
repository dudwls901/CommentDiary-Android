package com.movingmaker.presentation.view.main.gatherdiary

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ConcatAdapter
import com.movingmaker.domain.model.request.ReportCommentModel
import com.movingmaker.presentation.R
import com.movingmaker.presentation.base.BaseFragment
import com.movingmaker.presentation.databinding.FragmentGatherdiaryCommentdiaryDetailBinding
import com.movingmaker.presentation.util.FRAGMENT_NAME
import com.movingmaker.presentation.util.getCodaToday
import com.movingmaker.presentation.util.ymdToDate
import com.movingmaker.presentation.viewmodel.FragmentViewModel
import com.movingmaker.presentation.viewmodel.gatherdiary.GatherDiaryViewModel
import com.movingmaker.presentation.viewmodel.mydiary.MyDiaryViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class CommentDiaryDetailFragment :
    BaseFragment<FragmentGatherdiaryCommentdiaryDetailBinding>(R.layout.fragment_gatherdiary_commentdiary_detail),
    OnCommentSelectListener {

    private val fragmentViewModel: FragmentViewModel by activityViewModels()
    private val myDiaryViewModel: MyDiaryViewModel by activityViewModels()
    private val gatherDiaryViewModel: GatherDiaryViewModel by activityViewModels()
    private val concatAdapter by lazy { ConcatAdapter() }
    private var reportedCommentId = -1L
    private var likedCommentId = -1L

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.vm = myDiaryViewModel
        fragmentViewModel.setCurrentFragment(FRAGMENT_NAME.COMMENT_DIARY_DETAIL)
        observeDatas()
        initToolBar()
        binding.recyclerView.adapter = concatAdapter
    }


    private fun observeDatas() {

        gatherDiaryViewModel.handleComment.observe(viewLifecycleOwner) {
            if (it.second == "report") {
                myDiaryViewModel.deleteLocalReportedComment(it.first)
            } else {
                myDiaryViewModel.likeLocalComment(it.first)
            }
        }

        /*
        * - 임시저장만 하고 전송을 하지 않은 경우 -> emptyComment(일기 전송 시간이 지났어요)
        * - 코멘트 도착한 경우
        *   - 내가 코멘트 작성한 경우 -> commentList
        *   - 내가 코멘트 작성하지 않은 경우 -> openYet
        *     - 작성 가능 날짜인 경우 -> 코멘트 작성하러 가기
        *     - 작성 가능 날짜 지난 경우 -> '온도' 사용하여 코멘트 조회하기
        * - 코멘트 없는 경우
        *   - 코멘트 기다리는 경우 (일기 작성 당일 ~ 다음 날) -> commentYet
        *   - 코멘트 못 받은 경우 (일기 작성 다다음날 ~) -> emptyComment
        * */
        myDiaryViewModel.selectedDiary.observe(viewLifecycleOwner) { diary ->
            Timber.d("observeDatas: --> $diary ")
            diary?.let {
                val list = listOf(diary)
                concatAdapter.addAdapter(
                    CommentDiaryContentAdapter().also { it.submitList(list) }
                )
                //코멘트 없는 경우
                if (diary.commentList.isEmpty()) {
                    ymdToDate(diary.date)?.let { diaryDate ->
                        //코멘트 못 받은 경우
                        if (diaryDate <= getCodaToday().minusDays(2)) {
                            concatAdapter.addAdapter(
                                CommentDiaryEmptyCommentAdapter().also { it.submitList(list) }
                            )
                        } else { // 코멘트 기다리는 경우
                            concatAdapter.addAdapter(
                                CommentDiaryCommentYetAdapter().also { it.submitList(list) }
                            )
                        }
                    }
                } else { //코멘트 있는 경우
                    //내가 코멘트 작성한 경우
                    if (myDiaryViewModel.haveDayMyComment.value == true) {
                        concatAdapter.addAdapter(
                            CommentListAdapter(this@CommentDiaryDetailFragment).also {
                                it.submitList(
                                    diary.commentList
                                )
                            }
                        )
                    } else { //내가 코멘트 작성하지 않은 경우
                        concatAdapter.addAdapter(
                            CommentDiaryOpenYetAdapter().also { it.submitList(list) }
                        )
                    }

                }
            }
        }

        myDiaryViewModel.haveDayMyComment.observe(viewLifecycleOwner) {
            val diary = myDiaryViewModel.selectedDiary.value!!
        }
    }

    private fun initToolBar() = with(binding) {

        backButton.setOnClickListener {
            findNavController().popBackStack()
        }
        deleteButton.setOnClickListener {
            viewLifecycleOwner.lifecycleScope.launch {
                if (myDiaryViewModel.deleteDiary()) {
                    findNavController().popBackStack()
                }
            }
        }
    }

    override fun onHeartClickListener(commentId: Long) {
        likedCommentId = commentId
        gatherDiaryViewModel.likeComment(commentId)
    }

    override fun onReportClickListener(commentId: Long) {
        showReportDialog(commentId)
    }

    override fun onBlockClickListener(commentId: Long) {
        showBlockDialog(commentId)
    }

    private fun showBlockDialog(commentId: Long) {
        val dialogView = Dialog(requireContext())
        dialogView.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialogView.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialogView.setContentView(R.layout.dialog_common_block)
        dialogView.setCancelable(false)
        dialogView.show()

        val submitButton =
            dialogView.findViewById<Button>(com.movingmaker.presentation.R.id.submitButton)
        val cancelButton =
            dialogView.findViewById<Button>(com.movingmaker.presentation.R.id.cancelButton)

        submitButton.setOnClickListener {
            //차단, 신고하기 api에 내용 널로 올리기
            reportedCommentId = commentId
            gatherDiaryViewModel.reportComment(
                ReportCommentModel(
                    id = commentId,
                    ""
                )
            )
            dialogView.dismiss()

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
                gatherDiaryViewModel.reportComment(
                    ReportCommentModel(
                        id = commentId,
                        content = reportContent
                    )
                )
                dialogView.dismiss()
            }
        }
        cancelButton.setOnClickListener {
            dialogView.dismiss()
        }
    }

}