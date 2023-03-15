package com.example.app1

import android.R
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.app1.Database.DataBaseTask
import com.example.app1.Database.TaskEntity
import com.example.app1.databinding.ActivityUpdateBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UpdateActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUpdateBinding
    val books = arrayOf("Book A", "Book B", "Book C")
    val phoneNumberPattern = "^[+]?[0-9]{10,13}\$".toRegex()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val dataBase = DataBaseTask.getDataBase(this)
        val dataAccessObject = dataBase.getTaskDao()

        val arrayAdapter = ArrayAdapter<String>(this, R.layout.simple_dropdown_item_1line, books)
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





        binding.updateButton.setOnClickListener {


            var task = TaskEntity("", "", "")
            task.id = intent.getIntExtra("id", 0)

            val firstName = binding.nameInput.text.toString()
            val phoneNo = binding.phoneInput.text.toString()
            val newBook = book ?: return@setOnClickListener

            task.firstName = firstName
            task.phoneNumber = phoneNo
            task.book = newBook

            val fNValidity = isValidName(firstName)
            val pNValidity = isValidPhoneNumber(phoneNo)

            if (!pNValidity) {
                binding.phoneInput.error = "Invalid phone number"
            }
            if (!fNValidity) {
                binding.nameInput.error = "please enter your full Name"
            }

            if (!firstName.isNullOrEmpty() && pNValidity && !newBook.isNullOrEmpty()) {
                CoroutineScope(Dispatchers.IO).launch {
                    dataAccessObject.updateTask(task)
                }

                Toast.makeText(this, "Updated", Toast.LENGTH_LONG).show()
                val intent = Intent(this, ActivityDisplay::class.java)
                startActivity(intent)

            } else {
                Toast.makeText(this@UpdateActivity, "Data Missing", Toast.LENGTH_SHORT).show()

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