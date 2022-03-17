package com.movingmaker.commentdiary.view.main

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.movingmaker.commentdiary.R
import com.movingmaker.commentdiary.base.BaseActivity
import com.movingmaker.commentdiary.databinding.ActivityMainBinding
import com.movingmaker.commentdiary.view.main.gatherdiary.DiaryListFragment
import com.movingmaker.commentdiary.view.main.gatherdiary.CommentDiaryDetailFragment
import com.movingmaker.commentdiary.view.main.mydiary.CalendarWithDiaryFragment
import com.movingmaker.commentdiary.view.main.mydiary.WriteDiaryFragment
import com.movingmaker.commentdiary.view.main.mypage.TempMyPageFragment
import com.movingmaker.commentdiary.view.main.receiveddiary.ReceivedDiaryFragment
import com.movingmaker.commentdiary.viewmodel.FragmentViewModel
import com.movingmaker.commentdiary.viewmodel.gatherdiary.GatherDiaryViewModel
import com.movingmaker.commentdiary.viewmodel.mydiary.MyDiaryViewModel
import com.movingmaker.commentdiary.viewmodel.mypage.MyPageViewModel
import com.movingmaker.commentdiary.viewmodel.receiveddiary.ReceivedDiaryViewModel
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class MainActivity : BaseActivity<ActivityMainBinding>(), CoroutineScope {
    override val TAG: String = MainActivity::class.java.simpleName

    private var backButtonTime = 0L
    //test
    override val layoutRes: Int = R.layout.activity_main

    private val job = Job()

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    private val myPageViewModel: MyPageViewModel by viewModels()
    private val myDiaryViewModel: MyDiaryViewModel by viewModels()
    private val fragmentViewModel: FragmentViewModel by viewModels()
    private val gatherDiaryViewModel: GatherDiaryViewModel by viewModels()
    private val receivedDiaryViewModel: ReceivedDiaryViewModel by viewModels()

    private val fragmentMap = HashMap<String,Fragment>()

    companion object{
        val fragmentState = HashMap<String,Fragment>()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.lifecycleOwner = this
        binding.fragmentviewModel = fragmentViewModel
        Log.d(TAG, "onCreate:  ${fragmentViewModel.hasBottomNavi}")

        setFragments()
        initViews()
        observerFragments()
    }

    private fun setFragments(){
//        calendarWithDiaryFragment = CalendarWithDiaryFragment.newInstance()
//        fragment2 = Fragment2.newInstance()
//        diaryListFragment = DiaryListFragment.newInstance()
//        tempMyPageFragment = TempMyPageFragment.newInstance()
//        writeDiaryFragment = WriteDiaryFragment.newInstance()
//        commentDiaryDetailFragment = CommentDiaryDetailFragment.newInstance()

        fragmentMap["myDiary"] = CalendarWithDiaryFragment.newInstance()
        fragmentMap["receivedDiary"] = ReceivedDiaryFragment.newInstance()
        fragmentMap["gatherDiary"] = DiaryListFragment.newInstance()
        fragmentMap["myPage"] = TempMyPageFragment.newInstance()
        fragmentMap["writeDiary"] = WriteDiaryFragment.newInstance()
        fragmentMap["commentDiaryDetail"] = CommentDiaryDetailFragment.newInstance()

    }


    private fun observerFragments(){
        fragmentViewModel.fragmentState.observe(this){fragment->
            Log.d(TAG, "observerFragments: $fragment")
            if(fragment!=null)
            replaceFragment(fragment)
        }
    }

    private fun initViews(){

        initBottomNavigationView()
    }

    private fun initBottomNavigationView() = with(binding){
        bottomNavigationView.itemIconTintList=null
        bottomNavigationView.itemTextColor =null
        //클릭시 퍼지는 색상 변경
//        bottomNavigationView.itemRippleColor = null
        Log.d(TAG, "initBottomNavigationView:")

        bottomNavigationView.setOnItemSelectedListener{menu->
            when(menu.itemId){
                R.id.myDiary-> fragmentViewModel.setFragmentState("myDiary")
                R.id.receivedDiary-> fragmentViewModel.setFragmentState("receivedDiary")
                R.id.collection-> fragmentViewModel.setFragmentState("gatherDiary")
                R.id.myPage-> fragmentViewModel.setFragmentState("myPage")
            }
            true
        }

    }

    private fun replaceFragment(showFragment: String) {
        //액티비티에는 서포트프래그먼트매니저가 있다. 각각 액티비티에 attach되어있는 프래그먼트를 관리
        //트랜잭션 : 이 작업이 시작함을 알리고 커밋까지는 요 작업만 하게끔
        supportFragmentManager.beginTransaction().apply {
            //R.id.fragmentContainer
            Log.d(TAG, "replaceFragment: $showFragment")
            //처음 들어간 프래그먼트 추가
            if(fragmentState[showFragment]==null){
                fragmentState[showFragment] = fragmentMap[showFragment]!!
                //writeDiary는 태그 넣기
//                if(showFragment=="writeDiary"){
//                    add(binding.fragmentContainer.id,fragmentMap[showFragment]!!, showFragment)
//                }
//                else{
                    add(binding.fragmentContainer.id,fragmentMap[showFragment]!!)
//                }
            }
            //현재 프래그먼트만 보여주고 나머진 다 hide
            for(fragment in fragmentState) {
                if(fragmentMap[showFragment] == fragment.value) {
                    show(fragment.value)
                    Log.d(TAG, "replaceFragment: show ${showFragment} ${fragment} ")
                }
                else{
                    hide(fragment.value)
                }
            }
            when (showFragment) {
                "myDiary" -> {
                    fragmentViewModel.setHasBottomNavi(true)
                }
                "receivedDiary" -> {
                    fragmentViewModel.setHasBottomNavi(true)
                }
                "gatherDiary" -> {
                    fragmentViewModel.setHasBottomNavi(true)
                }
                "myPage" -> {
                    fragmentViewModel.setHasBottomNavi(true)
                }
                "writeDiary" -> {
                    fragmentViewModel.setHasBottomNavi(false)
                }
                "commentDiaryDetail" -> {
                    fragmentViewModel.setHasBottomNavi(false)
                }
            }
            //메인으로갈 때마다 초기화
//            supportFragmentManager.clearBackStack()
            Log.d(TAG, "replaceFragment: $showFragment")
            Log.d(TAG, "replaceFragment: ${supportFragmentManager.findFragmentById(R.id.fragmentContainer)}")
            Log.d(TAG, "replaceFragment: ${supportFragmentManager.backStackEntryCount}")
            commit()
        }
    }

//    private fun clearBackStack() {
//        val fragmentManager: FragmentManager = supportFragmentManager
//        fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
//    }

    override fun onBackPressed() {
        val currentTime = System.currentTimeMillis()
        val gapTime = currentTime - backButtonTime

        if (gapTime in 0..2000) {
            // 2초 안에 두번 뒤로가기 누를 시 앱 종료
            if (supportFragmentManager.backStackEntryCount == 0) finish()
        }
        else {
            backButtonTime = currentTime
            if (supportFragmentManager.backStackEntryCount == 0) Toast.makeText(this, "뒤로가기 버튼을 한번 더 누르면 종료됩니다.", Toast.LENGTH_SHORT).show()
            else{
                supportFragmentManager.popBackStack()
            }
        }
    }

}