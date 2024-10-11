package me.mrxeman.vulcan.activities.ui.oceny.subFragments

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.graphics.Color
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView

import me.mrxeman.vulcan.activities.ui.oceny.utils.Oceny
import me.mrxeman.vulcan.databinding.FragmentOcenaBinding
import me.mrxeman.vulcan.utils.Global
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeFormatterBuilder
import java.util.Locale.Category

/**
 * [RecyclerView.Adapter] that can display a [PlaceholderItem].
 * TODO: Replace the implementation with code for your data type.
 */
class MyOcenyRecyclerViewAdapter(
        private var oceny: List<Oceny.Ocena>)
    : RecyclerView.Adapter<MyOcenyRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

    return ViewHolder(FragmentOcenaBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    }

    @SuppressLint("SetTextI18n", "SimpleDateFormat")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = oceny[position]
        holder.ocena.text = item.grade
        holder.ocenaData.text = SimpleDateFormat("dd.mm.yyyy").format(item.data)
        holder.ocenaName.text = item.columnName
        holder.ocenaTyp.text = item.categoryName
        holder.ocenaWaga.text = "Waga: ${item.importance}"
        if (Global.getColor(item.importance) != null) {
            holder.ocena.setTextColor(Global.getColor(item.importance))
        }
    }

    override fun getItemCount(): Int = Oceny.selectedPrzedmiot!!.oceny.size

    inner class ViewHolder(binding: FragmentOcenaBinding) : RecyclerView.ViewHolder(binding.root) {
        val ocena: TextView = binding.ocena
        val ocenaName = binding.nazwaKolumny
        val ocenaTyp = binding.typOceny
        val ocenaData = binding.dataOceny
        val ocenaWaga = binding.wagaOceny

        override fun toString(): String {
            return super.toString() + " '" + ocena.text + "'"
        }
    }

}