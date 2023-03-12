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
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ConcatAdapter
import com.movingmaker.domain.model.request.ReportCommentModel
import com.movingmaker.domain.model.response.Diary
import com.movingmaker.presentation.R
import com.movingmaker.presentation.base.BaseFragment
import com.movingmaker.presentation.databinding.FragmentGatherdiaryCommentdiaryDetailBinding
import com.movingmaker.presentation.util.FRAGMENT_NAME
import com.movingmaker.presentation.view.main.gatherdiary.adapter.CommentDiaryCommentYetAdapter
import com.movingmaker.presentation.view.main.gatherdiary.adapter.CommentDiaryContentAdapter
import com.movingmaker.presentation.view.main.gatherdiary.adapter.CommentDiaryEmptyCommentAdapter
import com.movingmaker.presentation.view.main.gatherdiary.adapter.CommentDiaryOpenYetAdapter
import com.movingmaker.presentation.view.main.gatherdiary.adapter.CommentListAdapter
import com.movingmaker.presentation.view.main.mydiary.DiaryState
import com.movingmaker.presentation.viewmodel.FragmentViewModel
import com.movingmaker.presentation.viewmodel.gatherdiary.GatherDiaryViewModel
import com.movingmaker.presentation.viewmodel.mydiary.MyDiaryViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
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
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    myDiaryViewModel.diaryState.collectLatest { diaryState ->
                        Timber.e("diaryState DetailFragment ${diaryState.javaClass.simpleName}")
                        if (diaryState !is DiaryState.CommentDiary || diaryState is DiaryState.CommentDiary.TempDiaryInTime) return@collectLatest
                        setDefaultUi(diaryState.diary)
                        when (diaryState) {
                            //일기 전송된 상태
                            is DiaryState.CommentDiary.HaveNotCommentInTime -> {
                                //전송완료
                                //누군가의 코멘트 곧 도착 //empty
                                setHaveNotCommentInTimeUi(diaryState.diary)
                            }
                            is DiaryState.CommentDiary.HaveNotCommentOutTime -> {
                                //삭제 버튼
                                //아쉽게도 누군가로부터 코멘트 없어 //empty
                                setHaveNotCommentOutTimeUi()
                            }
                            is DiaryState.CommentDiary.HaveCommentInTimeCanOpen,
                            is DiaryState.CommentDiary.HaveCommentOutTimeCanOpen -> {
                                //삭제 버튼
                                //코멘트 리스트 뷰
                                setHaveCommentCanOpenUi(diaryState.diary)
                            }
                            is DiaryState.CommentDiary.HaveCommentInTimeCannotOpen,
                            is DiaryState.CommentDiary.HaveCommentOutTimeCannotOpen -> {
                                //삭제 버튼
                                //누군가의 코멘트
                                //코멘트 작성하기
                                //도착한일기의 코멘트를 작성해야 내게 온 코멘트 확인할 수 있어요.
                                setHaveCommentCannotOpenUi(diaryState.diary)
                            }
                            is DiaryState.CommentDiary.TempDiaryInTime -> {
                                //inTime은 이 프래그먼트 오지 않음, 위에서 return하여 unreachable하지만 지우면 when 컴파일 에러
                                /*no-op*/
                            }
                            is DiaryState.CommentDiary.TempDiaryOutTime -> {
                                setTempDiaryOutTimeUi()
                                //삭제 버튼
                                //일기 전송 시간이 지났어요. //empty
                            }
                        }
                    }
                }
            }
        }

//        viewLifecycleOwner.lifecycleScope.launch {
//            repeatOnLifecycle(Lifecycle.State.STARTED) {
//                myDiaryViewModel.selectedDiary.collectLatest {
//
//                }
//            }
//        }
    }

    private fun setDefaultUi(diary: Diary) {
        Timber.e("detail setDefaultUI ${diary}")
        concatAdapter.addAdapter(
            CommentDiaryContentAdapter().also { it.submitList(listOf(diary)) }
        )
    }

    private fun setTempDiaryOutTimeUi() {
        binding.sendedTextView.isVisible = false
        concatAdapter.addAdapter(
            CommentDiaryEmptyCommentAdapter().also {
                it.submitList(
                    listOf(getString(R.string.temp_diary_out_time_notice))
                )
            }
        )
    }

    private fun setHaveCommentCannotOpenUi(diary: Diary) {
        //삭제 버튼
        //누군가의 코멘트
        //코멘트 작성하기
        //도착한일기의 코멘트를 작성해야 내게 온 코멘트 확인할 수 있어요.

        binding.deleteButton.isVisible = true
        binding.sendedTextView.isVisible = false
        concatAdapter.addAdapter(
            CommentDiaryOpenYetAdapter().also { it.submitList(listOf(diary)) }
        )
    }

    private fun setHaveCommentCanOpenUi(diary: Diary) {
        binding.deleteButton.isVisible = true
        binding.sendedTextView.isVisible = false
        concatAdapter.addAdapter(
            CommentListAdapter(this@CommentDiaryDetailFragment).also {
                it.submitList(
                    diary.commentList
                )
            }
        )
    }

    private fun setHaveNotCommentOutTimeUi() {
        binding.deleteButton.isVisible = true
        binding.sendedTextView.isVisible = false
        concatAdapter.addAdapter(
            CommentDiaryEmptyCommentAdapter().also {
                it.submitList(
                    listOf(
                        getString(R.string.empty_comment)
                    )
                )
            }
        )
    }

    private fun setHaveNotCommentInTimeUi(diary: Diary) {
        binding.deleteButton.isVisible = false
        binding.sendedTextView.isVisible = true
        concatAdapter.addAdapter(
            CommentDiaryCommentYetAdapter().also {
                it.submitList(
                    listOf(diary)
                )
            }
        )
    }

    private fun initToolBar() = with(binding) {

        backButton.setOnClickListener {
            findNavController().popBackStack()
        }
        deleteButton.setOnClickListener {
            viewLifecycleOwner.lifecycleScope.launch {
                when (myDiaryViewModel.diaryState.value) {
                    is DiaryState.CommentDiary.TempDiaryOutTime -> {
                        myDiaryViewModel.deleteTempCommentDiary()
                        findNavController().popBackStack()
                    }
                    else -> {
                        if (myDiaryViewModel.deleteDiary()) {
                            findNavController().popBackStack()
                        }
                    }
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