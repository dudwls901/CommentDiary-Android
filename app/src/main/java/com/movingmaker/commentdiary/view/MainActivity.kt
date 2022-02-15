package com.movingmaker.commentdiary.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.movingmaker.commentdiary.R
import com.movingmaker.commentdiary.databinding.ActivityMainBinding
import com.movingmaker.commentdiary.view.main.CalendarWithDiaryFragment
import com.movingmaker.commentdiary.view.main.Fragment2
import com.movingmaker.commentdiary.view.main.Fragment3
import com.movingmaker.commentdiary.view.main.Fragment4

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var calendarWithDiaryFragment: CalendarWithDiaryFragment
    private lateinit var fragment2: Fragment2
    private lateinit var fragment3: Fragment3
    private lateinit var fragment4: Fragment4


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initBottomNavigationView()
    }

    private fun initBottomNavigationView() = with(binding){
        calendarWithDiaryFragment = CalendarWithDiaryFragment.newInstance()
        fragment2 = Fragment2.newInstance()
        fragment3 = Fragment3.newInstance()
        fragment4 = Fragment4.newInstance()
        supportFragmentManager.beginTransaction().add(binding.fragmentContainer.id, calendarWithDiaryFragment).commit()

        bottomNavigationView.setOnItemReselectedListener{menu->
            when(menu.itemId){
                R.id.fragment1-> replaceFragment(calendarWithDiaryFragment)
                R.id.fragment2-> replaceFragment(fragment2)
                R.id.fragment3-> replaceFragment(fragment3)
                R.id.fragment4-> replaceFragment(fragment4)
            }
            true
        }

    }

    private fun replaceFragment(fragment: Fragment) {
        //액티비티에는 서포트프래그먼트매니저가 있다. 각각 액티비티에 attach되어있는 프래그먼트를 관리
        //트랜잭션 : 이 작업이 시작함을 알리고 커밋까지는 요 작업만 하게끔
        supportFragmentManager.beginTransaction().apply {
            //R.id.fragmentContainer
            replace(binding.fragmentContainer.id, fragment)
            commit()
        }
    }

}