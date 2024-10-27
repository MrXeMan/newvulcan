package me.mrxeman.vulcan.activities.ui.statystyki.frekwencja.przedmioty.obecnosci

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.findFragment
import androidx.navigation.NavDirections
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import me.mrxeman.vulcan.R
import me.mrxeman.vulcan.activities.ui.frekwencja.FrekwencjaFragment
import me.mrxeman.vulcan.activities.ui.oceny.utils.Oceny
import me.mrxeman.vulcan.databinding.FragmentStatFrekwencjaPrzedmiotSubBinding
import me.mrxeman.vulcan.databinding.FragmentStatOcenyLicznikSubBinding
import me.mrxeman.vulcan.utils.Global
import java.time.format.DateTimeFormatter

class MyStatFrekwencjaPrzedmiotSubRecyclerViewAdapter(
    private var values: List<Frekwencja.Lekcja>)
    : RecyclerView.Adapter<MyStatFrekwencjaPrzedmiotSubRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(
            FragmentStatFrekwencjaPrzedmiotSubBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
        holder.name.text = item.name
        holder.hours.text = "${item.dateOd.format(Global.hourFormat)} - ${item.dateDo.format(Global.hourFormat)}"
        holder.date.text = item.dateOd.format(Global.dayFormat)
        holder.itemView.setOnClickListener {
            FrekwencjaFragment.selectedDate = item.dateOd.toLocalDate()
            it.findNavController().navigate(R.id.action_nav_statystyki_to_nav_frekwencja)
        }
    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(binding: FragmentStatFrekwencjaPrzedmiotSubBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val name: TextView = binding.przedmiotText2
        val hours: TextView = binding.hoursText
        val date: TextView = binding.dateText3
    }
}