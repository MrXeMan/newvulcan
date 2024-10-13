package me.mrxeman.vulcan.activities.ui.lekcje

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.DatePicker
import androidx.fragment.app.DialogFragment
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class DatePickerFragment : DialogFragment(), DatePickerDialog.OnDateSetListener {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val year = LekcjeFragment.selectedDate.year
        val month = LekcjeFragment.selectedDate.monthValue
        val day = LekcjeFragment.selectedDate.dayOfMonth

        return DatePickerDialog(this.requireContext(), this, year, month - 1, day)
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        LekcjeFragment.selectedDate = LocalDate.of(year, month + 1, dayOfMonth)
        if (parentFragment is LekcjeFragment) {
            (parentFragment as LekcjeFragment).datePicker!!.text = LekcjeFragment.selectedDate.format(
                DateTimeFormatter.ofPattern("dd.MM.yyyy"))
            (parentFragment as LekcjeFragment).setAdapter()
        }
    }

}