package com.dennyoctavian.dicodingevent.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dennyoctavian.dicodingevent.adapters.ListPastEventAdapter
import com.dennyoctavian.dicodingevent.databinding.FragmentFinishedBinding
import com.dennyoctavian.dicodingevent.viewmodels.PastEventViewModel

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [FinishedFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class FinishedFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private lateinit var binding: FragmentFinishedBinding
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
        binding = FragmentFinishedBinding.inflate(inflater, container, false)
        val pastViewModel = ViewModelProvider(this)[PastEventViewModel::class.java]
        pastViewModel.isLoading.observe(viewLifecycleOwner) {
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
        pastViewModel.error.observe(viewLifecycleOwner) { error ->
            run {
                if (error != null) {
                    binding.noFinishedEvent.text = error.toString()
                    binding.noFinishedEvent.visibility = View.VISIBLE
                    binding.finishedRV.visibility = View.INVISIBLE
                }
            }
        }
        pastViewModel.pastEvent.observe(viewLifecycleOwner) {
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
                    binding.finishedRV.adapter = ListPastEventAdapter(pastEvent, requireContext())
                }
            }
        }
        pastViewModel.loadAPastEvents()
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                pastViewModel.loadSearchEvents(query ?: "")
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                pastViewModel.loadSearchEvents(newText ?: "")
                return true
            }
        })
        return binding.root
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment FinishedFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            FinishedFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}