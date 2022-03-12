package com.movingmaker.commentdiary.view.main.mydiary

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
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
import com.movingmaker.commentdiary.viewmodel.mydiary.LocalDiaryViewModel
import com.movingmaker.commentdiary.viewmodel.mydiary.MyDiaryViewModel
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.CalendarMode
import com.prolificinteractive.materialcalendarview.format.ArrayWeekDayFormatter
import com.prolificinteractive.materialcalendarview.format.MonthArrayTitleFormatter
import kotlinx.coroutines.*
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
    private val localDiaryViewModel: LocalDiaryViewModel by activityViewModels()

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

        //여기선 라이프사이클 지정 안 하면 데이터바인딩 안 되고, commentdiarydetailfragment에선 안 해도 됨
//        binding.lifecycleOwner = viewLifecycleOwner
        binding.lifecycleOwner = this.viewLifecycleOwner
        observeData()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.myDiaryviewModel = myDiaryViewModel
        fragmentViewModel.setHasBottomNavi(true)
        Log.d(TAG, "onCreateView: oncreateview가 왜 계속 불리는데")
//        Log.d(TAG, "dataSelected : ${myDiaryViewModel.selectedDate.value}")
//        Log.d(TAG, "dataSelected : ${myDiaryViewModel.selectedDate.value == null} ${myDiaryViewModel.selectedDate.value == ""}")
//
//        launch(coroutineContext) {
//            binding.loadingBar.isVisible = true
//            withContext(Dispatchers.IO) {
//                localDiaryViewModel.getAll()
//            }
//            binding.loadingBar.isVisible = false
//
//        }
        Log.d(TAG, "onViewCreated: localDiaryViewModel ${localDiaryViewModel.localDiaryList.value!!}")
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
        //일기가 없는 경우 writeDiary
        writeDiaryLayout.setOnClickListener {
            fragmentViewModel.setFragmentState("writeDiary")
            Log.d(TAG, "replace before ${fragmentViewModel.fragmentState.value}")
        }
        //일기가 있는 경우
        diaryDetailLayout.setOnClickListener {
            //임시저장 상태면 writeDiary로, 이미 저장된 상태면 commentDiaryDetail
            Log.d(TAG, "initButtons: ${myDiaryViewModel.selectedDiary.value!!.id}")
            Log.d(TAG, "initButtons: ${fragmentViewModel.fragmentState}")
            //서버에 저장된 코멘트 일기인 경우
            if(myDiaryViewModel.selectedDiary.value!!.id!=null && myDiaryViewModel.selectedDiary.value!!.deliveryYN =='Y') {
                fragmentViewModel.setFragmentState("commentDiaryDetail")
            }
            //임시저장(코멘트일기)인 경우, 혼자쓴 일기인 경우
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

        //캘린더 현재 달로 일기 초기화
        setMonthCalendarDiaries(codaToday.format(DateTimeFormatter.ofPattern("yyyy.MM")))

        val selectedDate = myDiaryViewModel.selectedDiary.value!!.date
        //선택한 날짜 있으면 선택한 날짜로 캘린더 닷 설정
//        if (selectedDate != "") {
//            val (y, m, d) = selectedDate.split('.').map { it.toInt() }
//            materialCalendarView.currentDate = CalendarDay.from(y, m, d)
//            materialCalendarView.selectedDate = CalendarDay.from(y, m, d)
//            checkSelectedDate(CalendarDay.from(y, m, d))
//        }
//        //선택한 날짜 없으면 오늘 날짜로 캘린더 닷 설정
//        else {
            Log.d(TAG, "calendarday ${calendarDay}")
            materialCalendarView.currentDate = calendarDay
            materialCalendarView.selectedDate = calendarDay
            checkSelectedDate(calendarDay)
//        }
//        materialCalendarView.selectedDate = calendarDay
//        materialCalendarView.currentDate = calendarDay


        materialCalendarView.setOnDateChangedListener { widget, date, selected ->
            Log.d(TAG, "setOndateChangedListener: 나는 언제 불려? ${date} ")
            checkSelectedDate(date)
        }

        binding.materialCalendarView.setOnMonthChangedListener { widget, date ->
            //선택된 일기 없애주기
            myDiaryViewModel.setSelectedDiary(Diary(null,"","","",' ',null))

            val requestDate = LocalDate.of(date.year, date.month+1, date.day)
                .format(DateTimeFormatter.ofPattern("yyyy.MM"))
            Log.d(TAG, "@@@@@@@@@@@@@@@@@@@@@@@@@@@@@setOnMonthChangedListener: $requestDate")
            setMonthCalendarDiaries(requestDate)
        }
    }

    private fun setMonthCalendarDiaries(yearMonth: String) {
        Log.d(TAG, "calendar: yearmonth : $yearMonth ")
        //월 변경 시 포커스 x
        if (myDiaryViewModel.selectedDiary.value!!.date == "") {
            checkSelectedDate(null)
        }
//        binding.materialCalendarView.selectedDate = null
        //월 변경 시 해당 월의 일기 셋팅

        launch(coroutineContext) {
            myDiaryViewModel.setResponseGetMonthDiary(yearMonth)
            localDiaryViewModel.getAll()
        }
    }

    @SuppressLint("ResourceAsColor")
    private fun checkSelectedDate(date: CalendarDay?) = with(binding) {
        Log.d(TAG, "checkSElectedDate: $date ")
        Log.d(TAG, "checkSelectedDate: ${myDiaryViewModel.selectedDiary.value}")
        //달 이동한 경우 포커스 해제
        if (date == null) {
            readDiaryLayout.isVisible = false
            writeDiaryWrapLayout.isVisible = false
            //todo 미래 날짜 눌렀을 때랑 같은 화면 띄우기
            return
        }
        val codaToday = DateConverter.getCodaToday()
        //calendar date는 month가 0부터 시작하기 때문에 이를 다시 localDate로 바꿀 땐 +1
        val selectedDate = LocalDate.of(date.year, date.month+1, date.day)

//        Log.d(TAG, "myDiary selectedDate: ${codaToday} ${selectedDate}")

        //미래 날짜면 일기 작성 불가, 조회 불가
        if (selectedDate > codaToday) {
            writeDiaryWrapLayout.isVisible = true
            writeDiaryLayout.isVisible = false
            lineDecorationTextView.text = getString(R.string.write_diary_yet)
            readDiaryLayout.isVisible = false
            //todo 미래날짜 화면 띄우기
            return
        }
        //시간 남으면 databinding으로 옮기기
        myDiaryViewModel.setDateDiaryText(
            "${String.format("%02d", date.month+1)}월 ${String.format("%02d", date.day)}일 나의 일기"
        )
//        Log.d(TAG, "chekcselectedDate selectedDiary.value.deliverYN: ${myDiaryViewModel.selectedDiary.value!!.deliveryYN}")

        //이전 날짜면 검사
        if (myDiaryViewModel.monthDiaries.value?.isNotEmpty() == true) {
            for (diary in myDiaryViewModel.monthDiaries.value!!) {
                //작성한 일기가 있다면
                if (diary.date == selectedDate.toString().replace('-', '.')) {
                    //viewmodel로 넘기기
                    myDiaryViewModel.setSelectedDiary(diary)
                    Log.d(TAG, "checkSelectedDate: 일기 있음${myDiaryViewModel.selectedDiary.value}")
//                    myDiaryViewModel.setDeliveryYN(diary.deliveryYN)
//                    myDiaryViewModel.setSelectedDiary(diary)
//                    myDiaryViewModel.setSaveOrEdit("edit")
                    //코멘트일기 관련
//                    sendDiaryBeforeAfterTextView.text = getString(R.string.calendar_with_diary_comment_soon)
//                    sendDiaryBeforeAfterTextView.setBackgroundResource(R.drawable.background_pure_green_radius_bottom_10)
//                    sendDiaryBeforeAfterTextView.setTextColor(Color.parseColor("#fdfcf9"))

//                    Log.d(TAG, "selectedDiary ${diary} ")
                    //혼자쓰는일기면 가리고 코멘트 일기면 보이기
                    sendDiaryBeforeAfterTextView.isVisible = diary.deliveryYN != 'N'

                    //코멘트가 아직 도착하지 않은 경우 or 삭제된 경우 or서버에서 빈 값 내려올 땐 null?
                    //todo 서버에서 빈 코멘트올 때 무슨 값인지 확인
                    if (diary.commentList == null || diary.commentList.size == 0) {
                        sendDiaryBeforeAfterTextView.text = getString(R.string.calendar_with_diary_comment_soon)
                        sendDiaryBeforeAfterTextView.setBackgroundResource(R.drawable.background_light_brown_radius_bottom_10)
                        sendDiaryBeforeAfterTextView.setTextColor(R.color.text_dark_brown)
                    }
                    //코멘트 있는 경우
                    else {
                        sendDiaryBeforeAfterTextView.text = getString(R.string.arrived_comment)
                        sendDiaryBeforeAfterTextView.setBackgroundResource(R.drawable.background_pure_green_radius_bottom_10)
                        sendDiaryBeforeAfterTextView.setTextColor(R.color.text_dark_brown)
                    }

                    readDiaryLayout.isVisible = true
                    writeDiaryWrapLayout.isVisible = false
                    return
                }
            }
        }

        //임시저장 일기 있다면
        //todo 임시저장 일기도 한 달치로 바꾸기
        for(diary in localDiaryViewModel.localDiaryList.value!!){
            if(diary.date == selectedDate.toString().replace('-', '.')){
//                Log.d(TAG, "checkSelectedDate: localdiary ${diary.date} ${selectedDate.toString().replace('-', '.')}")

                //selectedDiary 대입
                myDiaryViewModel.setSelectedDiary(Diary(null,
                    diary.title,
                    diary.content,
                    diary.date,
                    diary.deliveryYN,
                    null
                )
                )
                Log.d(TAG, "checkSelectedDate: 임시저장 일기 있음${myDiaryViewModel.selectedDiary.value}")
                readDiaryLayout.isVisible = true
                writeDiaryWrapLayout.isVisible = false

                //서버 저장된 일기는 xml데이터바인딩처리되어있음 임시저장은 여기서 처리
                diaryHeadTextView.text = diary.title
                diaryContentsTextView.text = diary.content

                sendDiaryBeforeAfterTextView.isVisible = true
                sendDiaryBeforeAfterTextView.text = getString(R.string.upload_yet_comment_diary)
                sendDiaryBeforeAfterTextView.setBackgroundResource(R.drawable.background_brand_orange_radius_bottom_10)
                sendDiaryBeforeAfterTextView.setTextColor(R.color.text_dark_brown)

                return
            }
        }
        //일기가 없다면
        myDiaryViewModel.setSelectedDiary(Diary(
            null,
            "",
            "",
            selectedDate.toString().replace('-','.'),' ',
            null)
        )
        Log.d(TAG, "checkSelectedDate: 일기 없음${myDiaryViewModel.selectedDiary.value}")
        writeDiaryWrapLayout.isVisible = true
        writeDiaryLayout.isVisible = true
        lineDecorationTextView.text = getString(R.string.mydiary_text_decoration)
        readDiaryLayout.isVisible = false

    }

}