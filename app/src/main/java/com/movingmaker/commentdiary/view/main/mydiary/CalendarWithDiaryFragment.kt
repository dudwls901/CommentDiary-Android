package com.movingmaker.commentdiary.view.main.mydiary

import android.annotation.SuppressLint
import android.graphics.Point
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.util.DisplayMetrics
import android.util.Log
import android.view.Display
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.core.view.marginEnd
import androidx.core.view.marginTop
import androidx.fragment.app.activityViewModels
import com.movingmaker.commentdiary.R
import com.movingmaker.commentdiary.base.BaseFragment
import com.movingmaker.commentdiary.databinding.FragmentMydiaryWithCalendarBinding
import com.movingmaker.commentdiary.global.CodaSnackBar
import com.movingmaker.commentdiary.model.entity.Diary
import com.movingmaker.commentdiary.model.remote.RetrofitClient
import com.movingmaker.commentdiary.util.DateConverter
import com.movingmaker.commentdiary.util.Extension
import com.movingmaker.commentdiary.util.Extension.toDp
import com.movingmaker.commentdiary.util.Extension.toPx
import com.movingmaker.commentdiary.view.main.mydiary.calendardecorator.AloneDotDecorator
import com.movingmaker.commentdiary.view.main.mydiary.calendardecorator.CommentDotDecorator
import com.movingmaker.commentdiary.view.main.mydiary.calendardecorator.SelectedDateDecorator
import com.movingmaker.commentdiary.viewmodel.FragmentViewModel
import com.movingmaker.commentdiary.viewmodel.mydiary.MyDiaryViewModel
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.CalendarMode
import com.prolificinteractive.materialcalendarview.format.ArrayWeekDayFormatter
import kotlinx.coroutines.*
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.coroutines.CoroutineContext
import kotlin.math.roundToInt

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

        initSwipeRefresh()
        initViews()
        initButtons()
        observeData()
        refreshViews()
    }

    override fun onResume() {
        super.onResume()

    }
    private fun refreshViews(){
        //현재 클릭된 날짜는 그대로, but 내용만 최신화됨
        val curDate = myDiaryViewModel.selectedDate.value
        //선택해 놓은 날짜가 있을 시
        if(curDate!=null && curDate!=""){
            try {
                val dateYM = curDate.substring(0,7)
                launch(coroutineContext) {
                    myDiaryViewModel.setResponseGetMonthDiary(dateYM)
                }
            }
            catch (e: Exception){
            }
        }
    }
    

    private fun observeData() {

        myDiaryViewModel.responseGetMonthDiary.observe(viewLifecycleOwner) {
            binding.loadingBar.isVisible = false
            if (it.isSuccessful) {
                try {
                    myDiaryViewModel.setMonthDiaries(it.body()!!.result)
                    //다른 달로 이동했을 때
                    if (myDiaryViewModel.selectedDate.value == null) {
                        checkSelectedDate(null)
                    } else {
                        val (y, m, d) = myDiaryViewModel.selectedDate.value!!.split('.')
                            .map { it.toInt() }
                        checkSelectedDate(CalendarDay.from(y, m - 1, d))
                    }
                }catch(e: Exception){
                    Log.e(TAG, "observeData: ${e.stackTrace}", )
                }
//                Toast.makeText(requireContext(), "로그인 성공", Toast.LENGTH_SHORT).show()
            } else {
//                it.errorBody()?.let{ errorBody->
//                    RetrofitClient.getErrorResponse(errorBody)?.let{
//                        CodaSnackBar.make(binding.root, it.message).show()
//                    }
//                }
                //todo refreshToken 만료시 재 로그인 멘트, errorbody에 jwt만료인 경우만 띄우는 걸로 해야함
//                Log.d(TAG, "observeData: ${it.errorBody()}")
//                CodaSnackBar.make(binding.root, "다시 로그인해 주세요.").show()
//                Toast.makeText(requireContext(), "한 달 일기 불러오기 실패", Toast.LENGTH_SHORT).show()
            }
        }
        myDiaryViewModel.monthDiaries.observe(viewLifecycleOwner){
            binding.materialCalendarView.removeDecorators()
            binding.materialCalendarView.addDecorators(
                AloneDotDecorator(requireContext(), myDiaryViewModel.aloneDiary.value!!),
                CommentDotDecorator(requireContext(), myDiaryViewModel.commentDiary.value!!),
                SelectedDateDecorator(requireContext())
            )
        }

        fragmentViewModel.fragmentState.observe(viewLifecycleOwner){ fragment->
            //현재 화면으로 온 경우!
            if(fragment=="myDiary"){
                refreshViews()
            }
        }
    }

    private fun initButtons() = with(binding) {
        //일기가 없는 경우 writeDiary
        writeDiaryLayout.setOnClickListener {
            fragmentViewModel.setBeforeFragment("myDiary")
            fragmentViewModel.setFragmentState("writeDiary")
        }
        //일기가 있는 경우
        diaryDetailLayout.setOnClickListener {
            //임시저장 상태면 writeDiary로, 이미 저장된 상태면 commentDiaryDetail
            //서버에 저장된 코멘트 일기인 경우
            if(myDiaryViewModel.selectedDiary.value!!.tempYN =='N' && myDiaryViewModel.selectedDiary.value!!.deliveryYN=='Y') {
                fragmentViewModel.setBeforeFragment("myDiary")
                fragmentViewModel.setFragmentState("commentDiaryDetail")
            }
            //임시저장(코멘트일기)인 경우, 혼자쓴 일기인 경우
            else{
                fragmentViewModel.setBeforeFragment("myDiary")
                fragmentViewModel.setFragmentState("writeDiary")
            }
        }
    }

    private fun initSwipeRefresh() = with(binding) {
        swipeRefreshLayout.setOnRefreshListener {
            val codaToday = DateConverter.ymdFormat(DateConverter.getCodaToday())
            //1안 화면 현재 월, 날짜 그대로, 데이터만 갱신하여 줌
            //2안 화면 현재 월 그대로, 날짜 없애기
//            myDiaryViewModel.setSelectedDate(null)

            val (y, m, d) = codaToday.split('.').map { it.toInt() }
            materialCalendarView.currentDate = CalendarDay.from(y,m-1,d)
            myDiaryViewModel.setSelectedDate(codaToday)
            checkSelectedDate(CalendarDay.from(y, m - 1, d))
//            checkSelectedDate(null)
            //3안 화면 오늘 날짜로 초기화, 달력 움직여야함..
//            refreshViews()
            //위의 동작 완료시 리프레쉬 종료
            swipeRefreshLayout.isRefreshing = false
        }

        //스크롤이 최상단에 위치했을때만 스와이프 레이아웃의 리프레쉬가 트리거되기 위함
//        diaryContentsTextView.viewTreeObserver.addOnScrollChangedListener {
//            swipeRefreshLayout.isEnabled = diaryContentsTextView.scrollY==0
//        }

    }

    private fun initViews() = with(binding){

        diaryContentsTextView.movementMethod = ScrollingMovementMethod()

        initCalendar()
    }

    @SuppressLint("SetTextI18n", "ResourceType")
    private fun initCalendar() = with(binding) {

        //휴대폰 디스클레이 사이즈 구하기
        var display: Display
        val size = Point()
        Log.d(TAG, "initCalendar: width : ${size.x} ${size.y}")
//        Toast.makeText(requireContext(), "width: ${size.x} height : ${size.y}", Toast.LENGTH_LONG).show()
        if(size.x<=1080){
            val density :Float = calendarLine1.resources.displayMetrics.density

            materialCalendarView.apply {
                setTileSizeDp(40)
                setPadding(0,0,0,30.toPx())
            }
            val layoutParams1: ConstraintLayout.LayoutParams = calendarLine1.layoutParams as ConstraintLayout.LayoutParams
            val layoutParams2: ConstraintLayout.LayoutParams = calendarLine2.layoutParams as ConstraintLayout.LayoutParams
            layoutParams1.setMargins((calendarLine1.marginEnd.toDp() * density).roundToInt(),((calendarLine1.marginTop.toDp()-2)*density).roundToInt(),(calendarLine1.marginEnd.toDp() * density).roundToInt(),0)
            layoutParams2.setMargins((calendarLine1.marginEnd.toDp() * density).roundToInt(),((calendarLine2.marginTop.toDp()-4)*density).roundToInt(),(calendarLine1.marginEnd.toDp() * density).roundToInt(),0)
//            Log.d(TAG, "initCalendar: ${calendarLine1.marginTop} ${calendarLine1.marginTop.toDp()}")
//            Log.d(TAG, "initCalendar: ${calendarLine2.marginTop} ${calendarLine2.marginTop.toDp()}")
//            Toast.makeText(requireContext(), "initCalendar: ${calendarLine1.marginTop} ${calendarLine1.marginTop.toDp()}\n initCalendar: ${calendarLine2.marginTop} ${calendarLine2.marginTop.toDp()}", Toast.LENGTH_SHORT).show()
//            val size : Int=  (30 * density).roundToInt()
//            float scale = view.getResources().getDisplayMetrics().density;

//            Log.d(TAG, "initCalendar: ${calendarLine1.marginEnd} ${}")
        }
//        val width = size.x
//        val height = size.y

        //nexus 720 1280
        //pixel2 1080 1920
        //갤럭시s21 1080 2400
        //note9 1440 2960
        //리스너, 날짜 바뀌었을 시
        materialCalendarView.setOnDateChangedListener { widget, date, selected ->
            checkSelectedDate(date)
        }

        //리스너, 월 바뀌었을 시
        materialCalendarView.setOnMonthChangedListener { widget, date ->
            binding.calendarHeaderTextView.text = "${date.year}년 ${date.month+1}월"
            //선택된 일기 없애주기
            myDiaryViewModel.setSelectedDiary(Diary(null,"","","",' ',' ', null))
            val requestDate = LocalDate.of(date.year, date.month+1, date.day)
                .format(DateTimeFormatter.ofPattern("yyyy.MM"))
            setMonthCalendarDiaries(requestDate)
        }

        materialCalendarView.state().edit()
            .setFirstDayOfWeek(Calendar.SUNDAY)
            .setMinimumDate(CalendarDay.from(2021, 0, 1))//캘린더 시작 날짜
            .setMaximumDate(CalendarDay.from(2022, 11, 31))//캘린더 끝 날짜
            .setCalendarDisplayMode(CalendarMode.MONTHS) // 월 달력, 주 달력
            .commit()
        materialCalendarView.setWeekDayFormatter(ArrayWeekDayFormatter(resources.getStringArray(R.array.custom_weekdays)))
//        materialCalendarView.setTitleFormatter(MonthArrayTitleFormatter(resources.getStringArray(R.array.custom_months)))
//        materialCalendarView.setHeaderTextAppearance(R.style.CalendarViewHeaderCustomText);
//        materialCalendarView.setWeekDayTextAppearance(R.drawable.background_ivory_radius_15_border_brown_1)
//        materialCalendarView.setHeaderTextAppearance(R.drawable.background_ivory_radius_15_border_brown_1)
        //topbar(년월)
//        materialCalendarView.setTitleFormatter { day -> // CalendarDay라는 클래스는 LocalDate 클래스를 기반으로 만들어진 클래스다
//            // 월, 요일을 한글로 보이게 설정 (MonthArrayTitleFormatter의 작동을 확인하려면 밑의 setTitleFormatter()를 지운다)
//            val calendarHeaderBuilder = StringBuilder()
//            calendarHeaderBuilder.append(day.year)
//                .append("년 ")
//                .append(day.month + 1)
//                .append("월")
//            calendarHeaderBuilder.toString()
//        }
//        materialCalendarView.setWeekDayTextAppearance()
        materialCalendarView.addDecorator(
            SelectedDateDecorator(requireContext())
        )
        materialCalendarView.isDynamicHeightEnabled = true

        materialCalendarView.topbarVisible=false
        leftArrowButton.setOnClickListener {
            val beforeDate = materialCalendarView.currentDate
            materialCalendarView.currentDate = CalendarDay.from(beforeDate.year,beforeDate.month-1, beforeDate.day)
        }

        rightArrowButton.setOnClickListener {
            val beforeDate = materialCalendarView.currentDate
            materialCalendarView.currentDate = CalendarDay.from(beforeDate.year,beforeDate.month+1, beforeDate.day)
        }

    }


    private fun setMonthCalendarDiaries(yearMonth: String) {
        //처음 진입했을 때는 오늘로 설정
        if (myDiaryViewModel.selectedDate.value == "") {
            myDiaryViewModel.setSelectedDate(DateConverter.ymdFormat(DateConverter.getCodaToday()))
        }
        //월 변경 시 포커스 x
        else{
            myDiaryViewModel.setSelectedDate(null)
        }
//        binding.materialCalendarView.selectedDate = null
        //월 변경 시 해당 월의 일기 셋팅

        launch(coroutineContext) {
            binding.loadingBar.isVisible = true
            withContext(Dispatchers.IO) {
                myDiaryViewModel.setResponseGetMonthDiary(yearMonth)
            }
        }
    }


