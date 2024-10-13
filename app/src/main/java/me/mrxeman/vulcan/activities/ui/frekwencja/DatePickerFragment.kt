package me.mrxeman.vulcan.activities.ui.frekwencja

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.DatePicker
import androidx.fragment.app.DialogFragment
import me.mrxeman.vulcan.activities.ui.frekwencja.placeholder.Obecnosc
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class DatePickerFragment : DialogFragment(), DatePickerDialog.OnDateSetListener {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val year = FrekwencjaFragment.selectedDate.year
        val month = FrekwencjaFragment.selectedDate.monthValue
        val day = FrekwencjaFragment.selectedDate.dayOfMonth

        return DatePickerDialog(this.requireContext(), this, year, month - 1, day)
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        FrekwencjaFragment.selectedDate = LocalDate.of(year, month + 1, dayOfMonth)
        if (parentFragment is FrekwencjaFragment) {
            (parentFragment as FrekwencjaFragment).datePicker!!.text = FrekwencjaFragment.selectedDate.format(
                DateTimeFormatter.ofPattern("dd.MM.yyyy"))
            (parentFragment as FrekwencjaFragment).setAdapter()
        }
    }

}