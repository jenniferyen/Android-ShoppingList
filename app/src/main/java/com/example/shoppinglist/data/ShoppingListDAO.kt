package com.example.shoppinglist.data

import androidx.room.*

@Dao
interface ShoppingListDAO {

    @Query("SELECT * FROM items")
    fun getAllItems(): List<Item>

    @Insert
    fun addItem(item: Item): Long

    @Delete
    fun deleteItem(item: Item)

    @Update
    fun updateItem(item: Item)

    @Query("DELETE FROM items")
    fun deleteAll()

    @Query("SELECT * FROM items WHERE not bought")
    fun hidePurchased() : List<Item>
}