//    private fun checkSelectedDate(date: CalendarDay?) = with(binding){
//        //        Log.d(TAG, "checkSelectedDate: date ${date}")
//        //calendar date는 month가 0부터 시작하기 때문에 이를 다시 localDate로 바꿀 땐 +1
//        if(date!=null) {
//
//            var selectedDate = LocalDate.of(date.year, date.month + 1, date.day)
//            val dateToString = selectedDate.toString().replace('-', '.')
//            var nextDate = LocalDate.of(date.year, date.month + 1, date.day)
//            Log.d(TAG, "observeDatas: detail 0 ${myDiaryViewModel.selectedDate.value}: ")
//            nextDate = nextDate.plusDays(1)
//
//            val nextDateToString = nextDate.toString().replace('-', '.')
//
//            myDiaryViewModel.setSelectedDate(dateToString)
//
////        Log.d(TAG, "checkSelectedDate: 여기  내일 $nextDateToString")
////        Log.d(TAG, "checkSelectedDate: 여기 오늘 ${dateToString}")
//            //오늘 내가 코멘트를 받은 경우 어제 일기를 선택했을 때 오늘 내가 코멘트를 쓴 상태인지 확인 -> Day+1
//            launch(Dispatchers.IO) {
//                myDiaryViewModel.setResponseGetDayComment(nextDateToString)
//            }
//        }
//    }


    @SuppressLint("ResourceAsColor")
    private fun checkSelectedDate(date: CalendarDay?) = with(binding) {
//        materialCalendarView.currentDate = date
            materialCalendarView.selectedDate = date
        //달 이동한 경우 포커스 해제
        if (date == null) {
            readDiaryLayout.isVisible = false
            writeDiaryWrapLayout.isVisible = false
            noCommentTextView.isVisible = false
            return
        }
        //아래 부분은 날짜를 캘린더에서 선택한 경우

        //calendar date는 month가 0부터 시작하기 때문에 이를 다시 localDate로 바꿀 땐 +1
        var selectedDate =LocalDate.of(date.year, date.month+1, date.day)
        val dateToString = selectedDate.toString().replace('-','.')
        var nextDate = LocalDate.of(date.year, date.month+1, date.day)
        nextDate = nextDate.plusDays(1)

        val nextDateToString = nextDate.toString().replace('-','.')


        myDiaryViewModel.setSelectedDate(dateToString)


//        Log.d(TAG, "checkSelectedDate: 여기  내일 $nextDateToString")
//        Log.d(TAG, "checkSelectedDate: 여기 오늘 ${dateToString}")
        //오늘 내가 코멘트를 받은 경우 어제 일기를 선택했을 때 오늘 내가 코멘트를 쓴 상태인지 확인 -> Day+1
        launch (Dispatchers.IO) {
            myDiaryViewModel.setResponseGetDayComment(nextDateToString)
        }

        val codaToday = DateConverter.getCodaToday()
//        Log.d(TAG, "checkSelectedDate: 머야 $codaToday $selectedDate")

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

                        if (diary.commentList == null || diary.commentList.size == 0) {
                            //코멘트가 도착하지 않았는데 이틀 지난 경우
                            if(DateConverter.ymdToDate(diary.date)<=codaToday.minusDays(2)){
                                sendDiaryBeforeAfterTextView.isVisible = false
                                noCommentTextView.isVisible = diary.tempYN=='N'
                            }
                            //아직 코멘트 기다리는 경우
                            else {
                                //임시저장인 경우
                                noCommentTextView.isVisible = false
                                if(diary.tempYN=='Y'){
                                    sendDiaryBeforeAfterTextView.setTextColor(ContextCompat.getColor(requireContext(),R.color.text_black))
                                    sendDiaryBeforeAfterTextView.text = getString(R.string.upload_temp_comment_please)
                                    sendDiaryBeforeAfterTextView.setBackgroundResource(R.drawable.background_brand_orange_radius_bottom_10)
                                }
                                else{
                                    sendDiaryBeforeAfterTextView.setTextColor(ContextCompat.getColor(requireContext(),R.color.text_brown))
                                    sendDiaryBeforeAfterTextView.text = getString(R.string.calendar_with_diary_comment_soon)
                                    sendDiaryBeforeAfterTextView.setBackgroundResource(R.drawable.background_light_brown_radius_bottom_10)
                                }
                            }
                        }
                        //코멘트 있는 경우
                        else {
                            val codaToday = DateConverter.getCodaToday()
                            //이틀이 지났는데 코멘트 작성 안 한  경우만 안 보이게
                            binding.sendDiaryBeforeAfterTextView.isVisible =true
//                                DateConverter.ymdToDate(diary.date) > codaToday.minusDays(2)

//                            if(DateConverter.ymdToDate(diary.date)<=codaToday.minusDays(2) && !myDiaryViewModel.haveDayMyComment.value!!){
//                                binding.sendDiaryBeforeAfterTextView.isVisible = false
//                            }
//                            else{
//                                binding.sendDiaryBeforeAfterTextView.isVisible = true
//                            }
                            sendDiaryBeforeAfterTextView.text = getString(R.string.arrived_comment)
                            sendDiaryBeforeAfterTextView.setBackgroundResource(R.drawable.background_pure_green_radius_bottom_10)
                            sendDiaryBeforeAfterTextView.setTextColor(ContextCompat.getColor(requireContext(),R.color.background_ivory))
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
        writeDiaryWrapLayout.isVisible = true
        writeDiaryLayout.isVisible = true
        lineDecorationTextView.text = getString(R.string.mydiary_text_decoration)
        readDiaryLayout.isVisible = false
        noCommentTextView.isVisible = false
    }

}