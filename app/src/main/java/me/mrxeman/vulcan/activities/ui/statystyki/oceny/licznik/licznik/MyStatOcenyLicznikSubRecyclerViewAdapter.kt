package me.mrxeman.vulcan.activities.ui.statystyki.oceny.licznik.licznik

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import me.mrxeman.vulcan.activities.ui.oceny.utils.Oceny
import me.mrxeman.vulcan.databinding.FragmentStatOcenyLicznikSubBinding
import me.mrxeman.vulcan.utils.Global
import java.time.format.DateTimeFormatter

class MyStatOcenyLicznikSubRecyclerViewAdapter(
    private var values: List<Licznik.Oceny>)
    : RecyclerView.Adapter<MyStatOcenyLicznikSubRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(
            FragmentStatOcenyLicznikSubBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
        holder.name.text = item.name
        holder.date.text = item.date?.let { Global.convertToLocalDateViaMilisecond(it).format(Global.dayFormat) }
            ?: "Brak"
    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(binding: FragmentStatOcenyLicznikSubBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val name: TextView = binding.przedmiotName
        val date: TextView = binding.dateText2
    }
}