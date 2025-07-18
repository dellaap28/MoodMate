package com.example.moodmate

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.moodmate.database.DBHelper
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices


class AddDiaryActivity : AppCompatActivity() {
    private lateinit var dbHelper: DBHelper
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var tvLocation: TextView
    private var latitude: String = "Unknown"
    private var longitude: String = "Unknown"

    private fun getLastLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                100
            )
            return
        }

        fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
            location?.let {
                latitude = it.latitude.toString()
                longitude = it.longitude.toString()
                tvLocation.text = "Lokasi: $latitude, $longitude"
            } ?: run {
                tvLocation.text = "Lokasi tidak tersedia"
            }
        }.addOnFailureListener {
            tvLocation.text = "Gagal mengambil lokasi"
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 100 && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            getLastLocation()
        } else {
            Toast.makeText(this, "Izin lokasi diperlukan", Toast.LENGTH_SHORT).show()
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_diary)

        dbHelper = DBHelper(this)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)


        val etDate = findViewById<EditText>(R.id.etDate)
        val etMood = findViewById<EditText>(R.id.etMood)
        val etNote = findViewById<EditText>(R.id.etNote)
        tvLocation = findViewById(R.id.tvLocation)
        val btnSave = findViewById<Button>(R.id.btnSaveDiary)

        getLastLocation() // Coba ambil lokasi

        btnSave.setOnClickListener {
            val date = etDate.text.toString().trim()
            val mood = etMood.text.toString().trim()
            val note = etNote.text.toString().trim()

            if (date.isNotEmpty() && mood.isNotEmpty() && note.isNotEmpty()) {
                // Coba update lokasi terbaru sebelum menyimpan
                getLastLocation()

                val success = dbHelper.insertDiary(date, mood, note, latitude, longitude)
                if (success) {
                    Toast.makeText(this, "Diary saved with location!", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(this, "Failed to save diary.", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Isi semua data!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}