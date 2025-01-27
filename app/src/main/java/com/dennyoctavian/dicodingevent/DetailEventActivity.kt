package com.dennyoctavian.dicodingevent

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.dennyoctavian.dicodingevent.databinding.ActivityDetailEventBinding
import com.dennyoctavian.dicodingevent.models.Event
import com.dennyoctavian.dicodingevent.viewmodels.DetailEventViewModel
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale


class DetailEventActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailEventBinding

    @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityDetailEventBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        var event: Event? = null

        if (intent.hasExtra("event")) {
            event = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                intent.getParcelableExtra(
                    "event",
                    Event::class.java
                )
            } else {
                @Suppress("DEPRECATION")
                intent.getParcelableExtra("event") as? Event
            }
        }

        if (event != null) {
            val detailEventViewModel = ViewModelProvider(this)[DetailEventViewModel::class.java]
            detailEventViewModel.loadAPastEvents(event.id)

            detailEventViewModel.isLoading.observe(this) {
                isLoading ->
                if(isLoading) {
                    binding.loading.visibility = View.VISIBLE
                    binding.image.visibility = View.INVISIBLE
                    binding.title.visibility = View.INVISIBLE
                    binding.ownerName.visibility = View.INVISIBLE
                    binding.startTime.visibility = View.INVISIBLE
                    binding.quota.visibility = View.INVISIBLE
                    binding.description.visibility = View.INVISIBLE
                } else {
                    binding.loading.visibility = View.GONE
                    binding.image.visibility = View.VISIBLE
                    binding.title.visibility = View.VISIBLE
                    binding.ownerName.visibility = View.VISIBLE
                    binding.startTime.visibility = View.VISIBLE
                    binding.quota.visibility = View.VISIBLE
                    binding.description.visibility = View.VISIBLE
                }
            }

            detailEventViewModel.detailEvent.observe(this) {
                binding.apply {
                    setSupportActionBar(materialToolbar)
                    supportActionBar?.setDisplayHomeAsUpEnabled(true)
                    supportActionBar?.title = it.name
                    materialToolbar.setNavigationOnClickListener {
                        onBackPressedDispatcher.onBackPressed()
                    }
                    binding.title.text = it.name
                    Glide.with(baseContext)
                        .load(it.mediaCover)
                        .centerCrop()
                        .placeholder(android.R.color.darker_gray)
                        .error(android.R.color.holo_red_dark)
                        .into(binding.image)
                    binding.ownerName.text = it.ownerName
                    binding.startTime.text = "Jam Acara :  ${formatDate(it.beginTime)}" + "\n" + "Sisa Kuota : ${it.quota}"
                    val descriptionText = Html.fromHtml(it.description, Html.FROM_HTML_MODE_LEGACY)
                    binding.description.text = descriptionText
                    binding.gotoEvent.setOnClickListener { _ ->
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(it.link))
                        startActivity(intent)
                    }
                }
            }

        } else {
            Log.v("ERROR", "Event is null")
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun formatDate(indonesiaTime: String): String {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        val localDateTime = LocalDateTime.parse(indonesiaTime, formatter)

        val outputFormatter = DateTimeFormatter.ofPattern("d MMMM yyyy HH:mm", Locale("id", "ID"))
        return localDateTime.format(outputFormatter)
    }
}