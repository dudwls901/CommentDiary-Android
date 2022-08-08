package com.movingmaker.commentdiary.view.main

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.core.view.get
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.movingmaker.commentdiary.R
import com.movingmaker.commentdiary.global.base.BaseActivity
import com.movingmaker.commentdiary.databinding.ActivityMainBinding
import com.movingmaker.commentdiary.global.CodaSnackBar
import com.movingmaker.commentdiary.util.DateConverter
import com.movingmaker.commentdiary.util.FRAGMENT_NAME
import com.movingmaker.commentdiary.viewmodel.FragmentViewModel
import com.movingmaker.commentdiary.viewmodel.gatherdiary.GatherDiaryViewModel
import com.movingmaker.commentdiary.viewmodel.mydiary.MyDiaryViewModel
import com.movingmaker.commentdiary.viewmodel.mypage.MyPageViewModel
import com.movingmaker.commentdiary.viewmodel.receiveddiary.ReceivedDiaryViewModel
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class MainActivity : BaseActivity<ActivityMainBinding>(R.layout.activity_main), CoroutineScope {
    override val TAG: String = MainActivity::class.java.simpleName
    private val job = Job()
    private var deferredJob: Deferred<Job>? = null
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    private val myPageViewModel: MyPageViewModel by viewModels()
    private val myDiaryViewModel: MyDiaryViewModel by viewModels()
    private val fragmentViewModel: FragmentViewModel by viewModels()
    private val gatherDiaryViewModel: GatherDiaryViewModel by viewModels()
    private val receivedDiaryViewModel: ReceivedDiaryViewModel by viewModels()

    private var pushDate: String? = null
    private lateinit var navController: NavController
    private var backButtonTime = 0L

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        pushDate = intent?.getStringExtra("pushDate")
        Log.d("MainActivity", "onNewIntent: push $pushDate")
        if (pushDate != null) {
            myDiaryViewModel.setPushDate(pushDate!!)

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.lifecycleOwner = this
        binding.fragmentviewModel = fragmentViewModel
        Log.d(TAG, "onCreate: -->")
        initViews()
//            replaceFragment("myDiary")
        observerFragments()
        observerDatas()
        deferredJob = async(start = CoroutineStart.LAZY) {
            myDiaryViewModel.setResponseGetMonthDiary(
                DateConverter.ymFormat(DateConverter.getCodaToday()),
                "onCreate"
            )
        }
        receivedDiaryViewModel.setResponseGetReceivedDiary()

        pushDate = intent.getStringExtra("pushDate")
        //푸시로 들어온 경우 바로 코멘트 화면으로
        Log.d("MainActivity", "oncreate: push $pushDate")
        if (pushDate != null) {
            myDiaryViewModel.setPushDate(pushDate!!)
        }
    }

    private fun observerDatas() {

        receivedDiaryViewModel.initReceivedDiary.observe(this) {
            launch {
                Log.d(TAG, "observerDatas: $deferredJob --> 실행")
                deferredJob?.await()
            }
            Log.d(TAG, "observerDatas: --> receivedDiary: ${it}")
            if (it != null) {
                if (it.myComment?.isNotEmpty() == true) {
                    binding.bottomNavigationView.menu[1].icon =
//                    ContextCompat.getDrawable(this, R.drawable.bottom_ic_received_notice)
                        ContextCompat.getDrawable(this, R.drawable.bottom_ic_received)
                } else {
                    //코멘트가 없다면
                    binding.bottomNavigationView.menu[1].icon =
//                    ContextCompat.getDrawable(this, R.drawable.bottom_ic_received)
                        ContextCompat.getDrawable(this, R.drawable.bottom_ic_received_notice)
                }
            } else {
                //도착한 일기가 없다면
                ContextCompat.getDrawable(this, R.drawable.bottom_ic_received)
            }

        }
    }

    private fun observerFragments() {
        fragmentViewModel.curFragment.observe(this) { fragment ->
            Log.d(TAG, "observerFragments: nav ${navController.currentDestination?.label}")
            Log.d(TAG, "observerFragments: frag $fragment")
            //마이페이지만 스테이터스바 흰색
            if (fragment == FRAGMENT_NAME.MY_PAGE ||
                fragment == FRAGMENT_NAME.MY_ACCOUNT ||
                fragment == FRAGMENT_NAME.SIGN_OUT ||
                fragment == FRAGMENT_NAME.TERMS ||
                fragment == FRAGMENT_NAME.SENDED_COMMENT_LIST ||
                fragment == FRAGMENT_NAME.CHANGE_PASSWORD ||
                fragment == FRAGMENT_NAME.PUSHALARM_ONOFF
            ) {
                window.statusBarColor = getColor(R.color.background_ivory)
            } else {
                window.statusBarColor = getColor(R.color.core_beige)
            }
        }
    }

    private fun initViews() {

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.mainNavHostFragment) as NavHostFragment
        navController = navHostFragment.navController
        NavigationUI.setupWithNavController(binding.bottomNavigationView, navController)

        initBottomNavigationView()
    }

    private fun initBottomNavigationView() = with(binding) {

        bottomNavigationView.itemIconTintList = null
        bottomNavigationView.itemTextColor = null
        //클릭시 퍼지는 색상 변경
//        bottomNavigationView.itemRippleColor = null
    }

    override fun onBackPressed() {
        val currentTime = System.currentTimeMillis()
        val gapTime = currentTime - backButtonTime

        if (navController.currentDestination!!.id == R.id.calendarWithDiaryFragment ||
            navController.currentDestination!!.id == R.id.receivedDiaryFragment ||
            navController.currentDestination!!.id == R.id.diaryListFragment ||
            navController.currentDestination!!.id == R.id.myPageFragment
        ) {
            if (gapTime in 0..2000) {
                // 2초 안에 두번 뒤로가기 누를 시 앱 종료
//                finish()
                finishAndRemoveTask()
                //todo 아래 코드 필요?
                android.os.Process.killProcess(android.os.Process.myPid())
            } else {
                backButtonTime = currentTime
                CodaSnackBar.make(binding.root, "뒤로가기 버튼을 한번 더 누르면 종료됩니다.").show()
            }
        } else {
            super.onBackPressed()
        }
    }

}