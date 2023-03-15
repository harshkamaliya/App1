package com.example.app1.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.app1.Database.TaskEntity
import com.example.app1.OnItemClicked
import kotlinx.android.synthetic.main.item_layout.view.*


class ViewHolderTask(private val view: View, private val listener: OnItemClicked):RecyclerView.ViewHolder(view) {

    fun setData(taskEntity: TaskEntity) {


        view.apply {

            tvName.text =taskEntity.firstName.toString()
            tvPhoneNumber.text =taskEntity.phoneNumber.toString()
            tvBookSelected.text=taskEntity.book.toString()


            ivEdit.setOnClickListener {
                listener.onEditClick(taskEntity)
            }

            ivDelete.setOnClickListener {
                listener.onDeleteClick(taskEntity)
            }

//            cardView.setOnClickListener {
//                listener.onItemClick(view)
//            }


        }
    }



}