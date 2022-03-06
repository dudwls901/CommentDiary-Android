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
import com.movingmaker.commentdiary.R
import com.movingmaker.commentdiary.base.BaseFragment
import com.movingmaker.commentdiary.databinding.FragmentMydiaryWithCalendarBinding
import com.movingmaker.commentdiary.view.main.mydiary.calendardecorator.AloneDotDecorator
import com.movingmaker.commentdiary.view.main.mydiary.calendardecorator.CommentDotDecorator
import com.movingmaker.commentdiary.view.main.mydiary.calendardecorator.SelectedDateDecorator
import com.movingmaker.commentdiary.viewmodel.mydiary.MyDiaryViewModel
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.CalendarMode
import com.prolificinteractive.materialcalendarview.format.ArrayWeekDayFormatter
import com.prolificinteractive.materialcalendarview.format.MonthArrayTitleFormatter
import com.prolificinteractive.materialcalendarview.format.TitleFormatter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.text.Format
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

    private lateinit var binding: FragmentMydiaryWithCalendarBinding

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
        binding = FragmentMydiaryWithCalendarBinding.inflate(layoutInflater)
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
                Toast.makeText(requireContext(), "로그인 성공", Toast.LENGTH_SHORT).show()
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
        writeDiaryLayout.setOnClickListener {
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

    @SuppressLint("SimpleDateFormat", "ResourceType")
    private fun initCalendar() = with(binding) {

        materialCalendarView.setTitleFormatter(MonthArrayTitleFormatter(resources.getStringArray(R.array.custom_months)))
        materialCalendarView.setWeekDayFormatter(ArrayWeekDayFormatter(resources.getStringArray(R.array.custom_weekdays)))

        materialCalendarView.setTitleFormatter { day -> // CalendarDay라는 클래스는 LocalDate 클래스를 기반으로 만들어진 클래스다
            // 월, 요일을 한글로 보이게 설정 (MonthArrayTitleFormatter의 작동을 확인하려면 밑의 setTitleFormatter()를 지운다)
            val calendarHeaderBuilder = StringBuilder()
            calendarHeaderBuilder.append(day.year)
                .append("년 ")
                .append(day.month + 1)
                .append("월")
            calendarHeaderBuilder.toString()
        }

        materialCalendarView.state().edit()
            .setFirstDayOfWeek(Calendar.SUNDAY)
            .setMinimumDate(CalendarDay.from(2021, 0, 1))//캘린더 시작 날짜
            .setMaximumDate(CalendarDay.from(2022, 11, 31))//캘린더 끝 날짜
            .setCalendarDisplayMode(CalendarMode.MONTHS) // 월 달력, 주 달력
            .commit()
        materialCalendarView.addDecorator(
            SelectedDateDecorator(requireContext())
        )
//        var calendarCodaToday: Date

        val codaToday = LocalDateTime.now().minusHours(7).toLocalDate()
        val calendarDay =
            CalendarDay.from(codaToday.year, codaToday.monthValue - 1, codaToday.dayOfMonth)
        materialCalendarView.selectedDate = calendarDay
        materialCalendarView.currentDate = calendarDay

        setMonthCalendarDiaries(codaToday.format(DateTimeFormatter.ofPattern("yyyy.MM")))
        checkSelectedDate(calendarDay)


        materialCalendarView.setOnMonthChangedListener { widget, date ->
            val requestDate = LocalDate.of(date.year, date.month + 1, date.day)
                .format(DateTimeFormatter.ofPattern("yyyy.MM"))
            Log.d(TAG, "initCalendar: $requestDate")
            setMonthCalendarDiaries(requestDate)
        }

        materialCalendarView.setOnDateChangedListener { widget, date, selected ->
            checkSelectedDate(date)
        }
    }

    private fun setMonthCalendarDiaries(yearMonth: String) {
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
        //달 이동한 경우 포커스 해제
        if (date == null) {
            readDiaryLayout.isVisible = false
            writeDiaryWrapLayout.isVisible = false
            return
        }
        val codaToday = LocalDateTime.now().minusHours(7).toLocalDate()
        val selectedDate = LocalDate.of(date.year, date.month + 1, date.day)
        //미래 날짜면 일기 작성 불가, 조회 불가
        Log.d(TAG, "$selectedDate $codaToday")
        if (selectedDate > codaToday) {
            writeDiaryWrapLayout.isVisible = false
            readDiaryLayout.isVisible = false
            return
        }
        //이전 날짜면 검사
        myDiaryViewModel.setDateDiaryText(
            "${String.format("%02d", date.month + 1)}월 ${String.format("%02d", date.day)}일 나의 일기"
        )
        if (myDiaryViewModel.monthDiaries.value?.isNotEmpty() == true) {
            for (diary in myDiaryViewModel.monthDiaries.value!!) {
                if (diary.date == selectedDate.toString().replace('-', '.')) {
                    //viewmodel로 넘기기
                    myDiaryViewModel.setSelectedDiary(diary)
                    readDiaryLayout.isVisible = true
                    writeDiaryWrapLayout.isVisible = false
                    myDiaryViewModel.setSaveOrEdit("edit")
                    return
                }
            }
        }
        //일기가 없다면
        writeDiaryWrapLayout.isVisible = true
        readDiaryLayout.isVisible = false
        myDiaryViewModel.setSaveOrEdit("save")
    }

}