package com.movingmaker.presentation.view.main

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.core.view.get
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.movingmaker.presentation.R
import com.movingmaker.presentation.base.BaseActivity
import com.movingmaker.presentation.databinding.ActivityMainBinding
import com.movingmaker.presentation.util.FRAGMENT_NAME
import com.movingmaker.presentation.util.event.EventObserver
import com.movingmaker.presentation.view.onboarding.OnboardingLoginActivity
import com.movingmaker.presentation.view.snackbar.CodaSnackBar
import com.movingmaker.presentation.viewmodel.FragmentViewModel
import com.movingmaker.presentation.viewmodel.gatherdiary.GatherDiaryViewModel
import com.movingmaker.presentation.viewmodel.mydiary.MyDiaryViewModel
import com.movingmaker.presentation.viewmodel.mypage.MyPageViewModel
import com.movingmaker.presentation.viewmodel.receiveddiary.ReceivedDiaryViewModel
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>(R.layout.activity_main),
    SharedPreferences.OnSharedPreferenceChangeListener {

    @Inject
    lateinit var sharedPreferences: SharedPreferences

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
        Timber.d("onNewIntent: push $pushDate")
        if (pushDate != null) {
            myDiaryViewModel.setPushDate(pushDate!!)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.vm = fragmentViewModel
        registerOnSharedPreferenceChangeListener()
        initViews()
        observerFragments()
        observerDatas()

        receivedDiaryViewModel.getReceiveDiary()

        pushDate = intent.getStringExtra("pushDate")
        //푸시로 들어온 경우 바로 코멘트 화면으로
        Timber.d("oncreate: push $pushDate")
        if (pushDate != null) {
            myDiaryViewModel.setPushDate(pushDate!!)
        }
    }

    private fun registerOnSharedPreferenceChangeListener() {
        if (this::sharedPreferences.isInitialized) {
            sharedPreferences.registerOnSharedPreferenceChangeListener(this)
        }
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        startActivity(
            Intent(this, OnboardingLoginActivity::class.java)
        )
        finish()
    }


    private fun observerDatas() {

        myPageViewModel.snackMessage.observe(this, EventObserver {
            CodaSnackBar.make(binding.root, it).show()
        })

        myDiaryViewModel.snackMessage.observe(this, EventObserver {
            CodaSnackBar.make(binding.root, it).show()
        })

        gatherDiaryViewModel.snackMessage.observe(this, EventObserver {
            CodaSnackBar.make(binding.root, it).show()
        })

        receivedDiaryViewModel.snackMessage.observe(this, EventObserver {
            CodaSnackBar.make(binding.root, it).show()
        })

        receivedDiaryViewModel.receivedDiary.observe(this) {
            if (it != null) {
                if (it.myComment.isNotEmpty()) {
                    binding.bottomNavigationView.menu[1].icon =
                        ContextCompat.getDrawable(this, R.drawable.bottom_ic_received)
                } else {
                    //코멘트가 없다면
                    binding.bottomNavigationView.menu[1].icon =
                        ContextCompat.getDrawable(this, R.drawable.bottom_ic_received_notice)
                }
            } else {
                //도착한 일기가 없다면
                binding.bottomNavigationView.menu[1].icon =
                    ContextCompat.getDrawable(this, R.drawable.bottom_ic_received)
            }

        }
    }

    private fun observerFragments() {
        fragmentViewModel.curFragment.observe(this) { fragment ->
            Timber.d("observerFragments: nav ${navController.currentDestination?.label}")
            Timber.d("observerFragments: frag $fragment")
            //마이페이지만 스테이터스바 흰색
            window.statusBarColor = getColor(
                when (fragment) {
                    FRAGMENT_NAME.MY_PAGE,
                    FRAGMENT_NAME.MY_ACCOUNT,
                    FRAGMENT_NAME.SIGN_OUT,
                    FRAGMENT_NAME.TERMS,
                    FRAGMENT_NAME.SENDED_COMMENT_LIST,
                    FRAGMENT_NAME.CHANGE_PASSWORD,
                    FRAGMENT_NAME.PUSHALARM_ONOFF,
                    FRAGMENT_NAME.WRITE_DIARY,
                    FRAGMENT_NAME.ALONE_DIARY_DETAIL,
                    FRAGMENT_NAME.COMMENT_DIARY_DETAIL -> {
                        R.color.background_ivory
                    }
                    else -> {
                        R.color.core_beige
                    }
                }
            )
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
//                android.os.Process.killProcess(android.os.Process.myPid())
            } else {
                backButtonTime = currentTime
                CodaSnackBar.make(binding.root, "뒤로가기 버튼을 한번 더 누르면 종료됩니다.").show()
            }
        } else {
            super.onBackPressed()
        }
    }

}