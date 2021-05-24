package com.example.fridrops

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult

/**
 * fragment reprezentujuci dialogove okno, ktore sluzi na vyber kategorie slov, ktore chce uzivatel trenovat
 */
class CategoryPickFragment : DialogFragment() {

    /**
     * override metody predka
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    /**
     * override metody predka
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_category_pick, container, false)
    }

    /**
     * override metody predka - inicializacia alert dialogu pre vyber kategorie
     */
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val category_list = requireArguments().getStringArray("category_list")
        return activity?.let {
            val builder = AlertDialog.Builder(it, R.style.dialogStyle)
            builder.setTitle(R.string.category_pick)
                .setItems(category_list,
                    DialogInterface.OnClickListener { _, index ->
                        setFragmentResult("category_key", bundleOf("result" to index))
                    })
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

}