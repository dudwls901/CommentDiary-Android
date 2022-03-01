package com.movingmaker.commentdiary.view.main

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import com.movingmaker.commentdiary.base.BaseActivity
import com.movingmaker.commentdiary.CodaApplication
import com.movingmaker.commentdiary.R
import com.movingmaker.commentdiary.databinding.ActivityMainBinding
import com.movingmaker.commentdiary.view.main.mydiary.CalendarWithDiaryFragment
import com.movingmaker.commentdiary.view.main.mypage.TempMyPageFragment
import com.movingmaker.commentdiary.viewmodel.mypage.MyPageViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.first
import kotlin.coroutines.CoroutineContext

class MainActivity : BaseActivity<ActivityMainBinding>(), CoroutineScope {
    override val TAG: String = MainActivity::class.java.simpleName
    //test
    override val layoutRes: Int = R.layout.activity_main

    private val job = Job()

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    private val myPageViewModel: MyPageViewModel by viewModels()

    private lateinit var calendarWithDiaryFragment: CalendarWithDiaryFragment
    private lateinit var fragment2: Fragment2
    private lateinit var fragment3: Fragment3
    private lateinit var tempMyPageFragment: TempMyPageFragment


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initBottomNavigationView()
    }

    private fun initBottomNavigationView() = with(binding){
        calendarWithDiaryFragment = CalendarWithDiaryFragment.newInstance()
        fragment2 = Fragment2.newInstance()
        fragment3 = Fragment3.newInstance()
        tempMyPageFragment = TempMyPageFragment.newInstance()
        supportFragmentManager.beginTransaction().add(binding.fragmentContainer.id, calendarWithDiaryFragment).commit()

        bottomNavigationView.setOnItemSelectedListener{menu->
            when(menu.itemId){
                R.id.fragment1-> replaceFragment(calendarWithDiaryFragment)
                R.id.fragment2-> replaceFragment(fragment2)
                R.id.fragment3-> replaceFragment(fragment3)
                R.id.fragment4-> replaceFragment(tempMyPageFragment)
            }
            true
        }

    }

    private fun replaceFragment(fragment: Fragment) {
        //액티비티에는 서포트프래그먼트매니저가 있다. 각각 액티비티에 attach되어있는 프래그먼트를 관리
        //트랜잭션 : 이 작업이 시작함을 알리고 커밋까지는 요 작업만 하게끔
        var accessToken = ""
        var refreshToken =""
        var accessTokenExpiresIn = 0L
        launch(coroutineContext) {
            accessToken = withContext(Dispatchers.IO) {
                CodaApplication.getInstance().getDataStore().accessToken.first()
            }
            refreshToken = withContext(Dispatchers.IO) {
                CodaApplication.getInstance().getDataStore().refreshToken.first()
            }
            accessTokenExpiresIn = withContext(Dispatchers.IO) {
                CodaApplication.getInstance().getDataStore().accessTokenExpiresIn.first()
            }
            Log.d(TAG, "replaceFragment: $accessToken + \" \" + $refreshToken + \" \" + $accessTokenExpiresIn")
            Toast.makeText(this@MainActivity, accessToken + " " + refreshToken + " " + accessTokenExpiresIn, Toast.LENGTH_SHORT).show()
        }
        supportFragmentManager.beginTransaction().apply {
            //R.id.fragmentContainer
            replace(binding.fragmentContainer.id, fragment)
            commit()
        }
    }
    
}