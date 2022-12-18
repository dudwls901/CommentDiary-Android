package com.movingmaker.presentation.view.main.mydiary

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.core.view.marginEnd
import androidx.core.view.marginTop
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.movingmaker.presentation.R
import com.movingmaker.presentation.base.BaseFragment
import com.movingmaker.presentation.databinding.FragmentMydiaryWithCalendarBinding
import com.movingmaker.presentation.util.Extension.toDp
import com.movingmaker.presentation.util.Extension.toPx
import com.movingmaker.presentation.util.FRAGMENT_NAME
import com.movingmaker.presentation.util.calenderDayToLocalDate
import com.movingmaker.presentation.util.getCodaToday
import com.movingmaker.presentation.util.toCalenderDay
import com.movingmaker.presentation.util.ymFormatForLocalDate
import com.movingmaker.presentation.util.ymdFormat
import com.movingmaker.presentation.util.ymdToCalendarDay
import com.movingmaker.presentation.util.ymdToDate
import com.movingmaker.presentation.view.main.mydiary.calendardecorator.AloneDotDecorator
import com.movingmaker.presentation.view.main.mydiary.calendardecorator.CommentDotDecorator
import com.movingmaker.presentation.view.main.mydiary.calendardecorator.TodayDotDecorator
import com.movingmaker.presentation.viewmodel.FragmentViewModel
import com.movingmaker.presentation.viewmodel.mydiary.MyDiaryViewModel
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.CalendarMode
import com.prolificinteractive.materialcalendarview.format.ArrayWeekDayFormatter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.Calendar
import kotlin.math.roundToInt

