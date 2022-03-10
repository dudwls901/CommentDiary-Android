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
import com.movingmaker.commentdiary.CodaApplication
import com.movingmaker.commentdiary.R
import com.movingmaker.commentdiary.base.BaseFragment
import com.movingmaker.commentdiary.databinding.FragmentMydiaryWithCalendarBinding
import com.movingmaker.commentdiary.model.entity.Diary
import com.movingmaker.commentdiary.util.DateConverter
import com.movingmaker.commentdiary.view.main.mydiary.calendardecorator.AloneDotDecorator
import com.movingmaker.commentdiary.view.main.mydiary.calendardecorator.CommentDotDecorator
import com.movingmaker.commentdiary.view.main.mydiary.calendardecorator.SelectedDateDecorator
import com.movingmaker.commentdiary.viewmodel.FragmentViewModel
import com.movingmaker.commentdiary.viewmodel.mydiary.MyDiaryViewModel
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.CalendarMode
import com.prolificinteractive.materialcalendarview.format.ArrayWeekDayFormatter
import com.prolificinteractive.materialcalendarview.format.MonthArrayTitleFormatter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
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
    private val fragmentViewModel: FragmentViewModel by activityViewModels()

    companion object {
        const val TAG: String = "로그"

        fun newInstance(): CalendarWithDiaryFragment {
            return CalendarWithDiaryFragment()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMydiaryWithCalendarBinding.inflate(layoutInflater)
        binding.lifecycleOwner = viewLifecycleOwner
        observeData()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.myDiaryviewModel = myDiaryViewModel
        binding.writeDiaryWrapLayout.isVisible = false
        binding.readDiaryLayout.isVisible = false
        fragmentViewModel.setHasBottomNavi(true)
        Log.d(TAG, "onCreateView: oncreateview가 왜 계속 불리는데")
        Log.d(TAG, "dataSelected : ${myDiaryViewModel.selectedDate.value}")
        Log.d(TAG, "dataSelected : ${myDiaryViewModel.selectedDate.value == null} ${myDiaryViewModel.selectedDate.value == ""}"
        )
        initSwipeRefresh()
        initCalendar()
        initButtons()

    }

    override fun onResume() {
        super.onResume()

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
//                Log.d(TAG, "observeData: 일기를 몇 번 불러와")
//                Toast.makeText(requireContext(), "로그인 성공", Toast.LENGTH_SHORT).show()
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

    private fun initButtons() = with(binding) {
        writeDiaryLayout.setOnClickListener {
            fragmentViewModel.setFragmentState("writeDiary")
        }
        diaryDetailButton.setOnClickListener {
            //todo 임시저장 상태면 writeDiary로,  이미 저장된 상태면 commentDiaryDetail
            Log.d(TAG, "initButtons: ${myDiaryViewModel.selectedDiary.value!!.id}")
            Log.d(TAG, "initButtons: ${fragmentViewModel.fragmentState}")
            if(myDiaryViewModel.selectedDiary.value!!.id!=null && myDiaryViewModel.selectedDiary.value!!.deliveryYN =='Y') {
                fragmentViewModel.setFragmentState("commentDiaryDetail")
            }
            else{
                Log.d(TAG, "initButtons: 여기안와? ${fragmentViewModel.fragmentState}")
                fragmentViewModel.setFragmentState("writeDiary")
            }
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

        val codaToday = DateConverter.getCodaToday()
        val calendarDay =
            CalendarDay.from(codaToday.year, codaToday.monthValue-1 , codaToday.dayOfMonth)
//        launch(coroutineContext) {
//            myDiaryViewModel.setResponseGetMonthDiary(codaToday.format(DateTimeFormatter.ofPattern("yyyy.MM")))
        setMonthCalendarDiaries(codaToday.format(DateTimeFormatter.ofPattern("yyyy.MM")))
        Log.d(TAG, "시작 날짜? ?: ${myDiaryViewModel.selectedDate.value} ${calendarDay}")
        if (myDiaryViewModel.selectedDate.value != null) {
            val (y, m, d) = myDiaryViewModel.selectedDate.value!!.split('.').map { it.toInt() }
            materialCalendarView.currentDate = CalendarDay.from(y, m, d)
            materialCalendarView.selectedDate = CalendarDay.from(y, m, d)
            checkSelectedDate(CalendarDay.from(y, m, d))
        } else {
            Log.d(TAG, "calendarday ${calendarDay}")
            materialCalendarView.currentDate = calendarDay
            materialCalendarView.selectedDate = calendarDay
            checkSelectedDate(calendarDay)
        }
        Log.d(TAG, "설정 이후 날짜? : ${myDiaryViewModel.selectedDate.value} ${calendarDay}")
//        materialCalendarView.selectedDate = calendarDay
//        materialCalendarView.currentDate = calendarDay


        materialCalendarView.setOnDateChangedListener { widget, date, selected ->
            Log.d(TAG, "setOndateChangedListener: 나는 언제 불려? ${date} ")
            checkSelectedDate(date)
        }

        binding.materialCalendarView.setOnMonthChangedListener { widget, date ->
            val requestDate = LocalDate.of(date.year, date.month+1, date.day)
                .format(DateTimeFormatter.ofPattern("yyyy.MM"))
            Log.d(TAG, "@@@@@@@@@@@@@@@@@@@@@@@@@@@@@setOnMonthChangedListener: $requestDate")
            setMonthCalendarDiaries(requestDate)
        }
    }

    private fun setMonthCalendarDiaries(yearMonth: String) {
        Log.d(TAG, "calendar: yearmonth : $yearMonth ")
        //월 변경 시 포커스 x
        if (myDiaryViewModel.selectedDate.value == "") {
            checkSelectedDate(null)
        }
//        binding.materialCalendarView.selectedDate = null
        launch(coroutineContext) {
            myDiaryViewModel.setResponseGetMonthDiary(yearMonth)
        }
    }

    private fun checkSelectedDate(date: CalendarDay?) = with(binding) {
        //fragment context 사용 ->requireContext()
        Log.d(TAG, "checkSelectedDate:codaToday ${DateConverter.ymdToDate(DateConverter.ymdFormat(DateConverter.getCodaToday()))}")
        Log.d(TAG, "checkSelectedDate:codaToday ${DateConverter.getCodaToday()}")
        Log.d(TAG, "checkSelectedDate: ${DateConverter.getCodaToday().minusDays(1)}")
        Log.d(TAG, "checkSelectedDate: ${DateConverter.getCodaToday().minusDays(2)}")
        Log.d(TAG, "checkSelectedDate: ${DateConverter.getCodaToday().minusDays(1) > DateConverter.getCodaToday().minusDays(2)}")
        Log.d(TAG, "checkSelectedDate: ${DateConverter.getCodaToday().minusDays(1) < DateConverter.getCodaToday().minusDays(2)}")
        Log.d(TAG, "checkSelectedDate: ${DateConverter.getCodaToday().minusDays(2) == DateConverter.getCodaToday().minusDays(2)}")
//        Log.d(TAG, "checkSelectedDate: ${DateConverter.getCodaToday().minusDays(2)}")
//        Log.d(TAG, "checkSelectedDate:codaToday ${DateConverter.ymdFormat(DateConverter.getCodaToday())}")

        Log.d(TAG, "checkDate: $date ")
        //달 이동한 경우 포커스 해제
        if (date == null) {
            readDiaryLayout.isVisible = false
            writeDiaryWrapLayout.isVisible = false
            return
        }
        val codaToday = LocalDateTime.now().minusHours(7).toLocalDate()
        val selectedDate = LocalDate.of(date.year, date.month+1, date.day)

        Log.d(TAG, "myDiary selectedDate: ${codaToday} ${selectedDate}")

//        myDiaryViewModel.setSelectedDate(
//            "${date.year}.${
//                String.format(
//                    "%02d",
//                    date.month+1
//                )
//            }.${String.format("%02d", date.day)}"
//        )

//        myDiaryViewModel.setSelectedDate(
//            "${date.year}.${
//                String.format(
//                    "%02d",
//                    date.month+1
//                )
//            }.${String.format("%02d", date.day)}"
//        )
        //미래 날짜면 일기 작성 불가, 조회 불가
        if (selectedDate > codaToday) {
            writeDiaryWrapLayout.isVisible = false
            readDiaryLayout.isVisible = false
            return
        }
        myDiaryViewModel.setDateDiaryText(
            "${String.format("%02d", date.month+1)}월 ${String.format("%02d", date.day)}일 나의 일기"
        )
        Log.d(TAG, "mydiary: ${myDiaryViewModel.selectedDiary.value!!.deliveryYN}")
        //이전 날짜면 검사
        if (myDiaryViewModel.monthDiaries.value?.isNotEmpty() == true) {
            for (diary in myDiaryViewModel.monthDiaries.value!!) {
                Log.d(TAG, "selectedDiary ${diary} ")
                if (diary.date == selectedDate.toString().replace('-', '.')) {
                    //viewmodel로 넘기기
                    myDiaryViewModel.setDeliveryYN(diary.deliveryYN)
                    myDiaryViewModel.setSelectedDiary(diary)
                    myDiaryViewModel.setSaveOrEdit("edit")
                    //코멘트가 아직 도착하지 않은 경우
                    if(diary.commentList==null || diary.commentList.size==0){
                        receivedCommentButton.isVisible = false
                        commentSoonTextView.isVisible = true
                    }
                    else{
                        receivedCommentButton.isVisible = true
                        commentSoonTextView.isVisible = false
                    }
                    if(diary.deliveryYN=='N'){
                        commentSoonTextView.isVisible = false
                    }
                    readDiaryLayout.isVisible = true
                    writeDiaryWrapLayout.isVisible = false
                    return
                }
            }
        }
        //일기가 없다면
        myDiaryViewModel.setSelectedDiary(Diary(null,"","",selectedDate.toString().replace('-','.'),' ',null))
        writeDiaryWrapLayout.isVisible = true
        readDiaryLayout.isVisible = false
        myDiaryViewModel.setSaveOrEdit("save")
        //setselectedDiary
    }

}