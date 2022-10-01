package com.movingmaker.commentdiary.presentation.view.onboarding

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.viewpager2.widget.ViewPager2
import com.movingmaker.commentdiary.R
import com.movingmaker.commentdiary.databinding.ActivityOnboardingIntroBinding
import com.movingmaker.commentdiary.common.base.BaseActivity
import com.movingmaker.commentdiary.presentation.viewmodel.onboarding.IntroViewModel
import kotlinx.coroutines.*
import me.relex.circleindicator.CircleIndicator3
import kotlin.coroutines.CoroutineContext

class OnboardingIntroActivity : BaseActivity<ActivityOnboardingIntroBinding>(R.layout.activity_onboarding_intro),CoroutineScope {
    override val TAG: String = OnboardingIntroActivity::class.java.simpleName

    //    private val TAG: String = OnboardingIntroActivity::class.java.simpleName
    private lateinit var onboardingIntroAdapter: OnboardingIntroAdapter
    private lateinit var indicator: CircleIndicator3
    val introViewModel: IntroViewModel by viewModels()
    private val job = Job()

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    //Todo 스플래시 31>=, 31< 대응... 자동로그인이냐 인트로 화면이냐
    override fun onCreate(savedInstanceState: Bundle?) {
        //31버전보다 아래일 시
//        if(Build.VERSION.SDK_INT< Build.VERSION_CODES.S){
//            setTheme(R.style.Theme_CommentDiary)
//        }
        super.onCreate(savedInstanceState)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.statusBarColor = getColor(R.color.background_ivory)
        initViews()
        observeDatas()
    }

    private fun observeDatas(){
        introViewModel.introImage.observe(this){image->
            binding.introLayout.setBackgroundResource(image)
        }
    }

    private fun initViews() {
        initOnboardViewPager()
        initOnboardButton()
    }


    private fun initOnboardButton() = with(binding) {
        onboardingButton.setOnClickListener {
            if (introViewPager.currentItem == 2) {
                startActivity(
                    Intent(
                        this@OnboardingIntroActivity,
                        OnboardingLoginActivity::class.java
                    )
                )
                finish()
            } else {
                introViewPager.setCurrentItem(introViewPager.currentItem + 1, true)
                changeButton()
            }
        }
    }

    @SuppressLint("ResourceAsColor")
    private fun changeButton() {
        if (binding.introViewPager.currentItem == 2) {
            binding.onboardingButton.setBackgroundResource(R.drawable.background_green_radius_10)
            binding.onboardingButton.text = getString(R.string.onboarding_button_start)
            binding.onboardingButton.setTextColor(getColor(R.color.background_ivory))
        } else {
            binding.onboardingButton.setBackgroundResource(R.drawable.background_beige_radius_10)
            binding.onboardingButton.text = getString(R.string.onboarding_button_next)
            binding.onboardingButton.setTextColor(getColor(R.color.text_brown))
        }


    }

    private fun initOnboardViewPager() {

        onboardingIntroAdapter = OnboardingIntroAdapter(
            listOf(
                getString(R.string.onboarding_ment_first),
                getString(R.string.onboarding_ment_second),
                getString(R.string.onboarding_ment_third)
            ),
            context = this
        )

        binding.introViewPager.adapter = onboardingIntroAdapter
        binding.introViewPager.setCurrentItem(0, false)
        binding.introViewPager.orientation = ViewPager2.ORIENTATION_HORIZONTAL

        indicator = binding.introIndicator
        indicator.setViewPager(binding.introViewPager)
        indicator.createIndicators(3, 0)


        binding.introViewPager.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                introViewModel.setIntroPageNum(position)
//                Log.d("???????????", theme.toString())
                setTheme(R.style.Theme_CommentDiary)
//                Log.d("???????????", theme.toString())
                changeButton()
            }
        })

    }
}