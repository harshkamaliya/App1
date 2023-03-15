package com.example.app1.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.app1.Database.TaskEntity
import com.example.app1.OnItemClicked
import com.example.app1.R


class AdapterTask(private val repo: List<TaskEntity>, private val listener: OnItemClicked):RecyclerView.Adapter<ViewHolderTask>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderTask {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_layout, parent, false)
        return ViewHolderTask(view,listener)
    }

    override fun onBindViewHolder(holder: ViewHolderTask, position: Int) {
        holder.setData(repo[position])

    }

    override fun getItemCount(): Int {
        return repo.size
    }
}