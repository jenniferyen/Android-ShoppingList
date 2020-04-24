package com.example.shoppinglist

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.shoppinglist.adapter.ShoppingListAdapter
import com.example.shoppinglist.data.AppDatabase
import com.example.shoppinglist.data.Item
import kotlinx.android.synthetic.main.activity_main.*

class ShoppingActivity : AppCompatActivity(), ItemDialog.ItemHandler {

    companion object {
        const val KEY_ITEM = "KEY_ITEM"
        const val TAG_ITEM_DIALOG = "TAG_ITEM_DIALOG"
    }

    private lateinit var shoppingListAdapter: ShoppingListAdapter

    private var editIndex: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        initRecyclerView()

        fabUnhidePurchased.setOnClickListener {
            initRecyclerView()
        }
        fabHidePurchased.setOnClickListener {
            hidePurchased()
        }
        fabDeleteAll.setOnClickListener {
            shoppingListAdapter.deleteAll()
        }
        fabAddItem.setOnClickListener {
            showAddItemDialog()
        }
    }

    private fun initRecyclerView() {
        Thread {
            val shoppingList = AppDatabase.getInstance(this).shoppingListDAO().getAllItems()
            runOnUiThread {
                shoppingListAdapter = ShoppingListAdapter(this, shoppingList)
                recyclerShoppingList.adapter = shoppingListAdapter
            }
        }.start()
    }

    private fun hidePurchased() {
        Thread {
            val shoppingList = AppDatabase.getInstance(this).shoppingListDAO().hidePurchased()
            runOnUiThread {
                shoppingListAdapter = ShoppingListAdapter(this, shoppingList)
                recyclerShoppingList.adapter = shoppingListAdapter
            }
        }.start()
    }

    private fun showAddItemDialog() {
        ItemDialog().show(supportFragmentManager, TAG_ITEM_DIALOG)
    }

    fun showEditItemDialog(itemToEdit: Item, index: Int) {
        editIndex = index
        val editDialog = ItemDialog()

        val bundle = Bundle()
        bundle.putSerializable(KEY_ITEM, itemToEdit)

        editDialog.arguments = bundle
        editDialog.show(supportFragmentManager, TAG_ITEM_DIALOG)
    }

    override fun itemAdded(item: Item) {
        Thread {
            item.itemId = AppDatabase.getInstance(this).shoppingListDAO().addItem(item)
            runOnUiThread {
                shoppingListAdapter.addItem(item)
            }
        }.start()
    }

    override fun itemUpdated(item: Item) {
        Thread {
            AppDatabase.getInstance(this).shoppingListDAO().updateItem(item)
            runOnUiThread {
                shoppingListAdapter.updateItem(item, editIndex)
            }
        }.start()
    }

}
