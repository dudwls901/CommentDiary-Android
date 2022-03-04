package com.movingmaker.commentdiary.view.main.mydiary

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import com.movingmaker.commentdiary.base.BaseFragment
import com.movingmaker.commentdiary.databinding.FragmentWritediaryCalendarWithDiaryBinding
import com.movingmaker.commentdiary.model.entity.Diary
import com.movingmaker.commentdiary.view.main.mydiary.calendardecorator.AloneDotDecorator
import com.movingmaker.commentdiary.view.main.mydiary.calendardecorator.CommentDotDecorator
import com.movingmaker.commentdiary.viewmodel.mydiary.MyDiaryViewModel
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.CalendarMode
import com.prolificinteractive.materialcalendarview.format.DateFormatDayFormatter
import kotlinx.coroutines.*
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.coroutines.CoroutineContext

class CalendarWithDiaryFragment : BaseFragment(), CoroutineScope {
    override val TAG: String = CalendarWithDiaryFragment::class.java.simpleName

    private val job = Job()

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    private lateinit var binding: FragmentWritediaryCalendarWithDiaryBinding

    private val myDiaryViewModel: MyDiaryViewModel by activityViewModels()

    companion object {
        const val TAG: String = "로그"

        fun newInstance(): CalendarWithDiaryFragment {
            return CalendarWithDiaryFragment()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentWritediaryCalendarWithDiaryBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding.myDiaryviewModel = myDiaryViewModel
        binding.lifecycleOwner = viewLifecycleOwner


        initSwipeRefresh()
        initCalendar()
        initWriteButton()
        observeData()

        return binding.root
    }

    private fun observeData() {
        myDiaryViewModel.responseGetMonthDiary.observe(viewLifecycleOwner) {
            if (it.isSuccessful) {
//                Log.d(TAG, (it.body()?.code ?: 0).toString())
//                Log.d(TAG, it.body()?.message ?: "FAIL")
//                Log.d(TAG, it.body()?.result?.size.toString())
//                Log.d(TAG, it.body()?.result?.get(0)?.content ?: null)
//                Log.d(TAG, (it.body()?.result?.get(0)?.id ?: 0).toString())
//                Log.d(TAG, it.body()?.result?.get(0)?.title ?: "no")
//                Log.d(TAG, it.body()?.result?.get(0)?.date ?: "no")
//                Log.d(TAG, (it.body()?.result?.get(0)?.deliveryYN ?: " "))
//                Log.d(TAG, it.body()?.result?.get(0)?.commentList?.isEmpty().toString())
                myDiaryViewModel.setMonthDiaries(it.body()!!.result)
                Toast.makeText(requireContext(), "로그인 성공" + it.body(), Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireContext(), "한 달 일기 불러오기 실패", Toast.LENGTH_SHORT).show()
            }
        }
        myDiaryViewModel.aloneDiary.observe(viewLifecycleOwner) {
            binding.materialCalendarView.addDecorator(
                AloneDotDecorator(requireContext(), myDiaryViewModel.aloneDiary.value!!)
            )
        }
        myDiaryViewModel.commentDiary.observe(viewLifecycleOwner) {
            binding.materialCalendarView.addDecorator(
                CommentDotDecorator(requireContext(), myDiaryViewModel.commentDiary.value!!)
            )
        }
    }

    private fun initWriteButton() = with(binding) {
        writeDiaryLinearLayout.setOnClickListener {
            val intent = Intent(requireContext(), WriteDiaryActivity::class.java)
            startActivity(intent)
        }
    }

    private fun initSwipeRefresh() = with(binding) {
        //Todo swipe 이벤트 처리
        swipeRefreshLayout.setOnRefreshListener {
            //위의 동작 완료시 리프레쉬 종료
            swipeRefreshLayout.isRefreshing = false
        }
    }

    @SuppressLint("SimpleDateFormat")
    private fun initCalendar() = with(binding) {
        materialCalendarView.state().edit()
            .setFirstDayOfWeek(Calendar.SUNDAY)
            .setMinimumDate(CalendarDay.from(2021, 0, 1))//캘린더 시작 날짜
            .setMaximumDate(CalendarDay.from(2022, 11, 31))//캘린더 끝 날짜
            .setCalendarDisplayMode(CalendarMode.MONTHS) // 월 달력, 주 달력
            .commit()

//        var calendarCodaToday: Date
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val codaToday = LocalDateTime.now().minusHours(7).toLocalDate()
            val calendarDay = CalendarDay.from(codaToday.year, codaToday.monthValue-1, codaToday.dayOfMonth)
            materialCalendarView.selectedDate = calendarDay
            materialCalendarView.currentDate = calendarDay

            setMonthCalendarDiaries(codaToday.format(DateTimeFormatter.ofPattern("yyyy.MM")))
            checkSelectedDate(calendarDay)
        }
        else{
            //7시간 뺀 날짜
            val codaToday = Calendar.getInstance()
            codaToday.add(Calendar.HOUR, -7)
            materialCalendarView.selectedDate = CalendarDay.from(codaToday)
            checkSelectedDate(CalendarDay.from(codaToday))
        }

        materialCalendarView.setOnMonthChangedListener { widget, date ->


            val requestDate =  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                LocalDate.of(date.year, date.month+1,date.day).format(DateTimeFormatter.ofPattern("yyyy.MM"))
            } else {
                SimpleDateFormat("yyyy.mm").format(date)
            }
            Log.d(TAG, "initCalendar: $requestDate")
            setMonthCalendarDiaries(requestDate)


        }

