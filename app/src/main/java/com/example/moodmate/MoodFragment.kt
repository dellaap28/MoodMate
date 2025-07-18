package com.example.moodmate

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import android.content.Intent
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.moodmate.database.DBHelper

class MoodFragment : Fragment() {
    //private lateinit var dbHelper: DBHelper

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_mood, container, false)


        val btnAddDiary = view.findViewById<Button>(R.id.btnAddDiary)
        btnAddDiary.setOnClickListener {
            val intent = Intent(requireContext(), AddDiaryActivity::class.java)
            startActivity(intent)
        }

        //dbHelper = DBHelper(requireContext())

        //val etDate = view.findViewById<EditText>(R.id.etDate)
        //val etMood = view.findViewById<EditText>(R.id.etMood)
        //val etNote = view.findViewById<EditText>(R.id.etNote)
        //val btnSave = view.findViewById<Button>(R.id.btnSave)

        //btnSave.setOnClickListener {
            //val date = etDate.text.toString().trim()
            //val mood = etMood.text.toString().trim()
            //val note = etNote.text.toString().trim()

            //if (date.isNotEmpty() && mood.isNotEmpty() && note.isNotEmpty()) {
                //val success = dbHelper.insertDiary(date, mood, note)
                //if (success) {
                    //Toast.makeText(context, "Diary saved!", Toast.LENGTH_SHORT).show()
                    //etDate.text.clear()
                    //etMood.text.clear()
                    //etNote.text.clear()
                //} else {
                    //Toast.makeText(context, "Failed to save diary.", Toast.LENGTH_SHORT).show()
                //}
            //} else {
                //Toast.makeText(context, "All fields are required!", Toast.LENGTH_SHORT).show()
            //}
        //}

        return view
    }

    override fun onResume() {
        super.onResume()
        loadDiaryList()
    }

    private fun loadDiaryList() {
        val dbHelper = DBHelper(requireContext())
        val diaryList = dbHelper.getAllDiary() // Buat fungsi ini di DBHelper

        val listView = view?.findViewById<ListView>(R.id.listDiary)
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, diaryList)
        listView?.adapter = adapter
    }

}