@AndroidEntryPoint
class CalendarWithDiaryFragment :
    BaseFragment<FragmentMydiaryWithCalendarBinding>(R.layout.fragment_mydiary_with_calendar) {

    private val myDiaryViewModel: MyDiaryViewModel by activityViewModels()
    private val fragmentViewModel: FragmentViewModel by activityViewModels()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.vm = myDiaryViewModel
        fragmentViewModel.setCurrentFragment(FRAGMENT_NAME.CALENDAR_WITH_DIARY)

        initViews()
        observeData()
    }

    private fun refreshViews() {
        //현재 클릭된 날짜는 그대로, but 내용만 최신화됨
        ymFormatForLocalDate(myDiaryViewModel.selectedDate.value)?.let {
            lifecycleScope.launch {
                myDiaryViewModel.getMonthDiary(it)
            }
        }
    }


    private fun observeData() {

        myDiaryViewModel.pushDate.observe(viewLifecycleOwner) {
            Timber.d("observeData: push ${myDiaryViewModel.pushDate.value}")
            val date = it
            myDiaryViewModel.setSelectedDate(date)
            refreshViews()
            findNavController().navigate(CalendarWithDiaryFragmentDirections.actionCalendarWithDiaryFragmentToCommentDiaryDetailFragment())
        }

        myDiaryViewModel.selectedYearMonth.observe(viewLifecycleOwner) { ym ->
            ym?.let {
                myDiaryViewModel.getMonthDiary(it)
            }
        }

        myDiaryViewModel.monthDiaries.observe(viewLifecycleOwner) {
            myDiaryViewModel.selectedDate.value?.let { selectedDate ->
                ymdToCalendarDay(selectedDate)?.let { calendarDay ->
                    //for refresh?
                    checkSelectedDate(calendarDay)
                }
            }
            with(binding.materialCalendarView) {
                currentDate = toCalenderDay(myDiaryViewModel.selectedYearMonth.value)
                removeDecorators()
                addDecorators(
                    TodayDotDecorator(),
                    AloneDotDecorator(
                        requireContext(),
                        myDiaryViewModel.aloneDiary.value!!
                    ),
                    CommentDotDecorator(
                        requireContext(),
                        myDiaryViewModel.commentDiary.value!!
                    ),
                )
            }
        }


        myDiaryViewModel.selectedDate.observe(viewLifecycleOwner) { date ->
            when (date) {
                null -> {
                    checkSelectedDate(null)
                }
                else -> {
                    binding.materialCalendarView.selectedDate = toCalenderDay(date)
                    val (y, m, d) = date.split('.').map { it.toInt() }
                    checkSelectedDate(CalendarDay.from(y, m - 1, d))
                }
            }
        }

    }

    private fun initViews() {
        initSwipeRefresh()
        initButtons()
        initCalendar()
    }

    private fun initButtons() = with(binding) {
        //일기가 없는 경우 writeDiary
        writeDiaryLayout.setOnClickListener {
            val action =
                CalendarWithDiaryFragmentDirections.actionCalendarWithDiaryFragmentToWriteDiaryFragment()
            findNavController().navigate(action)
        }
        readDiaryLayout.setOnClickListener { //일기가 있는 경우
            val action = if (myDiaryViewModel.selectedDiary.value?.deliveryYN == 'Y') {
                CalendarWithDiaryFragmentDirections.actionCalendarWithDiaryFragmentToCommentDiaryDetailFragment()
            } else { //혼자쓴 일기인 경우
                CalendarWithDiaryFragmentDirections.actionCalendarWithDiaryFragmentToAloneDiaryDetailFragment()
            }
            findNavController().navigate(action)
        }
    }

    private fun initSwipeRefresh() = with(binding) {
        swipeRefreshLayout.setOnRefreshListener {
            val codaToday = ymdFormat(getCodaToday())
            //오늘 날짜로 이동
            val (y, m, d) = codaToday!!.split('.').map { it.toInt() }
            materialCalendarView.currentDate = CalendarDay.from(y, m - 1, d)
            myDiaryViewModel.setSelectedDate(codaToday)
            myDiaryViewModel.setSelectedYearMonth("$y.$m")

            swipeRefreshLayout.isRefreshing = false
        }

        //스크롤이 최상단에 위치했을때만 스와이프 레이아웃의 리프레쉬가 트리거되기 위함
//        diaryContentsTextView.viewTreeObserver.addOnScrollChangedListener {
//            swipeRefreshLayout.isEnabled = diaryContentsTextView.scrollY==0
//        }

    }

    private fun initCalendar() {
        adjustCalendarSize()
        setCalendarConfig()
        setMonthMoveListener()
        setDateMoveListener()
    }

    private fun setDateMoveListener() {
        binding.materialCalendarView.setOnDateChangedListener { _, date, _ ->
            myDiaryViewModel.setSelectedDate(ymdFormat(date))
        }
    }

    private fun setMonthMoveListener() = with(binding) {
        materialCalendarView.setOnMonthChangedListener { widget, date ->
            if (ymFormatForLocalDate(date) == myDiaryViewModel.selectedYearMonth.value) {
                return@setOnMonthChangedListener
            }
            myDiaryViewModel.setSelectedYearMonth(ymFormatForLocalDate(date))
            myDiaryViewModel.setSelectedDate(null)
        }

        leftArrowButton.setOnClickListener {
            val beforeDate = materialCalendarView.currentDate
            val nextDate = CalendarDay.from(beforeDate.year, beforeDate.month - 1, beforeDate.day)
            materialCalendarView.currentDate = nextDate
            myDiaryViewModel.setSelectedDate(null)
        }

        rightArrowButton.setOnClickListener {
            val beforeDate = materialCalendarView.currentDate
            val nextDate = CalendarDay.from(beforeDate.year, beforeDate.month + 1, beforeDate.day)
            materialCalendarView.currentDate = nextDate
            myDiaryViewModel.setSelectedDate(null)
        }
    }

    private fun setCalendarConfig() = with(binding.materialCalendarView) {
        state().edit()
            .setFirstDayOfWeek(Calendar.SUNDAY)
            .setMinimumDate(CalendarDay.from(2021, 0, 1))//캘린더 시작 날짜
            .setMaximumDate(CalendarDay.from(2099, 11, 31))//캘린더 끝 날짜
            .setCalendarDisplayMode(CalendarMode.MONTHS) // 월 달력, 주 달력
            .commit()
        setWeekDayFormatter(ArrayWeekDayFormatter(resources.getStringArray(R.array.custom_weekdays)))
        isDynamicHeightEnabled = true
        topbarVisible = false
    }

    @SuppressLint("ResourceAsColor")
    fun checkSelectedDate(date: CalendarDay?) = with(binding) {
        //달 이동한 경우 포커스 해제
        if (date == null) {
            materialCalendarView.selectedDate = null
            readDiaryLayout.isVisible = false
            writeDiaryWrapLayout.isVisible = false
            noCommentTextView.isVisible = false
        } else {
            changeDiaryViewState(date)
        }
    }

    //휴대폰 디스클레이 사이즈 구하기
    private fun getDisplaySize(): Pair<Int, Int> {
        val outMetrics = DisplayMetrics()
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
            val display = requireActivity().display
            display?.getRealMetrics(outMetrics)
        } else {
            @Suppress("DEPRECATION")
            val display = requireActivity().windowManager.defaultDisplay
            @Suppress("DEPRECATION")
            display.getMetrics(outMetrics)
        }
        return Pair(outMetrics.widthPixels, outMetrics.heightPixels)
    }

    private fun adjustCalendarSize() = with(binding) {

        val widthPixel = getDisplaySize().first

        //1080 이하인 기기 캘린더 사이즈 조정
        //nexus 720 1280
        //pixel2 1080 1920
        //갤럭시s21 1080 2400
        //note9 1440 2960
        if (widthPixel <= 1080) {
            val density: Float = calendarLine1.resources.displayMetrics.density

            materialCalendarView.apply {
                setTileSizeDp(40)
                setPadding(0, 0, 0, 30.toPx())
            }
            val layoutParams1: ConstraintLayout.LayoutParams =
                calendarLine1.layoutParams as ConstraintLayout.LayoutParams
            val layoutParams2: ConstraintLayout.LayoutParams =
                calendarLine2.layoutParams as ConstraintLayout.LayoutParams
            layoutParams1.setMargins(
                (calendarLine1.marginEnd.toDp() * density).roundToInt(),
                ((calendarLine1.marginTop.toDp() - 2) * density).roundToInt(),
                (calendarLine1.marginEnd.toDp() * density).roundToInt(),
                0
            )
            layoutParams2.setMargins(
                (calendarLine1.marginEnd.toDp() * density).roundToInt(),
                ((calendarLine2.marginTop.toDp() - 4) * density).roundToInt(),
                (calendarLine1.marginEnd.toDp() * density).roundToInt(),
                0
            )
        }
    }

    private fun changeDiaryViewState(date: CalendarDay) = with(binding) {

        //아래 부분은 날짜를 캘린더에서 선택한 경우
        //calendar date는 month가 0부터 시작하기 때문에 이를 다시 localDate로 바꿀 땐 +1
        val selectedDate = calenderDayToLocalDate(date)
        val nextDate = selectedDate.plusDays(1)

        //오늘 내가 코멘트를 받은 경우 어제 일기를 선택했을 때 오늘 내가 코멘트를 쓴 상태인지 확인 -> Day+1
        lifecycleScope.launch {
            ymdFormat(nextDate)?.let { date ->
                myDiaryViewModel.setResponseGetDayComment(date)
            }
        }

        val codaToday = getCodaToday()
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
            "${String.format("%02d", date.month + 1)}월 ${String.format("%02d", date.day)}일 나의 일기"
        )

        //이전 날짜면 검사
        if (myDiaryViewModel.monthDiaries.value?.isNotEmpty() == true) {
            for (diary in myDiaryViewModel.monthDiaries.value!!) {
                //작성한 일기가 있다면
                if (diary.date == selectedDate.toString().replace('-', '.')) {
                    //viewmodel로 넘기기
                    myDiaryViewModel.setSelectedDiary(diary)

                    //혼자 일기인 경우
                    if (diary.deliveryYN == 'N') {
                        sendDiaryBeforeAfterTextView.isVisible = false
                        noCommentTextView.isVisible = false
                    }
                    //코멘트 일기인 경우
                    else {
                        sendDiaryBeforeAfterTextView.isVisible = true
                        //혼자쓰는일기면 가리고 코멘트 일기면 보이기
                        //코멘트가 아직 도착하지 않은 경우 or 삭제된 경우 or서버에서 빈 값 내려올 땐 null?

                        if (diary.commentList == null || diary.commentList!!.size == 0) {
                            //코멘트가 도착하지 않았는데 이틀 지난 경우
                            val curDiaryDate = ymdToDate(diary.date)
                            if (curDiaryDate != null && curDiaryDate <= codaToday.minusDays(2)) {
                                sendDiaryBeforeAfterTextView.isVisible = false
                                noCommentTextView.isVisible = diary.tempYN == 'N'
                            }
                            //아직 코멘트 기다리는 경우
                            else {
                                //임시저장인 경우
                                noCommentTextView.isVisible = false
                                if (diary.tempYN == 'Y') {
                                    sendDiaryBeforeAfterTextView.setTextColor(
                                        ContextCompat.getColor(
                                            requireContext(),
                                            R.color.text_black
                                        )
                                    )
                                    sendDiaryBeforeAfterTextView.text =
                                        getString(R.string.upload_temp_comment_please)
                                    sendDiaryBeforeAfterTextView.setBackgroundResource(R.drawable.background_brand_orange_radius_bottom_10)
                                } else {
                                    sendDiaryBeforeAfterTextView.setTextColor(
                                        ContextCompat.getColor(
                                            requireContext(),
                                            R.color.text_brown
                                        )
                                    )
                                    sendDiaryBeforeAfterTextView.text =
                                        getString(R.string.calendar_with_diary_comment_soon)
                                    sendDiaryBeforeAfterTextView.setBackgroundResource(R.drawable.background_light_brown_radius_bottom_10)
                                }
                            }
                        }
                        //코멘트 있는 경우
                        else {
                            //이틀이 지났는데 코멘트 작성 안 한  경우만 안 보이게
                            binding.sendDiaryBeforeAfterTextView.isVisible = true
                            sendDiaryBeforeAfterTextView.text = getString(R.string.arrived_comment)
                            sendDiaryBeforeAfterTextView.setBackgroundResource(R.drawable.background_pure_green_radius_bottom_10)
                            sendDiaryBeforeAfterTextView.setTextColor(
                                ContextCompat.getColor(
                                    requireContext(),
                                    R.color.background_ivory
                                )
                            )
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
        myDiaryViewModel.setSelectedDiary(null)
        writeDiaryWrapLayout.isVisible = true
        writeDiaryLayout.isVisible = true
        lineDecorationTextView.text = getString(R.string.mydiary_text_decoration)
        readDiaryLayout.isVisible = false
        noCommentTextView.isVisible = false
    }

}