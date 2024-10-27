package me.mrxeman.vulcan.activities.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import me.mrxeman.vulcan.activities.ui.wiadomosci.utils.Messages
import me.mrxeman.vulcan.activities.ui.frekwencja.utils.Obecnosc
import me.mrxeman.vulcan.activities.ui.lekcje.utils.Lessons
import me.mrxeman.vulcan.activities.ui.oceny.utils.Oceny
import me.mrxeman.vulcan.activities.ui.sprawdziany.utils.Sprawdziany
import me.mrxeman.vulcan.activities.ui.statystyki.frekwencja.przedmioty.obecnosci.Frekwencja
import me.mrxeman.vulcan.activities.ui.statystyki.frekwencja.srednia.srednia.Srednia
import me.mrxeman.vulcan.databinding.FragmentHomeBinding
import kotlin.concurrent.thread

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        val homeViewModel =
            ViewModelProvider(this)[HomeViewModel::class.java]

        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        homeViewModel.gradeJSON.observe(viewLifecycleOwner) {
            if (it.isEmpty()) return@observe
            Oceny.load(it)
            Log.i("LOADS", "Oceny: $it")
        }
        homeViewModel.frekwencjaJSON.observe(viewLifecycleOwner) {
            if (it.isJsonNull) return@observe
            Obecnosc.load(it)
            Srednia.load(it)
            Frekwencja.load(it)
            Log.i("LOADS", "Frekwencja: $it")
        }
        homeViewModel.lessonsJSON.observe(viewLifecycleOwner) {
            if (it.isJsonNull) return@observe
            Lessons.load(it)
            Log.i("LOADS", "Lessons: $it")
        }
        homeViewModel.testsJSON.observe(viewLifecycleOwner) {
            if (it.isJsonNull) return@observe
            Sprawdziany.load(it)
            Log.i("LOADS", "Tests: $it")
        }
        homeViewModel.rMessagesJSON.observe(viewLifecycleOwner) {
            if (it.isJsonNull) return@observe
            Messages.loadReceived(it)
            Log.i("LOADS", "Messages: $it")
        }
        homeViewModel.sMessagesJSON.observe(viewLifecycleOwner) {
            if (it.isJsonNull) return@observe
            Messages.loadSent(it)
            Log.i("LOADS", "Messages: $it")
        }

//        binding.loadHomeworkButton.setOnClickListener {
//            val temporary = """
//                [{"typ":2,"przedmiotNazwa":"Język niemiecki","data":"2024-10-02T00:00:00+02:00","hasAttachment":false,"id":18572},{"typ":3,"przedmiotNazwa":"Podstawy przedsiębiorczości","data":"2024-10-01T00:00:00+02:00","hasAttachment":false,"id":18487},{"typ":3,"przedmiotNazwa":"Podstawy przedsiębiorczości","data":"2024-10-02T00:00:00+02:00","hasAttachment":false,"id":18680},{"typ":2,"przedmiotNazwa":"Historia i teraźniejszość","data":"2024-10-02T00:00:00+02:00","hasAttachment":false,"id":18623},{"typ":2,"przedmiotNazwa":"Historia i teraźniejszość","data":"2024-10-23T00:00:00+02:00","hasAttachment":false,"id":18817},{"typ":1,"przedmiotNazwa":"Historia","data":"2024-10-30T00:00:00+01:00","hasAttachment":false,"id":18813},{"typ":1,"przedmiotNazwa":"Eksploatacja urządzeń peryferyjnych i sieciowych","data":"2024-10-04T00:00:00+02:00","hasAttachment":false,"id":18568},{"typ":1,"przedmiotNazwa":"Matematyka","data":"2024-10-09T00:00:00+02:00","hasAttachment":false,"id":18607},{"typ":2,"przedmiotNazwa":"Chemia","data":"2024-10-09T00:00:00+02:00","hasAttachment":false,"id":18692},{"typ":2,"przedmiotNazwa":"Biologia","data":"2024-10-10T00:00:00+02:00","hasAttachment":false,"id":18698},{"typ":2,"przedmiotNazwa":"Fizyka","data":"2024-10-10T00:00:00+02:00","hasAttachment":false,"id":18627},{"typ":2,"przedmiotNazwa":"Bezpieczeństwo i higiena pracy","data":"2024-10-08T00:00:00+02:00","hasAttachment":false,"id":18668},{"typ":4,"przedmiotNazwa":"Fizyka","data":"2024-10-03T00:00:00+02:00","hasAttachment":false,"id":9155},{"typ":4,"przedmiotNazwa":"Język angielski","data":"2024-10-08T00:00:00+02:00","hasAttachment":false,"id":9162}]
//            """.trimIndent()
//            val temporary2 = """
//                {"typ":1,"data":"2024-10-04T00:00:00+02:00","przedmiotNazwa":"Eksploatacja urządzeń peryferyjnych i sieciowych","nauczycielImieNazwisko":"Aleksander Pietnoczka","opis":"Konfiguracja routera - WAN, LAN, DHCP, Wi-Fi","sprawdzianModulDydaktyczny":false,"linki":[],"id":18568}
//            """.trimIndent()
//            Toast.makeText(MyApplication.getContext(), "Loaded Homework!", Toast.LENGTH_SHORT).show()
//            ZadaniaDomowe.load(temporary, temporary2)
//        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}