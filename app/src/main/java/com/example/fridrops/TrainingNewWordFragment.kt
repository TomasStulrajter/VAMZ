package com.example.fridrops

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import kotlinx.android.synthetic.main.fragment_training_new_word.*
import kotlinx.android.synthetic.main.fragment_training_translate_image.*

/**
 * fragment predstavujuci situaciu, ked sa uzivatelovi predstavi nove slovo na zapamatanie
 */
class TrainingNewWordFragment : Fragment() {

    /**
     * override metody predka
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    /**
     * zaciatok zivotneho cyklu
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_training_new_word, container, false)
    }

    /**
     * priradenie dat z trenovacej aktivity do jednotlivych viewov fragmentu
     */
    override fun onStart() {
        super.onStart()

        val address = requireArguments().getString("address")
        new_picture.setImageURI(Uri.parse(address))
        new_picture.layoutParams.height = 500
        new_picture.layoutParams.width = 500

        new_word.text = requireArguments().getString("word")
        new_translation.text = requireArguments().getString("translation")

        learn_button.setOnClickListener {
            learnWord()
        }
    }

    /**
     * ukoncuje fragment a oznamuje treningovej aktivite naucenie noveho slova
     */
    private fun learnWord() {
        setFragmentResult("new_word_key", bundleOf("result" to true))
    }

}