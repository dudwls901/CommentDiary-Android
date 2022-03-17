package com.movingmaker.commentdiary.view.main.receiveddiary

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.activityViewModels
import com.movingmaker.commentdiary.base.BaseFragment
import com.movingmaker.commentdiary.databinding.FragmentReceiveddiaryBinding
import com.movingmaker.commentdiary.viewmodel.FragmentViewModel
import com.movingmaker.commentdiary.viewmodel.receiveddiary.ReceivedDiaryViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class ReceivedDiaryFragment: BaseFragment(), CoroutineScope {
    override val TAG: String = ReceivedDiaryFragment::class.java.simpleName

    private val job = Job()

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    companion object{
        fun newInstance() : ReceivedDiaryFragment {
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

    private fun observeDatas(){
        receivedDiaryViewModel.responseGetReceivedDiary.observe(viewLifecycleOwner){
            if(it.isSuccessful){
                Log.d(TAG, "observeDatas: ${it.body()!!.result}")
                it.body()?.let{ response ->
                    receivedDiaryViewModel.setReceivedDiary(response.result)
                    binding.commentLayout.isVisible = true
                    binding.diaryLayout.isVisible = true
                    binding.noReceivedDiaryYet.isVisible = false
                }
            }
            //전달된 일기가 없는경우 404
            else{
                binding.commentLayout.isVisible = false
                binding.diaryLayout.isVisible = false
                binding.noReceivedDiaryYet.isVisible = true
                Log.d(TAG, "observeDatas: receivecd diary 실패")
            }
        }

    }
    
    private fun initViews() = with(binding){

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
    }
}