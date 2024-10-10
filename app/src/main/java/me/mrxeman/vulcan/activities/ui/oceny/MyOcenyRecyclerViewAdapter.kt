package me.mrxeman.vulcan.activities.ui.oceny

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import me.mrxeman.vulcan.R

import me.mrxeman.vulcan.activities.ui.oceny.utils.Oceny
import me.mrxeman.vulcan.databinding.FragmentOcenyBinding
import me.mrxeman.vulcan.utils.MyApplication

/**
 * [RecyclerView.Adapter] that can display a [Oceny].
 */
class MyOcenyRecyclerViewAdapter(
    private val values: List<Oceny.Przedmiot>,
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

        holder.next.setOnClickListener {
            Toast.makeText(MyApplication.getContext(), "Selected ${przedmiot.name}", Toast.LENGTH_SHORT).show()
        }
    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(binding: FragmentOcenyBinding) : RecyclerView.ViewHolder(binding.root) {
        val text: TextView = binding.przedmiotText
        val next: ImageButton = binding.przedmiotButton

        override fun toString(): String {
            return super.toString() + " '" + text.text + "'"
        }
    }

}