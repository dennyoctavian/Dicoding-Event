package com.dennyoctavian.dicodingevent

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.dennyoctavian.dicodingevent.database.EventApp
import com.dennyoctavian.dicodingevent.databinding.ActivityDetailEventBinding
import com.dennyoctavian.dicodingevent.helpers.Helper
import com.dennyoctavian.dicodingevent.models.Event
import com.dennyoctavian.dicodingevent.viewmodels.DetailEventViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


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
                    binding.startTime.text = "Jam Acara :  ${Helper.formatDate(it.beginTime)}"
                    binding.quota.text ="Sisa Kuota: ${it.quota - it.registrants}"
                    val descriptionText = Html.fromHtml(it.description, Html.FROM_HTML_MODE_LEGACY)
                    binding.description.text = descriptionText
                    binding.gotoEvent.setOnClickListener { _ ->
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(it.link))
                        startActivity(intent)
                    }
                    val eventDao = (application as EventApp).db.eventDao()
                    lifecycleScope.launch {
                        val event = eventDao.getEventById(it.id.toString())
                        if (event != null) {
                            binding.saveButton.setImageResource(R.drawable.ic_favorite)
                        }else {
                            binding.saveButton.setImageResource(R.drawable.ic_favorite_outline)
                        }
                    }

                    binding.saveButton.setOnClickListener { _ ->
                        CoroutineScope(Dispatchers.IO).launch {
                            val event = eventDao.getEventById(it.id.toString())
                            if(event != null) {
                                eventDao.delete(it)
                                runOnUiThread {
                                    binding.saveButton.setImageResource(R.drawable.ic_favorite_outline)
                                    Toast.makeText(applicationContext, "Berhasil Menghapus Event dari Favorit", Toast.LENGTH_LONG).show()
                                }
                                return@launch
                            }else {
                                eventDao.insert(it)
                                runOnUiThread {
                                    binding.saveButton.setImageResource(R.drawable.ic_favorite)
                                    Toast.makeText(applicationContext, "Berhasil Menambahkan Event ke Favorit", Toast.LENGTH_LONG).show()
                                }
                                return@launch
                            }

                        }
                    }
                }
            }

        } else {
            Log.v("ERROR", "Event is null")
        }
    }


}