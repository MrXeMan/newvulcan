package me.mrxeman.vulcan.activities.ui.lekcje

import android.annotation.SuppressLint
import android.graphics.Color
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.navigation.findNavController
import me.mrxeman.vulcan.R
import me.mrxeman.vulcan.activities.ui.lekcje.subFragments.LekcjaFragment
import me.mrxeman.vulcan.activities.ui.lekcje.utils.Lessons
import me.mrxeman.vulcan.databinding.FragmentLekcjeBinding

import me.mrxeman.vulcan.utils.MyApplication

/**
 * [RecyclerView.Adapter] that can display a [PlaceholderItem].
 * TODO: Replace the implementation with code for your data type.
 */
class MyLekcjeRecyclerViewAdapter(
    private val values: MutableList<Lessons.Lekcja>?,
) : RecyclerView.Adapter<MyLekcjeRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(
            FragmentLekcjeBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values?.get(position)!!
        holder.button.setOnClickListener {
            LekcjaFragment.lekcja = item
            it.findNavController().navigate(R.id.action_nav_lekcje_to_nav_lekcja)
        }
        holder.przedmiot.text = item.przedmiot
        holder.godziny.text = "${item.godzinaOd} - ${item.godzinaDo}"
        if (item.zmiany?.kategoria == 5 || item.zmiany?.kategoria == 4) {
            holder.przedmiot.setTextColor(Color.rgb(255, 0, 0))
            holder.image.setColorFilter(Color.rgb(255, 0, 0))
            holder.godziny.setTextColor(Color.rgb(255, 0, 0))
            holder.more.setTextColor(Color.rgb(255, 0, 0))
        } else if (item.zmiany?.kategoria == 6) {
            holder.przedmiot.setTextColor(Color.rgb(0, 255, 0))
            holder.image.setColorFilter(Color.rgb(0, 255, 0))
            holder.godziny.setTextColor(Color.rgb(0, 255, 0))
            holder.more.setTextColor(Color.rgb(0, 255, 0))
        } else if (item.zmiany?.kategoria == 7) {
            holder.przedmiot.setTextColor(Color.rgb(0, 0, 255))
            holder.image.setColorFilter(Color.rgb(0, 0, 255))
            holder.godziny.setTextColor(Color.rgb(0, 0, 255))
            holder.more.setTextColor(Color.rgb(0, 0, 255))
        }
    }

    override fun getItemCount(): Int = values?.size ?: 0

    inner class ViewHolder(binding: FragmentLekcjeBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val image: ImageView = binding.lekcjeImage
        val more: TextView = binding.lekcjeMore
        val przedmiot: TextView = binding.lekcjePrzedmiot
        val godziny: TextView = binding.lekcjeGodziny
        val button: ConstraintLayout = binding.root
    }
}