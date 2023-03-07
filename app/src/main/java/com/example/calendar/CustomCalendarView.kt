package com.example.calendar

  import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.GridView
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
  import com.example.calendar.adapter.GridAdapter
  import java.text.SimpleDateFormat
import java.util.*

class CustomCalendarView : LinearLayout {
    private lateinit var nextBtn: ImageView
    private lateinit var preBtn: ImageView
    private lateinit var currentDate: TextView
    private lateinit var gridView: GridView
    private lateinit var gridAdapter: GridAdapter

    private val MAX_DAY = 42
    private var cal: Calendar = Calendar.getInstance()
    private lateinit var context: Context
    private var dates: MutableList<Date> = mutableListOf()
    private var eventList: MutableList<Events> = mutableListOf()

    private var dateFormat = SimpleDateFormat("MMM yyyy")
    var monthFormat = SimpleDateFormat("MM")
    var yearFormat = SimpleDateFormat("yyyy")

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        this.context = context
        initLayout()
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )


    private fun initLayout() {
        val inflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)
                as LayoutInflater
        val view: View = inflater.inflate(R.layout.calendar_layout, this)
        nextBtn = view.findViewById(R.id.nextBtn)
        preBtn = view.findViewById(R.id.backBtn)
        currentDate = view.findViewById(R.id.currentTime)
        gridView = view.findViewById(R.id.grid_cal)
    }


    fun getGridView(): GridView {
        return gridView
    }

    fun getContexts(): Context {
        return context
    }

    fun getCurrentDate(): TextView {
        return currentDate
    }



    fun getNextBtn():ImageView{
        return nextBtn
    }
    fun getPreBtn():ImageView{
        return preBtn
    }
}