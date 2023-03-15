package com.example.app1.Database

import androidx.lifecycle.LiveData
import androidx.room.*


@Dao
interface TaskDao {


    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertTask(taskEntity: TaskEntity)

    @Query("Select * from task_table")
    fun getTask(): LiveData<List<TaskEntity>>

    @Delete
    fun deleteTask(taskEntity: TaskEntity)

    @Update
    fun updateTask(taskEntity: TaskEntity)


    @Query("SELECT * FROM task_table WHERE id = :id")
    fun getById(id: Int): TaskEntity


}