        materialCalendarView.setOnDateChangedListener { widget, date, selected ->
            checkSelectedDate(date)
        }
    }

    private fun setMonthCalendarDiaries(yearMonth: String){
        Log.d(TAG, "calendar: yearmonth : $yearMonth ")
        //월 변경 시 포커스 x
        checkSelectedDate(null)
        launch(coroutineContext) {
            myDiaryViewModel.setResponseGetMonthDiary(yearMonth)
        }
    }

    private fun checkSelectedDate(date: CalendarDay?) = with(binding) {
        //fragment context 사용 ->requireContext()
        Log.d(TAG, "checkSelectedDate: $date ")
        if(date==null){
            readDiaryConstraintLayout.isVisible = false
            writeDiaryLinearLayout.isVisible = false
            return
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            Log.d(TAG, "checkSelectedDate: 1 ")
            val codaToday = LocalDateTime.now().minusHours(7).toLocalDate()
            val selectedDate = LocalDate.of(date.year, date.month+1, date.day)
            //미래 날짜면 일기 작성 불가, 조회 불가
            Log.d(TAG, "$selectedDate $codaToday")
            if(selectedDate>codaToday){
                readDiaryConstraintLayout.isVisible = false
                writeDiaryLinearLayout.isVisible = false
                return
            }
            //이전 날짜면 검사
            else{
                if (myDiaryViewModel.monthDiaries.value?.isNotEmpty() == true) {
                    Log.d(TAG, "$selectedDate $codaToday 2")
                    for (diary in myDiaryViewModel.monthDiaries.value!!) {
//                        val ymd = diary.date
                        Log.d(TAG, "${diary.date}  ${selectedDate.toString().replace('-','.')} 3")
                        if (diary.date == selectedDate.toString().replace('-','.')) {

                            Toast.makeText(requireContext(), "오레오 버전 이상 ${diary.date}  $selectedDate", Toast.LENGTH_SHORT).show()
                            readDiaryConstraintLayout.isVisible = true
                            writeDiaryLinearLayout.isVisible = false
                            return
                        }
                    }
                }
            }
        }
        //오레오 버전 이하
        else{
            Log.d(TAG, "checkSelectedDate: 2 ")
            val codaToday = Calendar.getInstance()
            val selectedDate = Calendar.getInstance()
            codaToday.add(Calendar.HOUR, -7)
            selectedDate.set(date.year, date.month, date.day)

            //미래 날짜면 모두 제거
            if (selectedDate > codaToday) {
                readDiaryConstraintLayout.isVisible = false
                writeDiaryLinearLayout.isVisible = false
                return
            }
            if (myDiaryViewModel.monthDiaries.value?.isNotEmpty() == true) {
                for (diary in myDiaryViewModel.monthDiaries.value!!) {
                    val ymdFormat = SimpleDateFormat("yyyy.mm.dd")
                    if (diary.date == ymdFormat.format(selectedDate.time)) {
                        Toast.makeText(requireContext(), "오레오 버전 이하 ${diary.date} ${ymdFormat.format(selectedDate)}" , Toast.LENGTH_SHORT).show()
                        readDiaryConstraintLayout.isVisible = true
                        writeDiaryLinearLayout.isVisible = false
                        return
                    }
                }
            }
        }

        writeDiaryLinearLayout.isVisible = true
        readDiaryConstraintLayout.isVisible = false
    }

}