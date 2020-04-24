package com.example.shoppinglist.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "items")
data class Item(
    @PrimaryKey(autoGenerate = true) var itemId: Long?,
    @ColumnInfo(name = "name") var name: String,
    @ColumnInfo(name = "category") var category: Int,
    @ColumnInfo(name = "bought") var bought: Boolean,
    @ColumnInfo(name = "itemDesc") var itemDesc: String,
    @ColumnInfo(name = "estPrice") var estPrice: String
) : Serializable