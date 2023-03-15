package com.example.app1

import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.app1.Database.DataBaseTask
import com.example.app1.Database.TaskDao
import com.example.app1.Database.TaskEntity
import com.example.app1.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var taskEntity: TaskEntity
    private lateinit var dataBase: DataBaseTask
    private lateinit var dataAccessObject: TaskDao
    val books = arrayOf("Book A", "Book B", "Book C")
    val phoneNumberPattern = "^[+]?[0-9]{10,13}\$".toRegex()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val arrayAdapter = ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, books)

        dataBase = DataBaseTask.getDataBase(this)
        dataAccessObject = dataBase.getTaskDao()
        binding.bookSpinner.adapter = arrayAdapter
        var book: String? = null
        binding.bookSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
                book = books[position].toString()

            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

        }


        binding.addButton.setOnClickListener {
            val name = binding.nameInput.toString()


            val firstName = binding.nameInput.text.toString()
            val phoneNo = binding.phoneInput.text.toString()
            val newBook = book ?: return@setOnClickListener

            val fNValidity = isValidName(firstName)
            val pNValidity = isValidPhoneNumber(phoneNo)


            if (!pNValidity) {
                binding.phoneInput.error = "Invalid phone number"
            }
            if (!fNValidity) {
                binding.nameInput.error = "please enter your full Name"
            }

            if (!firstName.isNullOrEmpty() && pNValidity && !newBook.isNullOrEmpty()) {
                val task = TaskEntity(firstName, phoneNo, newBook)
                CoroutineScope(Dispatchers.IO).launch {
                    dataAccessObject.insertTask(task)
                }


                Toast.makeText(this@MainActivity, "Saved", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, ActivityDisplay::class.java)
                startActivity(intent)

            } else {
                Toast.makeText(this@MainActivity, "Data Missing", Toast.LENGTH_SHORT).show()

            }


        }



    }

    private fun isValidName(firstName: String): Boolean {
        if (firstName.isNotEmpty()) {
            return true
        }
        return false
    }

    fun isValidPhoneNumber(phoneNumber: String): Boolean {
        return phoneNumberPattern.matches(phoneNumber)
    }



}