package com.movingmaker.commentdiary.view.main.mydiary

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
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
import kotlinx.coroutines.*
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.coroutines.CoroutineContext

//todo fragment show됐을 때 캘린더 리프레쉬 시키기
// 일기 작성하고 다시 메인 왔을 때 갱신이 안됨
//일기 삭제했을 때 닷이 안 없어짐
//todo 작성하기로 가는 탭이 없음
//todo 이전날짜도 마찬가지고 전체보기 누르면 작성하기
//todo 멘트도 바꿔주기

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

        //여기선 라이프사이클 지정 안 하면 데이터바인딩 안 되고, commentdiarydetailfragment에선 안 해도 됨
//        binding.lifecycleOwner = viewLifecycleOwner
        binding.lifecycleOwner = this.viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.myDiaryviewModel = myDiaryViewModel
        Log.d(TAG, "onCreateView: oncreateview  $view")
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
        initSwipeRefresh()
        initViews()
        initButtons()
        observeData()
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
                Log.d(TAG, "observeData: ${myDiaryViewModel.selectedDate.value}")
                //다른 달로 이동했을 때
                if(myDiaryViewModel.selectedDate.value == null){
                    checkSelectedDate(null)
                }
                else {
                    val (y, m, d) = myDiaryViewModel.selectedDate.value!!.split('.').map { it.toInt() }
                    checkSelectedDate(CalendarDay.from(y, m - 1, d))
                    Log.d(TAG, "observeData: $y ${m-1} $d")
                }

                Log.d(TAG, "observeData: responeGetMonthDiary")

