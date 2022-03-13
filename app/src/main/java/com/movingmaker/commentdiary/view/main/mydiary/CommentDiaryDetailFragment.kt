package com.movingmaker.commentdiary.view.main.mydiary

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.core.view.marginTop
import androidx.fragment.app.activityViewModels
import com.movingmaker.commentdiary.R
import com.movingmaker.commentdiary.base.BaseFragment
import com.movingmaker.commentdiary.databinding.FragmentMydiaryCommentdiaryDetailBinding
import com.movingmaker.commentdiary.util.DateConverter
import com.movingmaker.commentdiary.viewmodel.FragmentViewModel
import com.movingmaker.commentdiary.viewmodel.mydiary.MyDiaryViewModel
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class CommentDiaryDetailFragment : BaseFragment(), CoroutineScope {

    override val TAG: String = CommentDiaryDetailFragment::class.java.simpleName

    private lateinit var binding: FragmentMydiaryCommentdiaryDetailBinding
    private val myDiaryViewModel: MyDiaryViewModel by activityViewModels()
    private val fragmentViewModel: FragmentViewModel by activityViewModels()
    private val job = Job()

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    companion object{
        fun newInstance() : CommentDiaryDetailFragment {
            return CommentDiaryDetailFragment()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentMydiaryCommentdiaryDetailBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding.myDiaryviewModel = myDiaryViewModel
        fragmentViewModel.setHasBottomNavi(false)
        Log.d(TAG, "onCreateView saveOrEdit: ${myDiaryViewModel.saveOrEdit.value}")
        initViews()
        initToolBar()
        return binding.root
    }

    private fun initViews() = with(binding){
        Log.d(TAG, "initViews: detail ${myDiaryViewModel.selectedDiary.value}")
        val codaToday = DateConverter.getCodaToday()
        val selectedDate = DateConverter.ymdToDate(myDiaryViewModel.selectedDiary.value!!.date)
//        val minusTwoDay = codaToday.minusDays(2)
//        Log.d(TAG, "initViews: ${selectedDate} ${myDiaryViewModel.selectedDiary.value!!.commentList} ${myDiaryViewModel.selectedDiary.value!!.commentList!!.size}")
        Log.d(TAG, "initViews: detail ${myDiaryViewModel.selectedDiary.value!!.commentList?.isEmpty()}")
        //코멘트 없는 경우
        if(myDiaryViewModel.selectedDiary.value!!.commentList?.isEmpty()==true || myDiaryViewModel.selectedDiary.value!!.commentList==null){

            if(selectedDate <= codaToday.minusDays(2)){
                //이틀이 지나 영영 코멘트를 받을 수 없음
                emptyCommentTextView.isVisible = true
                diaryUploadServerYetTextView.isVisible = false
                sendCompleteTextView.isVisible = false
            }
            else{
                //아직 코멘트를 받지 못한 경우
                emptyCommentTextView.isVisible = false
                diaryUploadServerYetTextView.isVisible= true
                sendCompleteTextView.isVisible = true
            }
        }
        //코멘트 있는 경우 리사이클러뷰 띄우기
        else{
            emptyCommentTextView.isVisible = false
            diaryUploadServerYetTextView.isVisible = false
            sendCompleteTextView.isVisible = false
        }
    }

    private fun initToolBar() = with(binding){
        //툴바
        backButton.setOnClickListener {
//            parentFragmentManager.popBackStack()
            fragmentViewModel.setFragmentState("myDiary")
        }
    }
}