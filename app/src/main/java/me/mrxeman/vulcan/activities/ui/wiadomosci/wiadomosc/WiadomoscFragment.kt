package me.mrxeman.vulcan.activities.ui.wiadomosci.wiadomosc

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import me.mrxeman.vulcan.R
import me.mrxeman.vulcan.activities.ui.wiadomosci.utils.Messages
import me.mrxeman.vulcan.activities.ui.wiadomosci.wiadomosc.files.MyWiadFilesRecyclerViewAdapter
import java.time.format.DateTimeFormatter

class WiadomoscFragment : Fragment() {

    private var columnCount = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        val view = inflater.inflate(R.layout.fragment_wiadomosc_extra, container, false)

        val topic: TextView = view.findViewById(R.id.topic_extra)
        val date: TextView = view.findViewById(R.id.dateExtra)
        val sender: TextView = view.findViewById(R.id.sender)
        val receiver: TextView = view.findViewById(R.id.receiver)
        val subject: TextView = view.findViewById(R.id.subject)
        val recyclerView: RecyclerView = view.findViewById(R.id.files)
        val respond: Button = view.findViewById(R.id.respond)
        val forward: Button = view.findViewById(R.id.forward)

        respond.setOnClickListener {
            Toast.makeText(context, "Responding function!", Toast.LENGTH_SHORT).show()
        }
        forward.setOnClickListener {
            Toast.makeText(context, "Forwarding function!", Toast.LENGTH_SHORT).show()
        }

        var files: ArrayList<Messages.Attachment> = arrayListOf()

        if (selectedMessage == null) {
            topic.text = "Brak"
            date.text = "00.00.0000 00:00"
            sender.text = "Brak"
            receiver.text = "Brak"
            subject.text = "Brak"
        } else {
            topic.text = selectedMessage!!.topic
            date.text = selectedMessage!!.date.format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"))
            sender.text = selectedMessage!!.sender.name
            receiver.text = selectedMessage!!.receiver
            subject.text = selectedMessage!!.extra.invoke().first
            files = selectedMessage!!.extra.invoke().second
        }

        with(recyclerView) {
            layoutManager = when {
                columnCount <= 1 -> LinearLayoutManager(context)
                else -> GridLayoutManager(context, columnCount)
            }
            adapter = MyWiadFilesRecyclerViewAdapter(files)
        }

        return view
    }

    companion object {
        var selectedMessage: Messages.message? = null
    }
}