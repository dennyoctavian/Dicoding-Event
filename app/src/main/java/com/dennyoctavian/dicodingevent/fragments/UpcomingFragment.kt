package com.dennyoctavian.dicodingevent.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dennyoctavian.dicodingevent.adapters.ListActiveEventAdapter
import com.dennyoctavian.dicodingevent.databinding.FragmentUpcomingBinding
import com.dennyoctavian.dicodingevent.viewmodels.ActiveEventViewModel

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [UpcomingFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class UpcomingFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private lateinit var binding: FragmentUpcomingBinding
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUpcomingBinding.inflate(inflater, container, false)
        val activeViewModel = ViewModelProvider(this)[ActiveEventViewModel::class.java]
        activeViewModel.isLoading.observe(viewLifecycleOwner) {
                isLoading ->
            run {
                if (isLoading) {
                    binding.upcomingLoading.visibility = View.VISIBLE
                    binding.upcomingEvent.visibility = View.INVISIBLE
                    binding.noUpComingEvent.visibility = View.INVISIBLE
                } else {
                    binding.upcomingLoading.visibility = View.GONE
                }
            }
        }
        activeViewModel.error.observe(viewLifecycleOwner) { error ->
            run {
                if (error != null) {
                    binding.noUpComingEvent.text = error.toString()
                    binding.noUpComingEvent.visibility = View.VISIBLE
                    binding.upcomingEvent.visibility = View.INVISIBLE
                }
            }
        }
        activeViewModel.activeEvent.observe(viewLifecycleOwner) {
                activeEvent ->
            run {
                if (activeEvent.isEmpty()) {
                    binding.noUpComingEvent.text = "Tidak Ada Past Event"
                    binding.noUpComingEvent.visibility = View.VISIBLE
                    binding.upcomingEvent.visibility = View.INVISIBLE
                } else {
                    binding.noUpComingEvent.visibility = View.GONE
                    binding.upcomingEvent.visibility = View.VISIBLE
                    binding.upcomingEvent.layoutManager = LinearLayoutManager(context)
                    binding.upcomingEvent.adapter = ListActiveEventAdapter(activeEvent, requireContext())
                }
            }
        }
        activeViewModel.loadActiveEvents()
        return binding.root
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment UpcomingFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            UpcomingFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}