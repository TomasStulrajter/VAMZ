package com.example.fridrops

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import kotlinx.android.synthetic.main.fragment_training_choose_picture.*
import kotlin.random.Random

/**
 * fragment predstavujuci ulohu, kde sa k slovu priraduje spravny obrazok - na vyber je z 2 moznosti
 * @property correct_picture spravny obrazok, ktory ked uzivatel vyberie, tak odpovedal spravne
 * @property button_list zoznam vsetkych tlacidiel fragmentu
 * @property occupied_buttons zoznam tlacidiel, ktorym uz bol priradeny obrazok
 */
class TrainingChoosePictureTwoFragment : Fragment() {

    private var correct_picture : String? = ""
    private val button_list = mutableListOf<ImageButton>()
    private var occupied_buttons = arrayOf<Int>(0, 0)

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
        return inflater.inflate(R.layout.fragment_training_choose_picture_two, container, false)
    }

    /**
     * pouzita na priradenie obrazkov k tlacidlam
     */
    override fun onStart() {
        super.onStart()

        button_list.add(view_other_picture_1)
        button_list.add(view_other_picture_2)

        val correct_word = requireArguments().getString("correct_word")
        val correct_picture = requireArguments().getString("correct_picture")

        val other_picture_1 = requireArguments().getString("other_picture_1")

        this.correct_picture = correct_picture
        view_word_to_guess.text = correct_word

        //priradenie slov tlacidlam
        mapPictureToButton(correct_picture)
        mapPictureToButton(other_picture_1)

        for (i in button_list.indices) {
            button_list[i].setOnClickListener { pictureClicked(button_list[i]) }
        }
    }

    /**
     * metoda priradena kazdemu tlacidlu - po stlaceni sa ohodnoti, ci bolo stlacene spravne tlacidlo a vysledok sa odosle treningovej aktivite
     * @param button tlacidlo, ktoremu sa priraduje tato metoda
     */
    private fun pictureClicked(button: ImageButton) {
        setFragmentResult("result_key", bundleOf("result" to (button.tag == correct_picture), "answer" to ""))
    }

    /**
     * namapuje obrazok na tlacidlo
     * @param picture URI daneho obrazku
     */
    private fun mapPictureToButton(picture: String?) {
        var index = Random.nextInt(2)
        while (occupied_buttons[index] != 0) {
            index = Random.nextInt(2)
        }
        button_list[index].setImageURI(Uri.parse(picture))
        button_list[index].tag = picture
        button_list[index].layoutParams.height = 400
        button_list[index].layoutParams.width = 400

        occupied_buttons[index] = 1
    }
}