package com.example.app1

import android.view.View
import com.example.app1.Database.TaskEntity

interface OnItemClicked {

    fun onEditClick(taskEntity: TaskEntity)
    fun onDeleteClick(taskEntity: TaskEntity)
    fun onItemClick(view:View)
}