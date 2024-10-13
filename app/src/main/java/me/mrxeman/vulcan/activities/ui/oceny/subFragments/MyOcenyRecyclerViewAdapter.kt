package me.mrxeman.vulcan.activities.ui.oceny.subFragments

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.content.res.ColorStateList
import android.graphics.Color
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.preference.PreferenceManager

import me.mrxeman.vulcan.activities.ui.oceny.utils.Oceny
import me.mrxeman.vulcan.databinding.FragmentOcenaBinding
import me.mrxeman.vulcan.utils.Global
import me.mrxeman.vulcan.utils.MyApplication
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeFormatterBuilder
import java.util.Date
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
        holder.ocenaData.text = item.data?.let { Date(it) }
            ?.let { SimpleDateFormat("dd.mm.yyyy").format(it) }
        holder.ocenaName.text = item.columnName
        holder.ocenaTyp.text = item.categoryName
        holder.ocenaWaga.text = "Waga: ${item.importance}"
        holder.ocena.setTextColor(PreferenceManager.getDefaultSharedPreferences(MyApplication.getContext()).getInt("importance${item.importance}color",
            Global.Importance.getDefaultColor("importance${item.importance}color")))
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