package com.example.shoppinglist.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.example.shoppinglist.R
import com.example.shoppinglist.ShoppingActivity
import com.example.shoppinglist.adapter.ShoppingListAdapter.ViewHolder
import com.example.shoppinglist.data.AppDatabase
import com.example.shoppinglist.data.Item
import kotlinx.android.synthetic.main.item_layout.view.*

class ShoppingListAdapter(
    private val context: Context,
    shoppingListItems: List<Item>
) : RecyclerView.Adapter<ViewHolder>() {

    private val shoppingList: MutableList<Item> = mutableListOf()

    init {
        shoppingList.addAll(shoppingListItems)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemRow = LayoutInflater.from(context).inflate(
            R.layout.item_layout, parent, false
        )
        return ViewHolder(itemRow)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item: Item = shoppingList[holder.adapterPosition]

        bindProperties(holder, item)
        bindIcons(item, holder)

        holder.cbBought.setOnClickListener {
            item.bought = holder.cbBought.isChecked
            Thread {
                AppDatabase.getInstance(context).shoppingListDAO().updateItem(item)
            }.start()
        }
        holder.btnEdit.setOnClickListener {
            (context as ShoppingActivity).showEditItemDialog(item, holder.adapterPosition)
        }
        holder.btnDelete.setOnClickListener {
            deleteItem(holder.adapterPosition)
        }
    }

    override fun getItemCount(): Int {
        return shoppingList.size
    }

    private fun bindProperties(holder: ViewHolder, item: Item) {
        holder.tvName.text = item.name
        holder.tvDesc.text = item.itemDesc
        holder.tvEstPrice.text = context.getString(R.string.tv_estPrice, item.estPrice)
        holder.cbBought.isChecked = item.bought
    }

    private fun bindIcons(item: Item, holder: ViewHolder) {
        when (item.category) {
            0 -> {
                holder.spCategory.setImageResource(R.drawable.food)
            }
            1 -> {
                holder.spCategory.setImageResource(R.drawable.clothing)
            }
            2 -> {
                holder.spCategory.setImageResource(R.drawable.toiletries)
            }
            3 -> {
                holder.spCategory.setImageResource(R.drawable.electronics)
            }
            else -> {
                holder.spCategory.setImageResource(R.drawable.other)
            }
        }
    }

    private fun deleteItem(index: Int) {
        Thread {
            AppDatabase.getInstance(context).shoppingListDAO().deleteItem(
                shoppingList[index]
            )
            (context as ShoppingActivity).runOnUiThread {
                shoppingList.removeAt(index)
                notifyItemRemoved(index)
            }
        }.start()
    }

    fun deleteAll() {
        Thread {
            AppDatabase.getInstance(context).shoppingListDAO().deleteAll()
            (context as ShoppingActivity).runOnUiThread {
                shoppingList.clear()
                notifyDataSetChanged()
            }
        }.start()
    }

    fun addItem(item: Item) {
        shoppingList.add(item)
        notifyItemInserted(shoppingList.lastIndex)
    }

    fun updateItem(item: Item, editIndex: Int) {
        shoppingList[editIndex] = item
        notifyItemChanged(editIndex)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvName: TextView = itemView.tvName
        val tvDesc: TextView = itemView.tvDesc
        val spCategory: ImageView = itemView.ivImage
        val tvEstPrice: TextView = itemView.tvEstPrice
        val cbBought: CheckBox = itemView.cbBought

        val btnDelete: Button = itemView.btnDelete
        val btnEdit: Button = itemView.btnEdit
    }

}