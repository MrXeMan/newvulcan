package me.mrxeman.vulcan.activities.ui.statystyki.frekwencja.przedmioty

import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.transition.AutoTransition
import androidx.transition.TransitionManager
import me.mrxeman.vulcan.activities.ui.statystyki.frekwencja.przedmioty.obecnosci.Frekwencja
import me.mrxeman.vulcan.activities.ui.statystyki.frekwencja.przedmioty.obecnosci.MyStatFrekwencjaPrzedmiotSubRecyclerViewAdapter

import me.mrxeman.vulcan.databinding.FragmentStatFrekwencjaPrzedmiotBinding
import me.mrxeman.vulcan.utils.Global

class MyStatFrekwencjaPrzedmiotRecyclerViewAdapter(
    private val values: List<Frekwencja.Frekwencja>,
) : RecyclerView.Adapter<MyStatFrekwencjaPrzedmiotRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(
            FragmentStatFrekwencjaPrzedmiotBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
        holder.type.text = Global.Attendance.getDefaultText(item.category)
        with(holder.list) {
            layoutManager = LinearLayoutManager(context)
            adapter = MyStatFrekwencjaPrzedmiotSubRecyclerViewAdapter(item.lekcje)
        }
        holder.itemView.setOnClickListener {
            val v = if (holder.list.visibility == View.GONE) View.VISIBLE else View.GONE

            if (holder.itemView is ViewGroup) {
                TransitionManager.beginDelayedTransition(holder.itemView as ViewGroup, AutoTransition())
                holder.list.visibility = v
            } else {
                Log.e(null, "SOMETHING WENT TERRIBLY WRONG - MrXeMan")
            }
        }
    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(binding: FragmentStatFrekwencjaPrzedmiotBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val type: TextView = binding.typeText
        val list: RecyclerView = binding.list
    }

}