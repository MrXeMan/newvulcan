package me.mrxeman.vulcan.activities.ui.statystyki.frekwencja.srednia

import android.annotation.SuppressLint
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView

import me.mrxeman.vulcan.activities.ui.statystyki.frekwencja.srednia.srednia.Srednia.Przedmiot
import me.mrxeman.vulcan.databinding.FragmentStatFrekwencjaSredniaBinding

class MyStatFrekwencjaSredniaRecyclerViewAdapter(
    private val values: List<Przedmiot>,
) : RecyclerView.Adapter<MyStatFrekwencjaSredniaRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(
            FragmentStatFrekwencjaSredniaBinding.inflate(
                LayoutInflater.from(
                    parent.context
                ), parent, false
            )
        )

    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
        holder.przedmiot.text = item.name
        holder.procent.text = String.format("%.2f", item.proc * 100) + "%"
    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(binding: FragmentStatFrekwencjaSredniaBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val przedmiot: TextView = binding.przedmiotText
        val procent: TextView = binding.procentText
    }

}