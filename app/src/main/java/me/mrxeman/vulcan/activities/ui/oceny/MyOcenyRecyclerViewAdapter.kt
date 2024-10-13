package me.mrxeman.vulcan.activities.ui.oceny

import android.annotation.SuppressLint
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.preference.PreferenceManager
import me.mrxeman.vulcan.R

import me.mrxeman.vulcan.activities.ui.oceny.utils.Oceny
import me.mrxeman.vulcan.databinding.FragmentOcenyBinding
import me.mrxeman.vulcan.utils.Global
import me.mrxeman.vulcan.utils.MyApplication

/**
 * [RecyclerView.Adapter] that can display a [Oceny].
 */
class MyOcenyRecyclerViewAdapter(
    private var values: List<Oceny.Przedmiot>,
) : RecyclerView.Adapter<MyOcenyRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(
            FragmentOcenyBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val przedmiot = values[position]
        holder.text.text = przedmiot.name
        if (przedmiot.oceny.isEmpty()) {
            holder.newest.text = ""
            holder.newestText.text = ""
        } else {
            val grade = przedmiot.getNewestGrade()
            holder.newestText.text = "Newest: "
            holder.newest.text = grade!!.grade
            holder.newest.setTextColor(
                PreferenceManager.getDefaultSharedPreferences(MyApplication.getContext()).getInt("importance${grade.importance}color",
                Global.Importance.getDefaultColor("importance${grade.importance}color")))
        }

        holder.next.setOnClickListener {
            Oceny.selectedPrzedmiot = przedmiot
            it.findNavController().navigate(R.id.action_nav_oceny_to_nav_ocena)
//            Toast.makeText(MyApplication.getContext(), "Selected ${przedmiot.name}", Toast.LENGTH_SHORT).show()
        }
    }



    @SuppressLint("NotifyDataSetChanged")
    fun setFilteredList(filteredList: MutableList<Oceny.Przedmiot>?) {
        values = filteredList ?: Oceny.przedmioty
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(binding: FragmentOcenyBinding) : RecyclerView.ViewHolder(binding.root) {
        val text: TextView = binding.przedmiotText
        val next: ImageButton = binding.przedmiotButton
        val newest: TextView = binding.newestGrade
        val newestText: TextView = binding.newestText

        override fun toString(): String {
            return super.toString() + " '" + text.text + "'"
        }
    }

}