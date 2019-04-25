package ru.debian17.findme.data.model.db.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class CategoryModel(
    @PrimaryKey
    val id: Int,
    val name: String,
    val isPoint: Boolean,
    val isLong: Boolean
)