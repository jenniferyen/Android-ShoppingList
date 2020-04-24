package com.example.shoppinglist

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import androidx.fragment.app.DialogFragment
import com.example.shoppinglist.data.Item
import kotlinx.android.synthetic.main.item_dialog.view.*

class ItemDialog : DialogFragment() {

    companion object {
        const val KEY_ITEM = "KEY_ITEM"
    }

    interface ItemHandler {
        fun itemAdded(item: Item)
        fun itemUpdated(item: Item)
    }

    private lateinit var itemHandler: ItemHandler

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (context is ItemHandler) {
            itemHandler = context
        } else {
            throw RuntimeException("Activity does not implement ItemHandler interface")
        }
    }

    private lateinit var etName: EditText
    private lateinit var etDesc: EditText
    private lateinit var etEstPrice: EditText
    private lateinit var spCategory: Spinner

    private var editMode = false

    @SuppressLint("InflateParams")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireContext())

        builder.setTitle("Add new item")
        val dialogView: View = requireActivity().layoutInflater.inflate(
            R.layout.item_dialog, null
        )

        etName = dialogView.etName
        etDesc = dialogView.etDesc
        etEstPrice = dialogView.etEstPrice
        spCategory = dialogView.spCategory

        builder.setView(dialogView)

        editMode = ((arguments != null) && arguments!!.containsKey(KEY_ITEM))

        if (editMode) {
            val item: Item = (arguments?.getSerializable(KEY_ITEM) as Item)
            etName.setText(item.name)
            etDesc.setText(item.itemDesc)
            etEstPrice.setText(item.estPrice)
            spCategory.setSelection(item.category)
        }

        builder.setPositiveButton("OK") { _, _ ->
        }

        return builder.create()
    }

    override fun onResume() {
        super.onResume()

        val positiveButton: Button = (dialog as AlertDialog).getButton(Dialog.BUTTON_POSITIVE)
        positiveButton.setOnClickListener {
            if (etName.text.isNotEmpty()) {
                if (editMode) {
                    handleItemEdit()
                } else {
                    handleItemCreate()
                }

                dialog!!.dismiss()
            } else {
                etName.error = "This field cannot be empty"
            }
        }
    }

    private fun handleItemCreate() {
        itemHandler.itemAdded(
            Item(
                null,
                etName.text.toString(),
                spCategory.selectedItemPosition,
                false,
                etDesc.text.toString(),
                etEstPrice.text.toString()
            )
        )
    }

    private fun handleItemEdit() {
        val itemToEdit: Item = arguments?.getSerializable(KEY_ITEM) as Item
        itemToEdit.name = etName.text.toString()
        itemToEdit.category = spCategory.selectedItemPosition
        itemToEdit.itemDesc = etDesc.text.toString()
        itemToEdit.estPrice = etEstPrice.text.toString()

        itemHandler.itemUpdated(itemToEdit)
    }

}