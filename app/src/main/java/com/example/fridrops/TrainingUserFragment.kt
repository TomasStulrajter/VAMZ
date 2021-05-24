package com.example.fridrops

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import kotlinx.android.synthetic.main.fragment_training_choose_picture.*
import kotlinx.android.synthetic.main.fragment_training_translate_image.*
import kotlinx.android.synthetic.main.fragment_training_user.*

/**
 * fragment predstavujuci ulohu, kde uzivatel preklada zadane slovo do anglictiny - tato uloha sa pouziva v pripade, ze uzivatel trenuje slova z vlastnej kategorie
 */
class TrainingUserFragment : Fragment() {

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
        return inflater.inflate(R.layout.fragment_training_user, container, false)
    }

    /**
     * pouzita na nastavenie hodnot poslanych z treningovej aktivity do viewov vlastnenych fragmentom
     */
    override fun onStart() {
        super.onStart()

        val correct_word = requireArguments().getString("correct_word")
        val correct_translation = requireArguments().getString("correct_translation")

        word_to_translate.text = correct_word

        user_confirm_button.setOnClickListener {
            //Toast.makeText(requireContext(), "I have entered the onclicklistener method!", Toast.LENGTH_SHORT).show()
            checkAnswer()
        }

    }

    /**
     * kontroluje spravnost uzivatelovej odpovede
     */
    private fun checkAnswer() {
        val right_answer = requireArguments().getString("correct_translation")
        val user_answer = user_answer_field.text.toString()
        setFragmentResult("result_key", bundleOf("result" to (user_answer == right_answer), "answer" to right_answer))
    }
}