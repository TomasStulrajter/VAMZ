package com.example.fridrops

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import kotlinx.android.synthetic.main.fragment_training_translate_image.*

/**
 * fragment predstavujuci ulohu, kde uzivatel musi po anglicky napisat, co vidi na obrazku
 */
class TrainingTranslateImageFragment : Fragment() {

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
        return inflater.inflate(R.layout.fragment_training_translate_image, container, false)
    }

    /**
     * pouzita na nastavenie obrazku do image viewu fragmentu
     */
    override fun onStart() {
        super.onStart()
        val address = requireArguments().getString("address")

        picture.setImageURI(Uri.parse(address))
        picture.layoutParams.height = 400
        picture.layoutParams.width = 400

        confirm_button.setOnClickListener {
            //Toast.makeText(requireContext(), "I have entered the onclicklistener method!", Toast.LENGTH_SHORT).show()
            checkAnswer()
        }
    }

    /**
     * kontroluje spravnost uzivatelovej odpovede
     */
    private fun checkAnswer() {
        val right_answer = requireArguments().getString("answer")
        val user_answer = answer_field.text.toString()
        setFragmentResult("result_key", bundleOf("result" to (user_answer == right_answer), "answer" to right_answer))
    }
}