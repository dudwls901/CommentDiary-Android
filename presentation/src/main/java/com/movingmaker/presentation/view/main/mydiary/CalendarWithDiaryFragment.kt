package com.movingmaker.presentation.view.main.mydiary

import android.os.Bundle
import android.util.DisplayMetrics
import android.view.View
import android.view.WindowManager
import android.view.WindowMetrics
import androidx.appcompat.content.res.AppCompatResources
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.core.view.marginEnd
import androidx.core.view.marginTop
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.movingmaker.presentation.R
import com.movingmaker.presentation.base.BaseFragment
import com.movingmaker.presentation.databinding.FragmentMydiaryWithCalendarBinding
import com.movingmaker.presentation.util.Extension.toDp
import com.movingmaker.presentation.util.Extension.toPx
import com.movingmaker.presentation.util.FRAGMENT_NAME
import com.movingmaker.presentation.util.PreferencesUtil
import com.movingmaker.presentation.util.combineList
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
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.Calendar
import javax.inject.Inject
import kotlin.math.roundToInt

@AndroidEntryPoint
class CalendarWithDiaryFragment :
    BaseFragment<FragmentMydiaryWithCalendarBinding>(R.layout.fragment_mydiary_with_calendar) {

    private val myDiaryViewModel: MyDiaryViewModel by activityViewModels()
    private val fragmentViewModel: FragmentViewModel by activityViewModels()
    private var defenceFirstMonthMoveListener = true

    @Inject
    lateinit var preferencesUtil: PreferencesUtil

    @ExperimentalCoroutinesApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.vm = myDiaryViewModel
        myDiaryViewModel.setUserId(preferencesUtil.getUserId())
        fragmentViewModel.setCurrentFragment(FRAGMENT_NAME.CALENDAR_WITH_DIARY)
        defenceFirstMonthMoveListener = true
        initViews()
        observeData()
    }


    @ExperimentalCoroutinesApi
    private fun observeData() = with(myDiaryViewModel) {

        /**
         * 코멘트 받아서 푸시로 들어온 경우
         * 1. selectedDate 해당 일기 날짜로 설정
         * 2. 달력 이동 로직 (getMonthDiary -> 해당 일기 check -> selectedDiary 해당 일기로 설정)
         * 3. 해당 일기 detail 화면으로 이동
         * */
        pushDate.observe(viewLifecycleOwner) {
            viewLifecycleOwner.lifecycleScope.launch {
                Timber.d("observeData: push ${pushDate.value}")
                val date = it
                setSelectedDate(date)
                refreshViews(date)
                findNavController().navigate(CalendarWithDiaryFragmentDirections.actionCalendarWithDiaryFragmentToCommentDiaryDetailFragment())
            }
        }

        /**
         * 기존에는 monthdiary받아올 동안 selectedDate를 바꿔놓고 monthdiary 다 받아오면 그동안 바꿔놓은 selectedDate로 처리했음
         * 이제는 selectedDate, selectedDiary, haveWrittenComment로 화면 상태 정의하려고 하니 이 monthDiary는 미리 선행되어야 함이 확실해야 함
         * monthDiary 받아온 후 클릭한 날짜 하단 일기뷰 상태 처리
         * */
        monthDiaries.observe(viewLifecycleOwner) {
            with(binding.materialCalendarView) {
                removeDecorators()
                addDecorators(
                    TodayDotDecorator(),
                    AloneDotDecorator(
                        requireContext(),
                        aloneDiaryDates.value!!
                    ),
                    CommentDotDecorator(
                        requireContext(),
                        combineList(
                            commentDiaryDates.value!!,
                            tempDiaryDates.value!!
                        )
                    ),
                )
            }
        }

        /*
        * todo 2번 로직 변경하기
        * 1. 외부에서 달력 이둥
        * 2. 화면 재진입 포커싱 유지 / 달력 이동 포커싱 해제
        * 3. Month Diary 가져오기
        * 4. Month Diary 가져온 후 선택한 날짜에 대한 하단 일기 프리뷰 상태 변경
        * */
        selectedYearMonth.observe(viewLifecycleOwner) {
            Timber.e("?? $it $defenceFirstMonthMoveListener")
            //화면 재진입시 포커싱 해제 대응
            if (!defenceFirstMonthMoveListener) {
                myDiaryViewModel.setSelectedDate(null)
                binding.materialCalendarView.selectedDate = null
                viewLifecycleOwner.lifecycleScope.launch {
                    myDiaryViewModel.updatePeriodDiaries(it)
                }
            }
            defenceFirstMonthMoveListener = false
        }


        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {

                launch {
                    selectedDateWithMonthDiaries.collectLatest { (date, _) ->
                        when (date) {
                            null -> {
                                setSelectedDiary(null)
                                getDayWrittenComment(null)
                            }
                            else -> {
                                binding.materialCalendarView.selectedDate = toCalenderDay(date)
                                myDiaryViewModel.setSelectedDiary(getDiaryInMonth(date))
                                //오늘 내가 코멘트를 받은 경우 어제 일기를 선택했을 때 오늘 내가 코멘트를 쓴 상태인지 확인 -> Day+1
                                getDayWrittenComment(ymdFormat(ymdToDate(date)!!.plusDays(1)))
                            }
                        }
                    }
                }
                launch {
                    myDiaryViewModel.diaryState.collectLatest { diaryState ->
                        Timber.e("여기 diaryState: ${diaryState.javaClass.simpleName} selectedDate: ${myDiaryViewModel.selectedDate.value} haveDayMyWrittenComment: ${myDiaryViewModel.haveDayWrittenComment.first()} selectedDiary: ${myDiaryViewModel.selectedDiary.value}")
                        when (diaryState) {
                            DiaryState.Init -> {/*no-op*/
                            }
                            DiaryState.NoneSelectedDiary -> {
                                setNoneSelectedDiaryUI()
                            }
                            DiaryState.SelectedFutureDiary -> {
                                setFutureDiaryDateUI()
                            }
                            is DiaryState.AloneDiary -> {
                                setAloneDiaryDateUI()
                            }
                            is DiaryState.EmptyDiary -> {
                                setEmptyDiaryDateUI()
                            }
                            is DiaryState.CommentDiary -> {
                                setCommentDiaryDateUI()
                                when (diaryState) {
                                    is DiaryState.CommentDiary.HaveNotCommentInTime -> {
                                        setHaveNotCommentInTimeUI()
                                    }
                                    is DiaryState.CommentDiary.HaveCommentInTimeCanOpen -> {
                                        setHaveCommentInTimeCanOpenUI()
                                    }
                                    is DiaryState.CommentDiary.HaveCommentInTimeCannotOpen -> {
                                        setHaveCommentInTimeCannotOpenUI()
                                    }
                                    is DiaryState.CommentDiary.HaveNotCommentOutTime -> {
                                        setHaveNotCommentOutTimeUI()
                                    }
                                    is DiaryState.CommentDiary.HaveCommentOutTimeCanOpen -> {
                                        setHaveCommentOutTimeCanOpenUI()
                                    }
                                    is DiaryState.CommentDiary.HaveCommentOutTimeCannotOpen -> {
                                        setHaveCommentOutTimeCannotOpenUI()
                                    }
                                    is DiaryState.CommentDiary.TempDiaryInTime -> {
                                        setTempDiaryInTimeUI()
                                    }
                                    is DiaryState.CommentDiary.TempDiaryOutTime -> {
                                        setTempDiaryOutTimeUI()
                                    }
                                }
                            }
                        }
                    }
                }
            }
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
            myDiaryViewModel.updatePeriodDiaries(ymDate)
        }
    }

    private fun initButtons() = with(binding) {
        //일기가 없는 경우 writeDiary
        writeDiaryLayout.setOnClickListener {
            val action =
                CalendarWithDiaryFragmentDirections.actionCalendarWithDiaryFragmentToWriteDiaryFragment()
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
            viewLifecycleOwner.lifecycleScope.launch {
                val ymdDate = ymdFormat(date)!!
                myDiaryViewModel.setSelectedDate(ymdDate)
            }
        }
    }

    private fun setMonthMoveListener() = with(binding) {

        materialCalendarView.setOnMonthChangedListener { _, date ->
            myDiaryViewModel.setSelectedYearMonth(ymFormatForLocalDate(date))
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


    private fun setNoneSelectedDiaryUI() = with(binding) {
        writeDiaryLayout.isVisible = false
        readDiaryLayout.isVisible = false
        futureTextView.isVisible = false
        bottomDecoImageView.isVisible = false
        noCommentTextView.isVisible = false
    }

    private fun setFutureDiaryDateUI() = with(binding) {
        context?.let {
            bottomDecoImageView.setImageDrawable(
                AppCompatResources.getDrawable(
                    it,
                    R.drawable.img_brick
                )
            )
        }
        readDiaryLayout.isVisible = false
        writeDiaryLayout.isVisible = false
        futureTextView.isVisible = true
        bottomDecoImageView.isVisible = true
    }

    private fun setEmptyDiaryDateUI() = with(binding) {
        context?.let {
            writeDiaryHeaderTextView.setTextColor(it.getColor(R.color.core_orange))
            bottomDecoImageView.setImageDrawable(
                AppCompatResources.getDrawable(
                    it,
                    R.drawable.img_brick_with_pen
                )
            )
        }
        writeDiaryLayout.isVisible = true
        futureTextView.isVisible = false
        readDiaryLayout.isVisible = false
        noCommentTextView.isVisible = false
        bottomDecoImageView.isVisible = true
    }

    private fun setAloneDiaryDateUI() = with(binding) {
        readDiaryLayout.isVisible = true
        writeDiaryLayout.isVisible = false
        bottomDecoImageView.isVisible = false
        commentDiaryNoticeTextView.isVisible = false
        tempDiaryLineBeforeCommentTextView.isVisible = false
        noCommentTextView.isVisible = false
        futureTextView.isVisible = false
        readDiaryLayout.setOnClickListener {
            findNavController().navigate(CalendarWithDiaryFragmentDirections.actionCalendarWithDiaryFragmentToWriteDiaryFragment())
        }
    }

    private fun setCommentDiaryDateUI() = with(binding) {
        readDiaryLayout.isVisible = true
        writeDiaryLayout.isVisible = false
        bottomDecoImageView.isVisible = false
        futureTextView.isVisible = false
        readDiaryLayout.setOnClickListener {
            findNavController().navigate(CalendarWithDiaryFragmentDirections.actionCalendarWithDiaryFragmentToCommentDiaryDetailFragment())
        }
    }

    private fun setHaveNotCommentInTimeUI() = with(binding) {
        context?.let {
            diaryDateTextView.setTextColor(it.getColor(R.color.core_green))
            commentDiaryNoticeTextView.setBackgroundResource(R.drawable.background_light_brown_radius_bottom_10)
            commentDiaryNoticeTextView.setTextColor(it.getColor(R.color.text_dark_brown))
            commentDiaryNoticeTextView.text = getText(R.string.calendar_with_diary_comment_soon)
        }
        commentDiaryNoticeTextView.isVisible = true
        tempDiaryLineBeforeCommentTextView.isVisible = false
        noCommentTextView.isVisible = false
        futureTextView.isVisible = false
    }

    private fun setHaveNotCommentOutTimeUI() = with(binding) {
        context?.let {
            diaryDateTextView.setTextColor(it.getColor(R.color.core_green))
        }
        commentDiaryNoticeTextView.isVisible = false
        noCommentTextView.isVisible = true
        tempDiaryLineBeforeCommentTextView.isVisible = false
        futureTextView.isVisible = false
    }

    private fun setHaveCommentInTimeCanOpenUI() = with(binding) {
        context?.let {
            diaryDateTextView.setTextColor(it.getColor(R.color.core_green))
            commentDiaryNoticeTextView.setBackgroundResource(R.drawable.background_core_green_radius_bottom_10)
            commentDiaryNoticeTextView.setTextColor(it.getColor(R.color.background_ivory))
            commentDiaryNoticeTextView.text = getText(R.string.arrived_comment)
        }
        commentDiaryNoticeTextView.isVisible = true
        tempDiaryLineBeforeCommentTextView.isVisible = false
        noCommentTextView.isVisible = false
        futureTextView.isVisible = false
        //todo
    }

    private fun setHaveCommentOutTimeCanOpenUI() = with(binding) {
        context?.let {
            diaryDateTextView.setTextColor(it.getColor(R.color.core_green))
            commentDiaryNoticeTextView.setBackgroundResource(R.drawable.background_core_green_radius_bottom_10)
            commentDiaryNoticeTextView.setTextColor(it.getColor(R.color.background_ivory))
            commentDiaryNoticeTextView.text = getText(R.string.arrived_comment)
        }
        commentDiaryNoticeTextView.isVisible = true
        tempDiaryLineBeforeCommentTextView.isVisible = false
        noCommentTextView.isVisible = false
        futureTextView.isVisible = false
    }

    private fun setHaveCommentOutTimeCannotOpenUI() = with(binding) {
        context?.let {
            diaryDateTextView.setTextColor(it.getColor(R.color.core_green))
            commentDiaryNoticeTextView.setBackgroundResource(R.drawable.background_core_green_radius_bottom_10)
            commentDiaryNoticeTextView.setTextColor(it.getColor(R.color.background_ivory))
            commentDiaryNoticeTextView.text = getText(R.string.arrived_comment)
        }
        commentDiaryNoticeTextView.isVisible = true
        tempDiaryLineBeforeCommentTextView.isVisible = false
        noCommentTextView.isVisible = false
        futureTextView.isVisible = false
    }

    private fun setHaveCommentInTimeCannotOpenUI() = with(binding) {
        context?.let {
            diaryDateTextView.setTextColor(it.getColor(R.color.core_green))
            commentDiaryNoticeTextView.setBackgroundResource(R.drawable.background_core_green_radius_bottom_10)
            commentDiaryNoticeTextView.setTextColor(it.getColor(R.color.background_ivory))
            commentDiaryNoticeTextView.text = getText(R.string.arrived_comment)
        }
        commentDiaryNoticeTextView.isVisible = true
        tempDiaryLineBeforeCommentTextView.isVisible = false
        noCommentTextView.isVisible = false
        futureTextView.isVisible = false
    }

    private fun setTempDiaryInTimeUI() = with(binding) {
        context?.let {
            diaryDateTextView.setTextColor(it.getColor(R.color.core_green))
            commentDiaryNoticeTextView.setBackgroundResource(R.drawable.background_ivory_radius_bottom_10)
            commentDiaryNoticeTextView.setTextColor(it.getColor(R.color.text_gray_brown))
            commentDiaryNoticeTextView.text = getText(R.string.temp_diary_before_send)
        }
        commentDiaryNoticeTextView.isVisible = true
        tempDiaryLineBeforeCommentTextView.isVisible = true
        noCommentTextView.isVisible = false
        futureTextView.isVisible = false
        readDiaryLayout.setOnClickListener {
            findNavController().navigate(CalendarWithDiaryFragmentDirections.actionCalendarWithDiaryFragmentToWriteDiaryFragment())
        }
    }

    private fun setTempDiaryOutTimeUI() = with(binding) {
        context?.let {
            diaryDateTextView.setTextColor(it.getColor(R.color.core_green))
        }
        commentDiaryNoticeTextView.isVisible = false
        tempDiaryLineBeforeCommentTextView.isVisible = false
        noCommentTextView.isVisible = false
        futureTextView.isVisible = false
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