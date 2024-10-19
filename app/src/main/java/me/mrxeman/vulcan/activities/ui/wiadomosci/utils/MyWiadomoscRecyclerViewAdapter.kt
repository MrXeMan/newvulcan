package me.mrxeman.vulcan.activities.ui.wiadomosci.utils

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import me.mrxeman.vulcan.R
import me.mrxeman.vulcan.activities.ui.wiadomosci.wiadomosc.WiadomoscFragment

import me.mrxeman.vulcan.databinding.FragmentWiadomoscBinding
import java.time.format.DateTimeFormatter


class MyWiadomoscRecyclerViewAdapter(
    private val values: List<Messages.message>,
) : RecyclerView.Adapter<MyWiadomoscRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(
            FragmentWiadomoscBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
        holder.name.text = item.sender.name
        holder.date.text = item.date.format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"))
        holder.topic.text = item.topic
        holder.read.isChecked = item.read
        holder.forwarded.isChecked = item.forwarded
        holder.responded.isChecked = item.responded
        holder.itemView.setOnClickListener {
            WiadomoscFragment.selectedMessage = item
            it.findNavController().navigate(R.id.action_nav_wiadomosci_to_nav_wiadomosc)
        }
    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(binding: FragmentWiadomoscBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val name = binding.name
        val topic = binding.topic
        val date = binding.date
        val read = binding.read
        val forwarded = binding.forwarded
        val responded = binding.responded
    }

}