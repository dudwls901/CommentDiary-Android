package com.movingmaker.commentdiary.view.onboarding

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.viewpager2.widget.ViewPager2
import com.movingmaker.commentdiary.BaseActivity
import com.movingmaker.commentdiary.R
import com.movingmaker.commentdiary.databinding.ActivityOnboardingIntroBinding
import me.relex.circleindicator.CircleIndicator3

class OnboardingIntroActivity : BaseActivity<ActivityOnboardingIntroBinding>() {
    override val TAG: String = OnboardingIntroActivity::class.java.simpleName
    override val layoutRes = R.layout.activity_onboarding_intro
    private lateinit var onboardingIntroAdapter: OnboardingIntroAdapter
    private lateinit var indicator: CircleIndicator3
    private lateinit var onboardingSignUpSuccessFragment: OnboardingSignUpSuccessFragment
    //todo one activity 나머지 프래그먼트 구조로 변경하기
    //todo Retro 작업 (이메일 send, 회원가입, 로그인, pw 찾기)
    //todo 팝업 작업 (pw찾기, 이메일 send)
    //스플래시
    private var result = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        val intent = Intent(this, OnboardingSignUpActivity::class.java)
        result = intent.getStringExtra("result")?: ""
        initViews()
    }

    private fun initViews() {
        Toast.makeText(this, result, Toast.LENGTH_SHORT).show()
        if(result ==""){
            initOnboardViewPager()
        }
        else{
            onboardingSignUpSuccessFragment = OnboardingSignUpSuccessFragment.newInstance()
            supportFragmentManager.beginTransaction().add(binding.onboardingFrameLayout.id,onboardingSignUpSuccessFragment).commit()
            changeButton()
        }
        initOnboardButton()
    }


    private fun initOnboardButton() = with(binding) {
        //todo 현재 회원가입 화면에서 sendAuth누르면 오게끔 되어있음
        onboardingButton.setOnClickListener {
            if(result=="") {
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
            else{

            }
        }
    }

    private fun changeButton() {
        if(result=="ok"){
            binding.onboardingButton.setBackgroundResource(R.drawable.onboarding_button_green_radius)
            binding.onboardingButton.text = getString(R.string.onboarding_button_write_start)
            binding.onboardingButton.setTextColor(getColor(R.color.white))
        }
        else {
            if (binding.introViewPager.currentItem == 2) {
                binding.onboardingButton.setBackgroundResource(R.drawable.onboarding_button_green_radius)
                binding.onboardingButton.text = getString(R.string.onboarding_button_start)
                binding.onboardingButton.setTextColor(getColor(R.color.white))
            } else {
                binding.onboardingButton.setBackgroundResource(R.drawable.onboarding_button_yellow_radius)
                binding.onboardingButton.text = getString(R.string.onboarding_button_next)
                binding.onboardingButton.setTextColor(getColor(R.color.black))
            }
        }

    }

    private fun initOnboardViewPager() {

        onboardingIntroAdapter = OnboardingIntroAdapter(
            listOf(
                getString(R.string.onboarding_ment_first),
                getString(R.string.onboarding_ment_second),
                getString(R.string.onboarding_ment_third)
            )
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
                changeButton()
            }
        })

    }


}