//                Toast.makeText(requireContext(), "로그인 성공", Toast.LENGTH_SHORT).show()
            } else {
//                Toast.makeText(requireContext(), "한 달 일기 불러오기 실패", Toast.LENGTH_SHORT).show()
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
            if(myDiaryViewModel.selectedDiary.value!!.tempYN =='N' && myDiaryViewModel.selectedDiary.value!!.deliveryYN=='Y') {
                Log.d(TAG, "initButtons: 여기여기여기${myDiaryViewModel.selectedDiary.value!!.tempYN}")
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

    private fun initViews() = with(binding){
        materialCalendarView.setOnDateChangedListener { widget, date, selected ->
            Log.d(TAG, "setOndateChangedListener: ${date} ")
            checkSelectedDate(date)
        }

        materialCalendarView.setOnMonthChangedListener { widget, date ->
            //선택된 일기 없애주기
            myDiaryViewModel.setSelectedDiary(Diary(null,"","","",' ',' ', null))

            val requestDate = LocalDate.of(date.year, date.month+1, date.day)
                .format(DateTimeFormatter.ofPattern("yyyy.MM"))
            Log.d(TAG, "@@@@@@@@@@@@@@@@@@@@@@@@@@@@@setOnMonthChangedListener: $requestDate")
            setMonthCalendarDiaries(requestDate)
        }
        initCalendar()
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
//        setMonthCalendarDiaries(codaToday.format(DateTimeFormatter.ofPattern("yyyy.MM")))

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
//            materialCalendarView.currentDate = calendarDay
//            materialCalendarView.selectedDate = calendarDay
//            checkSelectedDate(calendarDay)
//        }

    }

    private fun setMonthCalendarDiaries(yearMonth: String) {
        Log.d(TAG, "calendar: yearmonth : $yearMonth ")
        //월 변경 시 포커스 x
        if (myDiaryViewModel.selectedDate.value == "") {
            myDiaryViewModel.setSelectedDate(DateConverter.ymdFormat(DateConverter.getCodaToday()))
        }
        else{
            myDiaryViewModel.setSelectedDate(null)
        }
//        binding.materialCalendarView.selectedDate = null
        //월 변경 시 해당 월의 일기 셋팅

        launch(coroutineContext) {
            myDiaryViewModel.setResponseGetMonthDiary(yearMonth)
        }
    }

    @SuppressLint("ResourceAsColor")
    private fun checkSelectedDate(date: CalendarDay?) = with(binding) {
        Log.d(TAG, "checkSElectedDate: $date ")
        Log.d(TAG, "checkSelectedDate: ${myDiaryViewModel.selectedDiary.value}")
//        materialCalendarView.currentDate = date
            materialCalendarView.selectedDate = date
        //달 이동한 경우 포커스 해제 or 냅두기?
        if (date == null) {
            readDiaryLayout.isVisible = false
            writeDiaryWrapLayout.isVisible = false
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
            noCommentTextView.isVisible = false
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

                    //혼자 일기인 경우
                    if(diary.deliveryYN=='N'){
                        sendDiaryBeforeAfterTextView.isVisible = false
                        noCommentTextView.isVisible = false
                    }
                    //코멘트 일기인 경우
                    else{
                        sendDiaryBeforeAfterTextView.isVisible = true
                        //혼자쓰는일기면 가리고 코멘트 일기면 보이기
                        //코멘트가 아직 도착하지 않은 경우 or 삭제된 경우 or서버에서 빈 값 내려올 땐 null?

                        //todo
                        //todo 서버에서 빈 코멘트올 때 무슨 값인지 확인
                        if (diary.commentList == null || diary.commentList.size == 0) {
                            //코멘트가 도착하지 않았는데 이틀 지난 경우
                            Log.d(TAG, "checkSelectedDate: ${DateConverter.ymdToDate(diary.date) } ${selectedDate.minusDays(2)}")
                            if(DateConverter.ymdToDate(diary.date)<=codaToday.minusDays(2)){
                                sendDiaryBeforeAfterTextView.isVisible = false
                                noCommentTextView.isVisible = diary.tempYN=='N'
                            }
                            //아직 코멘트 기다리는 경우
                            else {
                                //임시저장인 경우
                                sendDiaryBeforeAfterTextView.setTextColor(R.color.text_dark_brown)
                                noCommentTextView.isVisible = false
                                if(diary.tempYN=='Y'){
                                    sendDiaryBeforeAfterTextView.text = getString(R.string.upload_yet_comment_diary)
                                    sendDiaryBeforeAfterTextView.setBackgroundResource(R.drawable.background_brand_orange_radius_bottom_10)
                                }
                                else{
                                    sendDiaryBeforeAfterTextView.setTextColor(R.color.text_brown)
                                    sendDiaryBeforeAfterTextView.text = getString(R.string.calendar_with_diary_comment_soon)
                                    sendDiaryBeforeAfterTextView.setBackgroundResource(R.drawable.background_light_brown_radius_bottom_10)
                                }
                            }
                        }
                        //코멘트 있는 경우
                        else {
                            sendDiaryBeforeAfterTextView.text = getString(R.string.arrived_comment)
                            sendDiaryBeforeAfterTextView.setBackgroundResource(R.drawable.background_pure_green_radius_bottom_10)
                            sendDiaryBeforeAfterTextView.setTextColor(R.color.text_dark_brown)
                            noCommentTextView.isVisible = false
                        }
                    }
                    readDiaryLayout.isVisible = true
                    writeDiaryWrapLayout.isVisible = false
                    return
                }
            }
        }

        //일기가 없다면
        myDiaryViewModel.setSelectedDiary(Diary(
            null,
            "",
            "",
            selectedDate.toString().replace('-','.'),
            ' ',
            ' ',
            null)
        )
        Log.d(TAG, "checkSelectedDate: 일기 없음${myDiaryViewModel.selectedDiary.value}")
        writeDiaryWrapLayout.isVisible = true
        writeDiaryLayout.isVisible = true
        lineDecorationTextView.text = getString(R.string.mydiary_text_decoration)
        readDiaryLayout.isVisible = false
        noCommentTextView.isVisible = false
    }

}