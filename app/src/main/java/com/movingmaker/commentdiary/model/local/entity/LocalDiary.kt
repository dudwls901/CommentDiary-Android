package com.movingmaker.commentdiary.model.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class LocalDiary(
//    @PrimaryKey(autoGenerate = true) val id: Long?,
    @PrimaryKey val date: String,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "content") val content: String,
    @ColumnInfo(name = "delivery_yn") val deliveryYN: Char,
)