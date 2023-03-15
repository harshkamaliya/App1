package com.example.app1

import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.app1.Database.DataBaseTask
import com.example.app1.Database.TaskDao
import com.example.app1.Database.TaskEntity
import com.example.app1.adapter.AdapterTask
import com.example.app1.databinding.ActivityDisplayBinding
import kotlinx.android.synthetic.main.item_layout.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ActivityDisplay : AppCompatActivity(), OnItemClicked {
    private val task = mutableListOf<TaskEntity>()
    private lateinit var binding: ActivityDisplayBinding
    private lateinit var dataBase: DataBaseTask
    private lateinit var dataAccessObject: TaskDao
    lateinit var newAdapter: AdapterTask


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDisplayBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        dataBase = DataBaseTask.getDataBase(this)
        dataAccessObject = dataBase.getTaskDao()
        newAdapter = AdapterTask(task, this)
        binding.rv.layoutManager = LinearLayoutManager(this)
        binding.rv.adapter = newAdapter


        dataAccessObject.getTask().observe(this, Observer {
            if (it.isNullOrEmpty()) {
                binding.tvNoData.visibility = View.VISIBLE
                binding.rv.visibility = View.GONE
            } else {
                binding.tvNoData.visibility = View.GONE
                binding.rv.visibility = View.VISIBLE
            }
            val newTask = it
            task.clear()
            task.addAll(newTask)
            newAdapter.notifyDataSetChanged()


        })


        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {

                TODO("Not yet implemented")
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val item = task.get(viewHolder.adapterPosition)
                task.removeAt(viewHolder.adapterPosition)
                newAdapter.notifyItemRemoved(viewHolder.adapterPosition)
                CoroutineScope(Dispatchers.IO).launch {
                    dataAccessObject.deleteTask(item)

                }

                Toast.makeText(this@ActivityDisplay, "Delete", Toast.LENGTH_SHORT).show()
            }

        }).attachToRecyclerView(binding.rv)


        binding.btnDownload.setOnClickListener {

            downloadPdf()
        }


    }

    private fun checkItem() {
        if (task.isEmpty()) {
            binding.tvNoData.visibility = View.VISIBLE
            binding.rv.visibility = View.GONE

        } else {
            binding.tvNoData.visibility = View.GONE
            binding.rv.visibility = View.VISIBLE
        }

    }

    override fun onResume() {
        super.onResume()
        checkItem()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        getMenuInflater().inflate(R.menu.menu_main, menu)
        return true

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.miCompose -> {
                launchFragment()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun launchFragment() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)

    }

    override fun onEditClick(taskEntity: TaskEntity) {
        ivEdit.setOnClickListener {
            val intent = Intent(this, UpdateActivity::class.java)
            intent.putExtra("id", taskEntity.id)
            startActivity(intent)

        }
    }

    override fun onDeleteClick(taskEntity: TaskEntity) {
        ivDelete.setOnClickListener {

            CoroutineScope(Dispatchers.IO).launch {
                dataAccessObject.deleteTask(taskEntity)

            }

        }
    }

    override fun onItemClick(view: View) {
        Toast.makeText(this, "Item Clicked", Toast.LENGTH_SHORT).show()

    }

    private fun downloadPdf() {
       // val url = "https://drive.google.com/file/d/1BfcnaeV9xWpvmvUJ6xoQm3YuPEdGFjGZ/view?usp=drivesdk"
        val url = "https://www.fda.gov/media/150386/download"

        val request = DownloadManager.Request(Uri.parse(url))
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
        request.setTitle("Sample FDA PDF")
        request.setDescription("Downloading")
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "new_directory/sample.pdf")

        val downloadManager = getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        downloadManager.enqueue(request)
    }


}