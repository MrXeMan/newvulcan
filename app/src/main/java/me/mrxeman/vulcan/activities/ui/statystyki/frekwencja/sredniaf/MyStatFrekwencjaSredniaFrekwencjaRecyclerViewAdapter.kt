package me.mrxeman.vulcan.activities.ui.statystyki.frekwencja.sredniaf

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import me.mrxeman.vulcan.R

import me.mrxeman.vulcan.activities.ui.statystyki.frekwencja.sredniaf.placeholder.PlaceholderContent.PlaceholderItem
import me.mrxeman.vulcan.databinding.FragmentStatFrekwencjaSredniaFrekwencjaBinding

/**
 * [RecyclerView.Adapter] that can display a [PlaceholderItem].
 * TODO: Replace the implementation with code for your data type.
 */
class MyStatFrekwencjaSredniaFrekwencjaRecyclerViewAdapter(
    private val values: List<PlaceholderItem>,
) : RecyclerView.Adapter<MyStatFrekwencjaSredniaFrekwencjaRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(
            FragmentStatFrekwencjaSredniaFrekwencjaBinding.inflate(
                LayoutInflater.from(
                    parent.context
                ), parent, false
            )
        )

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
        holder.idView.text = item.id
        holder.contentView.text = item.content
    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(binding: FragmentStatFrekwencjaSredniaFrekwencjaBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val idView: TextView = binding.itemNumber
        val contentView: TextView = binding.content

        override fun toString(): String {
            return super.toString() + " '" + contentView.text + "'"
        }
    }

}