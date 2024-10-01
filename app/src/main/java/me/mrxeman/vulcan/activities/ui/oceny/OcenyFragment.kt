package me.mrxeman.vulcan.activities.ui.oceny

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import me.mrxeman.vulcan.databinding.FragmentOcenyBinding

class OcenyFragment : Fragment() {

    private var _binding: FragmentOcenyBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val ocenyViewModel =
            ViewModelProvider(this)[OcenyViewModel::class.java]

        _binding = FragmentOcenyBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textOceny
        ocenyViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}