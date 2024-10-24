package me.mrxeman.vulcan.activities.ui.statystyki.oceny.licznik

import android.annotation.SuppressLint
import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.transition.AutoTransition
import androidx.transition.TransitionManager

import me.mrxeman.vulcan.activities.ui.statystyki.oceny.licznik.licznik.Licznik.Licznik
import me.mrxeman.vulcan.activities.ui.statystyki.oceny.licznik.licznik.Licznik.Oceny
import me.mrxeman.vulcan.activities.ui.statystyki.oceny.licznik.licznik.Licznik.SUB_ITEMS
import me.mrxeman.vulcan.activities.ui.statystyki.oceny.licznik.licznik.MyStatOcenyLicznikSubRecyclerViewAdapter
import me.mrxeman.vulcan.databinding.FragmentStatOcenyLicznikBinding

class MyStatOcenyLicznikRecyclerViewAdapter(
        private val values: List<Licznik>)
    : RecyclerView.Adapter<MyStatOcenyLicznikRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            FragmentStatOcenyLicznikBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false)
            )

    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
        holder.ocena.text = "Ocena: ${item.grade}"
        holder.licznik.text = "Liczba: ${item.number}"
        with(holder.recyclerView) {
            layoutManager = LinearLayoutManager(context)
            adapter = MyStatOcenyLicznikSubRecyclerViewAdapter(getFilteredList(item.grade, SUB_ITEMS))

        }
        holder.itemView.setOnClickListener {
            val v = if (holder.recyclerView.visibility == View.GONE) View.VISIBLE else View.GONE

            if (holder.itemView is ViewGroup) {
                TransitionManager.beginDelayedTransition(holder.itemView as ViewGroup, AutoTransition())
                holder.recyclerView.visibility = v
            } else {
                Log.e(null, "SOMETHING WENT TERRIBLY WRONG - MrXeMan")
            }
        }
    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(binding: FragmentStatOcenyLicznikBinding) : RecyclerView.ViewHolder(binding.root) {
        val ocena: TextView = binding.ocenaText
        val licznik: TextView = binding.licznik
        val recyclerView: RecyclerView = binding.list
    }

    private fun getFilteredList(grade: String, list: MutableList<Oceny>): MutableList<Oceny> {
        val toReturn: MutableList<Oceny> = mutableListOf()
        list.forEach {
            if (it.grade.contentEquals(grade, true)) {
                toReturn.add(it)
            }
        }
        return toReturn
    }

}