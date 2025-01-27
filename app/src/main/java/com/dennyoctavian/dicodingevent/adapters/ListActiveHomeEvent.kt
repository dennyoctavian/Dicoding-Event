package com.dennyoctavian.dicodingevent.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dennyoctavian.dicodingevent.DetailEventActivity
import com.dennyoctavian.dicodingevent.databinding.ItemEventUpcomingBinding
import com.dennyoctavian.dicodingevent.models.Event

class ListActiveHomeEventAdapter(private val listEvent: List<Event>, private val context: Context) :
    RecyclerView.Adapter<ListActiveHomeEventAdapter.ListActiveHomeEventViewHolder>() {
    inner class ListActiveHomeEventViewHolder(val binding: ItemEventUpcomingBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListActiveHomeEventViewHolder {
        val binding =
            ItemEventUpcomingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListActiveHomeEventViewHolder(binding)
    }

    override fun getItemCount(): Int = listEvent.size

    override fun onBindViewHolder(holder: ListActiveHomeEventViewHolder, position: Int) {
        val item = listEvent[position]
        holder.binding.apply {
            Glide.with(context)
                .load(item.mediaCover)
                .centerCrop()
                .placeholder(android.R.color.darker_gray)
                .error(android.R.color.holo_red_dark)
                .into(imageView)
            title.text = item.name
        }
        holder.itemView.setOnClickListener {
            val intent = Intent(context, DetailEventActivity::class.java)
            intent.putExtra("event", item)
            context.startActivity(intent)
        }
    }
}