package me.mrxeman.vulcan.activities.ui.domowe

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import me.mrxeman.vulcan.R
import me.mrxeman.vulcan.activities.ui.domowe.utils.ZadaniaDomowe

import me.mrxeman.vulcan.utils.Global
import java.time.LocalDate


class MyZadaniaDomoweRecyclerViewAdapter(
    private val hvalues: MutableList<ZadaniaDomowe.Item>,
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val DATE_VARIANT: Int = 0
    private val HW_VARIANT: Int = 1

    internal class MainHolder(itemView: View?) : RecyclerView.ViewHolder(itemView!!) {

        var dayView: TextView
        var dateView: TextView

        init {
            dayView = itemView!!.findViewById(R.id.dayText)
            dateView  = itemView.findViewById(R.id.dateText)
        }
    }

    internal class SubHolder(itemView: View?) : RecyclerView.ViewHolder(itemView!!) {

        var lesson: TextView
        var subject: TextView

        init {
            lesson = itemView!!.findViewById(R.id.lessonText)
            subject = itemView.findViewById(R.id.reqText)
        }
    }

    override fun getItemViewType(position: Int): Int {
        val item: ZadaniaDomowe.Item = hvalues[position]
        return when (item.date) {
            true -> {
                DATE_VARIANT
            }
            false -> {
                HW_VARIANT
            }
        }
    }

    override fun getItemCount(): Int {
        return hvalues.size
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): RecyclerView.ViewHolder {

        return when (viewType) {
            DATE_VARIANT -> {
                val view: View = LayoutInflater.from(parent.context)
                    .inflate(R.layout.fragment_sprawdziany_main, parent, false)

                MainHolder(view)
            }

            HW_VARIANT -> {
                val view: View = LayoutInflater.from(parent.context)
                    .inflate(R.layout.fragment_sprawdziany_sub, parent, false)

                SubHolder(view)
            }

            else -> {
                throw Exception("How did you do it? Here you go!")
            }
        }

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder.itemViewType) {
            DATE_VARIANT -> {
                if (!hvalues[position].date) throw Exception("Don't know how bruh.")
                val date: LocalDate = hvalues[position].extra as LocalDate
                val view: MainHolder = holder as MainHolder
                view.dayView.text = date.dayOfWeek.name
                view.dateView.text = date.format(Global.dayFormat)
            }
            HW_VARIANT -> {
                if (hvalues[position].date) throw Exception("Don't know how bruh.")
                val spr: ZadaniaDomowe.ZadanieDomowe = hvalues[position].extra as ZadaniaDomowe.ZadanieDomowe
                val view: SubHolder = holder as SubHolder
                view.subject.text = spr.description
                view.lesson.text = spr.przedmiot
            }
        }
    }

}