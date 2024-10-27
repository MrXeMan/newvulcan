package me.mrxeman.vulcan.activities.ui.statystyki.oceny.srednia

import android.annotation.SuppressLint
import android.graphics.Paint
import android.graphics.Typeface
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import me.mrxeman.vulcan.R
import me.mrxeman.vulcan.activities.ui.statystyki.oceny.srednia.srednia.Srednia

import me.mrxeman.vulcan.databinding.FragmentStatOcenySredniaBinding

class MyStatOcenySredniaRecyclerViewAdapter(
    private val values: List<Srednia.Przedmiot>,
) : RecyclerView.Adapter<MyStatOcenySredniaRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        Srednia.load()
        return ViewHolder(
            FragmentStatOcenySredniaBinding.inflate(
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
        if (item.srednia.isNaN()) {
            holder.srednia.text = "Brak"
        } else {
            holder.srednia.text = String.format("%.2f", item.srednia)
        }
        if (item.name == "Wszystko") {
            holder.name.setTypeface(null, Typeface.BOLD)
            holder.name.textAlignment = View.TEXT_ALIGNMENT_CENTER
            holder.srednia.setTypeface(null, Typeface.BOLD)
        } else {
            holder.name.setTypeface(null, Typeface.NORMAL)
            holder.name.textAlignment = View.TEXT_ALIGNMENT_TEXT_START
            holder.srednia.setTypeface(null, Typeface.NORMAL)
        }
    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(binding: FragmentStatOcenySredniaBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val name: TextView = binding.przedmiot
        val srednia: TextView = binding.srednia
    }

}