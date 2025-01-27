package com.dennyoctavian.dicodingevent.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.dennyoctavian.dicodingevent.R
import com.dennyoctavian.dicodingevent.adapters.ListPastEventAdapter
import com.dennyoctavian.dicodingevent.database.EventApp
import com.dennyoctavian.dicodingevent.databinding.FragmentFavoriteBinding
import com.dennyoctavian.dicodingevent.databinding.FragmentFinishedBinding
import kotlinx.coroutines.launch

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [FavoriteFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class FavoriteFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private lateinit var binding: FragmentFavoriteBinding
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
        binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        val eventDao = (requireActivity().application as EventApp).db.eventDao()
        lifecycleScope.launch {
            try {
                binding.favoriteLoading.visibility = View.VISIBLE
                binding.favoriteRV.visibility = View.INVISIBLE
                eventDao.getAllEventsSaved().collect { events ->
                    if (events.isNotEmpty()) {
                        binding.favoriteRV.layoutManager = LinearLayoutManager(context)
                        binding.favoriteRV.adapter = ListPastEventAdapter(events, requireContext())
                        binding.favoriteRV.visibility = View.VISIBLE
                        binding.favoriteLoading.visibility = View.GONE
                    } else {
                        binding.favoriteLoading.visibility = View.GONE
                        binding.noFavoriteEvent.visibility = View.VISIBLE
                        binding.favoriteRV.visibility = View.GONE
                    }
                }
            } catch (e: Exception) {
                binding.favoriteLoading.visibility = View.GONE
                binding.favoriteRV.visibility = View.GONE
                binding.noFavoriteEvent.visibility = View.VISIBLE
                binding.noFavoriteEvent.text = "An error occurred: ${e.message}"
            }
        }
        return binding.root
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment FavoriteFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            FavoriteFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}