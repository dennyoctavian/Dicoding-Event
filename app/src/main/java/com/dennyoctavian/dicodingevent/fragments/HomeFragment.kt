package com.dennyoctavian.dicodingevent.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dennyoctavian.dicodingevent.adapters.ListActiveHomeEventAdapter
import com.dennyoctavian.dicodingevent.adapters.ListPastHomeEventAdapter
import com.dennyoctavian.dicodingevent.databinding.FragmentHomeBinding
import com.dennyoctavian.dicodingevent.viewmodels.HomeViewModel

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private lateinit var binding: FragmentHomeBinding
    private var param1: String? = null
    private var param2: String? = null

    @SuppressLint("SetTextI18n")
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
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        val homeViewModel = ViewModelProvider(this)[HomeViewModel::class.java]
        homeViewModel.isLoadingActiveEvent.observe(viewLifecycleOwner) {
                isLoading ->
            run {
                if (isLoading) {
                    binding.upComingLoading.visibility = View.VISIBLE
                    binding.upcomingRV.visibility = View.INVISIBLE
                    binding.noUpcomingEvent.visibility = View.INVISIBLE
                } else {
                    binding.upComingLoading.visibility = View.GONE
                }
            }
        }
        homeViewModel.activeEvent.observe(viewLifecycleOwner) {
                activeEvent ->
            run {
                if (activeEvent.isEmpty()) {
                    binding.noUpcomingEvent.text = "Tidak Ada Upcoming Event Tersedia"
                    binding.noUpcomingEvent.visibility = View.VISIBLE
                    binding.upcomingRV.visibility = View.INVISIBLE
                } else {
                    binding.noUpcomingEvent.visibility = View.GONE
                    binding.upcomingRV.visibility = View.VISIBLE
                    binding.upcomingRV.layoutManager = LinearLayoutManager(context,
                        LinearLayoutManager.HORIZONTAL, false
                    )
                    binding.upcomingRV.adapter = ListActiveHomeEventAdapter(activeEvent, requireContext())
                }
            }
        }

        homeViewModel.errorActiveEvent.observe(viewLifecycleOwner) { error ->
            run {
                if (error != null) {
                    binding.noUpcomingEvent.text = error.toString()
                    binding.noUpcomingEvent.visibility = View.VISIBLE
                    binding.upcomingRV.visibility = View.INVISIBLE
                }
            }
        }

        homeViewModel.isLoadingPastEvent.observe(viewLifecycleOwner) {
                isLoading ->
            run {
                if (isLoading) {
                    binding.finishedLoading.visibility = View.VISIBLE
                    binding.finishedRV.visibility = View.INVISIBLE
                    binding.noFinishedEvent.visibility = View.INVISIBLE
                } else {
                    binding.finishedLoading.visibility = View.GONE
                }
            }
        }

        homeViewModel.pastEvent.observe(viewLifecycleOwner) {
                pastEvent ->
            run {
                if (pastEvent.isEmpty()) {
                    binding.noFinishedEvent.text = "Tidak Ada Past Event"
                    binding.noFinishedEvent.visibility = View.VISIBLE
                    binding.finishedRV.visibility = View.INVISIBLE
                } else {
                    binding.noFinishedEvent.visibility = View.GONE
                    binding.finishedRV.visibility = View.VISIBLE
                    binding.finishedRV.layoutManager = LinearLayoutManager(context)
                    binding.finishedRV.adapter = ListPastHomeEventAdapter(pastEvent, requireContext())
                }
            }
        }



        homeViewModel.errorPastEvent.observe(viewLifecycleOwner) { error ->
            run {
                if (error != null) {
                    binding.noFinishedEvent.text = error.toString()
                    binding.noFinishedEvent.visibility = View.VISIBLE
                    binding.finishedRV.visibility = View.INVISIBLE
                }
            }
        }
        homeViewModel.loadActiveEvents()
        homeViewModel.loadAPastEvents()
        return binding.root
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment HomeFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}