package me.mrxeman.vulcan.activities.ui.wiadomosci.wiadomosc.files

import android.content.Intent
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import me.mrxeman.vulcan.activities.WebViewActivity
import me.mrxeman.vulcan.activities.ui.wiadomosci.utils.Messages
import me.mrxeman.vulcan.databinding.FragmentWiadFilesBinding

class MyWiadFilesRecyclerViewAdapter(
    private val values: ArrayList<Messages.Attachment>,
) : RecyclerView.Adapter<MyWiadFilesRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(
            FragmentWiadFilesBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
        holder.name.text = item.name
        holder.itemView.setOnClickListener {
            Toast.makeText(it.context, "Opening ${item.name}", Toast.LENGTH_SHORT).show()
            val intent = Intent(it.context, WebViewActivity::class.java)
            intent.putExtra("URL", item.url.toString())
            it.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(binding: FragmentWiadFilesBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val name = binding.fileName
    }

}