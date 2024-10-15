package me.mrxeman.vulcan.activities.ui.lekcje.subFragments

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import me.mrxeman.vulcan.R
import me.mrxeman.vulcan.activities.ui.lekcje.utils.Lessons
import me.mrxeman.vulcan.utils.Extensions.format
import me.mrxeman.vulcan.utils.Extensions.ifNull
import me.mrxeman.vulcan.utils.Global
import org.w3c.dom.Text
import java.time.format.DateTimeFormatter

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

class LekcjaFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        val root = inflater.inflate(R.layout.fragment_lekcja, container, false)

        if (lekcja == null) {
            return root
        }

        val mainView: ConstraintLayout = root.findViewById(R.id.mainLayout)

        val przedmiot: TextView = mainView.findViewById(R.id.LessonName)
        val teacher: TextView = mainView.findViewById(R.id.teacherName)
        val room: TextView = mainView.findViewById(R.id.roomNumber)
        val group: TextView = mainView.findViewById(R.id.groupText)
        val hours: TextView = mainView.findViewById(R.id.hourText)

        przedmiot.text = lekcja!!.przedmiot
        teacher.text = lekcja!!.nauczyciel
        room.text = "sala: ${lekcja!!.sala}"
        group.text = "grupa: " + if (lekcja!!.grupa == null) "0/0" else lekcja!!.grupa
        hours.text = "${lekcja!!.godzinaOd.format()} - ${lekcja!!.godzinaDo.format()}"

        val subView: ConstraintLayout = root.findViewById(R.id.subLayout)

        if (lekcja!!.zmiany == null) {
            mainView.removeView(mainView.findViewById(R.id.divider))
            (subView.rootView as ConstraintLayout).removeView(subView)
        } else {
            val changes: Lessons.Zmiana = lekcja!!.zmiany!!
            val changeType: TextView = subView.findViewById(R.id.changeType)
            val changedLesson: TextView = subView.findViewById(R.id.changedLessonText)
            val changedDate: TextView = subView.findViewById(R.id.changedDateText)
            val changedHours: TextView = subView.findViewById(R.id.changedHourText)
            val changedRoom: TextView = subView.findViewById(R.id.changedRoomNumber)
            val changedTeacher: TextView = subView.findViewById(R.id.changedTeacherText)
            val extraInfo: TextView = subView.findViewById(R.id.extraInformationText)

            changeType.text = "Typ zmiany: " +  when (changes.kategoria) {
                1 -> "Odwolane"
                5 -> "Przeniesione na inna lekcje"
                6 -> "Przeniesione z innej lekcji"
                7 -> "Zastepstwo za"
                else -> "UNKNOWN";
            }

            changedLesson.text = "Przedmiot: ${changes.przedmiot.ifNull()}"
            changedDate.text = "Data: ${changes.date.ifNull()}"
            changedHours.text = "Godziny: " + if (changes.godzinaOd != null) "${changes.godzinaOd.format()} - ${changes.godzinaDo.format()}" else "Brak"
            changedRoom.text = "Sala: ${changes.sala.ifNull()}"
            changedTeacher.text = "Nauczyciel: ${changes.nauczyciel.ifNull()}"
            if (changes.extra != null) {
                extraInfo.text = changes.extra
            } else {
                subView.removeView(subView.findViewById(R.id.extraInformation))
                subView.removeView(subView.findViewById(R.id.extraInformationText))
            }
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        lekcja = null
    }

    companion object {
        var lekcja: Lessons.Lekcja? = null
    }
}
