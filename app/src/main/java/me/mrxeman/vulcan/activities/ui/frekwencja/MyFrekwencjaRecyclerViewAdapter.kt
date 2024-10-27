package me.mrxeman.vulcan.activities.ui.frekwencja

import android.annotation.SuppressLint
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import me.mrxeman.vulcan.activities.ui.frekwencja.utils.Obecnosc

import me.mrxeman.vulcan.databinding.FragmentFrekwencjaBinding
import me.mrxeman.vulcan.utils.Global

class MyFrekwencjaRecyclerViewAdapter(
    private val values: MutableList<Obecnosc.Frekwencja>?,
) : RecyclerView.Adapter<MyFrekwencjaRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(
            FragmentFrekwencjaBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values?.get(position)!!
        holder.image.setColorFilter(Global.getDefaultPreferences().getInt(
            Global.Attendance.convert(item.frekwencjaCategory),
            Global.Attendance.getDefaultColor(item.frekwencjaCategory)))
        holder.lekcja.text = item.lesson
        holder.obecnosc.text = Global.getDefaultPreferences().getString(
            Global.Attendance.convertText(item.frekwencjaCategory),
            Global.Attendance.getDefaultText(item.frekwencjaCategory)
        )
    }

    override fun getItemCount(): Int = values?.size ?: 0

    inner class ViewHolder(binding: FragmentFrekwencjaBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val image: ImageView = binding.frekwencjaImage
        val lekcja: TextView = binding.frekwencjaLekcja
        val obecnosc: TextView = binding.frekwencjaObecnosc
    }

}