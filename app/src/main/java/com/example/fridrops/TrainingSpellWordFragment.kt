package com.example.fridrops

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import kotlinx.android.synthetic.main.fragment_training_spell_word.*
import kotlinx.android.synthetic.main.fragment_training_translate_image.*
import kotlinx.android.synthetic.main.fragment_training_translate_image.confirm_button
import kotlinx.android.synthetic.main.fragment_training_translate_image.picture
import kotlin.random.Random

/**
 * fragment predstavujuci ulohu, kde uzivatel postupne vybera nasledujuce pisemno slova z ponukanych moznosti
 * @property answer slovo, ktore je potrebne vyhlaskovat
 * @property button_list zoznam tlacidiel pre ponukane pismena
 * @property letters zoznam pismen anglickej abecedy
 * @property current_letter index aktualne hadaneho pismena v ramci hlaskovaneho slova
 * @property errors pocita chyby, ktorych sa uzivatel dospustil v ramci hadania nasledujucuch pismen
 */
class TrainingSpellWordFragment : Fragment() {

    private var answer: String? = ""
    private val button_list = mutableListOf<Button>()
    private val letters = listOf<String>("a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "x", "y", "z")
    private var current_letter = 0
    private var errors = 0

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
        return inflater.inflate(R.layout.fragment_training_spell_word, container, false)
    }

    /**
     * pouzita na vygenerovanie pociatocnej ponuky pismen na zvolenie
     */
    override fun onStart() {
        super.onStart()
        answer = requireArguments().getString("answer")
        current_letter

        button_list.add(letter_1)
        button_list.add(letter_2)
        button_list.add(letter_3)
        button_list.add(letter_4)
        button_list.add(letter_5)

        val address = requireArguments().getString("address")
        picture.setImageURI(Uri.parse(address))
        picture.layoutParams.height = 500
        picture.layoutParams.width = 500

        populateLetters()

        for (i in button_list.indices) {
            button_list[i].setOnClickListener { checkAnswer(button_list[i]) }
        }
    }

    /**
     * vygeneruje ponuku pismen pre dalsie hadane pismeno
     */
    private fun populateLetters() {
        val letter_picker = (letters.indices).shuffled().take(5)
        for (i in button_list.indices) {
            button_list[i].text = letters[letter_picker[i]]
        }

        val index_to_replace = Random.nextInt(0, button_list.size)
        button_list[index_to_replace].text = answer?.get(current_letter).toString()
    }

    /**
     * kontroluje, ci uzivatel zvolil spravne pismeno. Ak ano, vygeneruje dalsiu paticu, inak mu odpocita zivot
     */
    private fun checkAnswer(button: Button) {
        if (button.text == answer?.get(current_letter).toString()) {
            current_letter++
            if (current_letter == answer?.length) {
                setFragmentResult("result_key", bundleOf("result" to true, "answer" to ""))
            } else {
                error_notice.text = "Životy : ${3 - errors}"
                populateLetters()
            }
        } else {
            errors++
            if (errors == 3) {
                setFragmentResult("result_key", bundleOf("result" to false, "answer" to ""))
            } else {
                error_notice.text = "Nesprávne písmeno! Životy : ${3 - errors}"
            }

        }
    }

}