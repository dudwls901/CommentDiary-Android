package com.movingmaker.presentation.view.main.mydiary

import android.os.Bundle
import android.util.DisplayMetrics
import android.view.View
import android.view.WindowManager
import android.view.WindowMetrics
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.core.view.marginEnd
import androidx.core.view.marginTop
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.movingmaker.domain.model.response.Diary
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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.time.LocalDate
import java.util.Calendar
import kotlin.math.roundToInt

//todo 삭제 이후 화면 동기화, 일기 상세 후 뒤돌아올 때 화면 유지
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


    private fun observeData() {

        myDiaryViewModel.localDiaries.observe(viewLifecycleOwner) {
            //개수가 0인 경우는 캐싱할 때 clear한 경우
            if (it.isNotEmpty()) {
                myDiaryViewModel.setMonthDiaries(it)
            }
        }
        /**
         * 코멘트 받아서 푸시로 들어온 경우
         * 1. selectedDate 해당 일기 날짜로 설정
         * 2. 달력 이동 로직 (getMonthDiary -> 해당 일기 check -> selectedDiary 해당 일기로 설정)
         * 3. 해당 일기 detail 화면으로 이동
         * */
        myDiaryViewModel.pushDate.observe(viewLifecycleOwner) {
            viewLifecycleOwner.lifecycleScope.launch {
                Timber.d("observeData: push ${myDiaryViewModel.pushDate.value}")
                val date = it
                myDiaryViewModel.setSelectedDate(date)
                refreshViews(date)
                findNavController().navigate(CalendarWithDiaryFragmentDirections.actionCalendarWithDiaryFragmentToCommentDiaryDetailFragment())
            }
        }

        //monthDiary 받아온 후 클릭한 날짜 하단 일기뷰 상태 처리
        myDiaryViewModel.monthDiaries.observe(viewLifecycleOwner) {
            with(binding.materialCalendarView) {
                removeDecorators()
                addDecorators(
                    TodayDotDecorator(),
                    AloneDotDecorator(
                        requireContext(),
                        myDiaryViewModel.aloneDiaryDates.value!!
                    ),
                    CommentDotDecorator(
                        requireContext(),
                        myDiaryViewModel.commentDiaryDates.value!!
                    ),
                )
            }
            when (val selectedDate = myDiaryViewModel.selectedDate.value) {
                null -> {
                    checkSelectedDate(null)
                }
                else -> {
                    binding.materialCalendarView.selectedDate = toCalenderDay(selectedDate)
                    checkSelectedDate(ymdToDate(selectedDate))
                }
            }
        }
        myDiaryViewModel.selectedDiary.observe(viewLifecycleOwner) {
            Timber.e("여기 $it")
        }
    }

    private fun initViews() {
        initSwipeRefresh()
        initButtons()
        initCalendar()
    }

    private fun refreshViews(date: String) {
        //다른 달로 이동한 경우
        // -> monthChangeListener (currentDate == toCalenderDay(date)인 경우 트리거 x)
        binding.materialCalendarView.currentDate = toCalenderDay(date)
        ymFormatForLocalDate(myDiaryViewModel.selectedDate.value)?.let { ymDate ->
            //이번 달 화면에서 refresh 하는 경우 달력 이동x, Month Diary 갱신, 일기 상태 변경
            myDiaryViewModel.getRemoteCommentDiaries(ymDate)
        }
    }

    private fun initButtons() = with(binding) {
        //일기가 없는 경우 writeDiary
        writeDiaryLayout.setOnClickListener {
            val action =
                CalendarWithDiaryFragmentDirections.actionCalendarWithDiaryFragmentToWriteDiaryFragment()
            findNavController().navigate(action)
        }
        //todo 임시저장 일기 화면 대응
        readDiaryLayout.setOnClickListener { //일기가 있는 경우
            //임시 저장 아닌 코멘트 일기인 경우
            val action =
                if (myDiaryViewModel.selectedDiary.value?.deliveryYN == 'Y' && myDiaryViewModel.selectedDiary.value?.userId == -1L) {
                    CalendarWithDiaryFragmentDirections.actionCalendarWithDiaryFragmentToCommentDiaryDetailFragment()
                } else { //혼자쓴 일기인 경우
                    CalendarWithDiaryFragmentDirections.actionCalendarWithDiaryFragmentToWriteDiaryFragment()
                }
            findNavController().navigate(action)
        }
    }

    private fun initSwipeRefresh() = with(binding) {
        swipeRefreshLayout.setOnRefreshListener {
            val codaToday = ymdFormat(getCodaToday())!!
            //달력 이동 후 selectedDate 오늘로 설정
            //selectedDate = ? -> null -> codaToday
            refreshViews(codaToday)
            myDiaryViewModel.setSelectedDate(codaToday)
            swipeRefreshLayout.isRefreshing = false
        }
    }

    private fun initCalendar() {
        adjustCalendarSize()
        setCalendarConfig()
        setMonthMoveListener()
        setDateMoveListener()
    }

    private fun setDateMoveListener() {
        //selectDate 직접 변경시 트리거 x date 클릭시에만 트리거 o
        binding.materialCalendarView.setOnDateChangedListener { _, date, _ ->
            myDiaryViewModel.setSelectedDate(ymdFormat(date))
            checkSelectedDate(calenderDayToLocalDate(date))
        }
    }

    private fun setMonthMoveListener() = with(binding) {
        /*
        * 달력 이동 최종 트리거, 현 달력에 따른 전체 뷰 상태 변환
        * 1. 외부에서 달력 이둥
        * 2. 화면 재진입 포커싱 유지 / 달력 이동 포커싱 해제
        * 3. Month Diary 가져오기
        * 4. Month Diary 가져온 후 선택한 날짜에 대한 하단 일기 프리뷰 상태 변경
        * */
        materialCalendarView.setOnMonthChangedListener { _, date ->
            //화면 재진입시 포커싱 해제 대응
            ymFormatForLocalDate(date)?.let { ymDate ->
                if (ymDate == myDiaryViewModel.selectedYearMonth.value) {
                    return@setOnMonthChangedListener
                }
                myDiaryViewModel.setSelectedDate(null)
                myDiaryViewModel.setSelectedYearMonth(ymFormatForLocalDate(date))
                viewLifecycleOwner.lifecycleScope.launch {
                    myDiaryViewModel.getRemoteCommentDiaries(ymDate)
                }
            }
        }

        // -> monthChangeListener
        leftArrowButton.setOnClickListener {
            val beforeDate = materialCalendarView.currentDate
            val nextDate = CalendarDay.from(beforeDate.year, beforeDate.month - 1, beforeDate.day)
            materialCalendarView.currentDate = nextDate
        }

        // -> monthChangeListener
        rightArrowButton.setOnClickListener {
            val beforeDate = materialCalendarView.currentDate
            val nextDate = CalendarDay.from(beforeDate.year, beforeDate.month + 1, beforeDate.day)
            materialCalendarView.currentDate = nextDate
        }
    }

    private fun checkSelectedDate(date: LocalDate?) = with(binding) {
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

    private fun changeDiaryViewState(selectedDate: LocalDate) = with(binding) {
        //미래 날짜면 일기 작성 불가, 조회 불가
        if (selectedDate > getCodaToday()) {
            setFutureDiaryDate()
        } else {
            //이전 날짜면 검사
            viewLifecycleOwner.lifecycleScope.launch {
                ymdFormat(selectedDate)?.let { selectedDateYMD ->
                    when (val diary = getDiaryInMonth(selectedDateYMD)) {
                        null -> {
                            //해당 날짜에 일기 없는 경우
                            setEmptyDiaryDate()
                        }
                        else -> {
                            //해당 날짜에 일기 있는 경우
                            setExistDiaryDate(diary)
                        }
                    }
                }
            }
        }
    }

    private fun setFutureDiaryDate() = with(binding) {
        writeDiaryWrapLayout.isVisible = true
        writeDiaryLayout.isVisible = false
        lineDecorationTextView.text = getString(R.string.write_diary_yet)
        readDiaryLayout.isVisible = false
        noCommentTextView.isVisible = false
    }

    private fun setEmptyDiaryDate() = with(binding) {
        myDiaryViewModel.setSelectedDiary(null)
        writeDiaryWrapLayout.isVisible = true
        writeDiaryLayout.isVisible = true
        lineDecorationTextView.text = getString(R.string.mydiary_text_decoration)
        readDiaryLayout.isVisible = false
        noCommentTextView.isVisible = false
    }

    private fun setExistDiaryDate(diary: Diary) = with(binding) {
        //일기 있는 경우 selectedDiary 설정
        myDiaryViewModel.setSelectedDiary(diary)
        readDiaryLayout.isVisible = true
        writeDiaryWrapLayout.isVisible = false
        //해당 날짜에 일기 있는 경우
        when (diary.deliveryYN) {
            'Y' -> {
                setCommentDiaryDate(diary)
            }
            else -> {
                setAloneDiaryDate()
            }
        }
    }

    private fun setAloneDiaryDate() = with(binding) {
        beforeCommentTextView.isVisible = false
        noCommentTextView.isVisible = false
    }

    private fun setCommentDiaryDate(diary: Diary) = with(binding) {
        ymdToDate(diary.date)?.let { diaryDate ->
            beforeCommentTextView.isVisible = true

            val nextDate = diaryDate.plusDays(1)
            //오늘 내가 코멘트를 받은 경우 어제 일기를 선택했을 때 오늘 내가 코멘트를 쓴 상태인지 확인 -> Day+1
            viewLifecycleOwner.lifecycleScope.launch {
                ymdFormat(nextDate)?.let { date ->
                    myDiaryViewModel.getDayWrittenComment(date)
                }
            }
            if (diary.commentList.isEmpty()) {
                setEmptyComment(diaryDate)
            } else {
                setExistComment()
            }
        }
    }

    private fun setExistComment() = with(binding) {
        beforeCommentTextView.isVisible = true
        beforeCommentTextView.text = getString(R.string.arrived_comment)
        beforeCommentTextView.setBackgroundResource(R.drawable.background_pure_green_radius_bottom_10)
        beforeCommentTextView.setTextColor(
            requireContext().getColor(
                R.color.background_ivory
            )
        )
        noCommentTextView.isVisible = false
    }

    private fun setEmptyComment(diaryDate: LocalDate) = with(binding) {
        //코멘트가 도착하지 않았는데 이틀 지난 경우 : 더 이상 코멘트를 받을 수 없는 상태
        if (diaryDate <= getCodaToday().minusDays(2)) {
            beforeCommentTextView.isVisible = false
            noCommentTextView.isVisible = true
        } else { //아직 코멘트 기다리는 경우 : 아직 코멘트를 받을 수 있는 상태
            noCommentTextView.isVisible = false
            beforeCommentTextView.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.text_brown
                )
            )
            beforeCommentTextView.text =
                getString(R.string.calendar_with_diary_comment_soon)
            beforeCommentTextView.setBackgroundResource(R.drawable.background_light_brown_radius_bottom_10)
        }

    }

    private suspend fun getDiaryInMonth(selectedDateYMD: String): Diary? =
        withContext(Dispatchers.Default) {
            myDiaryViewModel.monthDiaries.value?.let { monthDiaries ->
                monthDiaries.find { it.date == selectedDateYMD }
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


    //휴대폰 디스클레이 사이즈 구하기
    private fun getDisplaySize(): Pair<Int, Int> {
        return if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
            val metrics: WindowMetrics =
                requireContext().getSystemService(WindowManager::class.java).currentWindowMetrics
            Pair(metrics.bounds.width(), metrics.bounds.height())
        } else {
            val outMetrics = DisplayMetrics()

            @Suppress("DEPRECATION")
            val display = requireActivity().windowManager.defaultDisplay
            @Suppress("DEPRECATION")
            display.getMetrics(outMetrics)
            Pair(outMetrics.widthPixels, outMetrics.heightPixels)
        }

    }

    private fun adjustCalendarSize() = with(binding) {
        //1080 이하인 기기 캘린더 사이즈 조정
        //nexus 720 1280
        //pixel2 1080 1920
        //갤럭시s21 1080 2400
        //note9 1440 2960
        val widthPixel = getDisplaySize().first
        if (widthPixel <= 1080) {
            val density: Float = calendarLine1.resources.displayMetrics.density

            materialCalendarView.apply {
                setTileSizeDp(40)
                setPadding(0, 0, 0, 30.toPx())
            }
            val calendarLine1LayoutParams: ConstraintLayout.LayoutParams =
                calendarLine1.layoutParams as ConstraintLayout.LayoutParams
            val calendarLine2LayoutParams: ConstraintLayout.LayoutParams =
                calendarLine2.layoutParams as ConstraintLayout.LayoutParams
            calendarLine1LayoutParams.setMargins(
                (calendarLine1.marginEnd.toDp() * density).roundToInt(),
                ((calendarLine1.marginTop.toDp() - 2) * density).roundToInt(),
                (calendarLine1.marginEnd.toDp() * density).roundToInt(),
                0
            )
            calendarLine2LayoutParams.setMargins(
                (calendarLine1.marginEnd.toDp() * density).roundToInt(),
                ((calendarLine2.marginTop.toDp() - 4) * density).roundToInt(),
                (calendarLine1.marginEnd.toDp() * density).roundToInt(),
                0
            )
        }
    }
}