package com.example.fridrops

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.media.MediaPlayer
import android.os.Bundle
import android.os.CountDownTimer
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.fragment.app.add
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.activity_training.*
import kotlinx.android.synthetic.main.dictionary_item.*
import kotlinx.android.synthetic.main.fragment_training_connect.*
import kotlinx.coroutines.Dispatchers
import kotlin.random.Random

/**
 * aktitivta spravujuca priebeh treningu
 * @property dictionaryViewModel instancia view modelu
 * @property allWords zoznam vsetkych slov
 * @property not_yet_learned_words zoznam slov, ktore este neboli naucene
 * @property eligible_words zoznam naucenych slov vo zvolenej kategorii
 * @property categories zoznam dostupnych kategorii
 * @property picked_category uchovava kategoriu zvolenu uzivatelom na zaciatku treningu
 * @property max_possible_task obmedzuje, ktore typy treningovch uloh je mozne generovat vzhladom na pocet naucenych slov
 * @property wordCount pocet slov
 * @property userMode priznak indikujuci, ci si pouzivatel zvolil na trening svoju vlastnu kategoriu
 * @property current_words zoznam testovanych slov v ramci aktualnej ulohy
 * @property completed_tasks pocet doteraz riesenych uloh
 * @property correct_tasks pocet doteraz splnenych uloh
 * @property timer hlavny timer, ktory odpocitava sekundy do konca treningu
 * @property preTimer timer na zaciatku treningu, davajuci cas na to, aby sa udaje z databazy dostali do aktivity
 */
class TrainingActivity : AppCompatActivity() {

    private lateinit var dictionaryViewModel: DictionaryViewModel
    lateinit var allWords : List<Word>
    var not_yet_learned_words = mutableListOf<Word>()
    var eligible_words = mutableListOf<Word>()
    var categories = mutableListOf<String?>()
    var picked_category: String? = ""
    var max_possible_task : Int = 0
    var wordCount : Int = 0
    var userMode : Boolean = false
    var current_words = mutableListOf<String?>()
    var completed_tasks = 0
    var correct_tasks = 0

    val timer = object: CountDownTimer(60000, 1000) {
        override fun onTick(millisUntilFinished: Long) {
            timer_text.setText((millisUntilFinished / 1000).toString())
        }

        override fun onFinish() {
            playSound()
            showStatisticsDialog()
        }
    }

    val preTimer = object: CountDownTimer(500, 1000) {
        override fun onTick(millisUntilFinished: Long) {

        }

        override fun onFinish() {
            getCategories()
            showCategoryPickDialog()
        }
    }

    /**
     * zaciatok zivotneho cyklu
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_training)
        //setSupportActionBar(findViewById(R.id.toolbar))

        dictionaryViewModel = ViewModelProvider(this).get(DictionaryViewModel::class.java)
        allWords = listOf<Word>()

        val nameObserver = Observer<List<Word>> { words ->
            allWords = words
        }
        dictionaryViewModel.dictionary.observe(this, nameObserver)
        dictionaryViewModel.wordCount.observe(this, Observer { count -> wordCount })

        preTimer.start()

        supportFragmentManager
            .setFragmentResultListener("category_key", this) { _, bundle ->
                val index = bundle.getInt("result")
                picked_category = categories[index]
                Log.i("kategoria", picked_category.toString())
                setWords()
                setNewTask()
                timer.start()
            }

        supportFragmentManager
            .setFragmentResultListener("result_key", this) { _, bundle ->
                val result = bundle.getBoolean("result")
                val answer = bundle.getString("answer")

                if (result) {
                    for (i in current_words.indices)
                        dictionaryViewModel.increaseStrength(current_words[i])
                    correct_tasks += 1
                }
                completed_tasks += 1
                current_words.clear()

                informResult(result, answer)
            }

        supportFragmentManager
            .setFragmentResultListener("new_word_key", this) { _, _ ->
                //setWords()
                setMaxTask()
                setNewTask()
            }

        supportFragmentManager
            .setFragmentResultListener("statistics_key", this) { _, _ ->
                finish()
            }
    }

    /**
     * metoda na generovanie novej ulohy a spustanie korespondujuceho fragmentu ulohy
     */
    private fun setNewTask() {
        Log.i(" dictionary_size", "${eligible_words.size}, $max_possible_task")

        var task = 0
        if (userMode) {
            task = -1
        } else {
            task = (0..max_possible_task).random()
        }

        //0 WORDS TIER
        if (task == 0) {
            //new word task
            if (not_yet_learned_words.size != 0) {
                val index = (not_yet_learned_words.indices).random()
                val chosen_word_location = not_yet_learned_words[index].image_location
                val chosen_word_word = not_yet_learned_words[index].word
                val chosen_word_translation = not_yet_learned_words[index].translation

                dictionaryViewModel.setWordAsLearned(not_yet_learned_words[index].id)
                eligible_words.add(not_yet_learned_words[index])
                not_yet_learned_words.removeAt(index)

                val bundle = bundleOf("word" to chosen_word_word, "translation" to chosen_word_translation, "address" to chosen_word_location)
                supportFragmentManager.commit {
                    setReorderingAllowed(true)
                    replace<TrainingNewWordFragment>(R.id.current_fragment, args = bundle) }
            } else {
                setNewTask()
            }
        }
        //1 WORD TIER
        else if (task == 1) {
            //spell word task
            val id = (eligible_words.indices).random()
            val chosen_word_location = eligible_words[id].image_location
            val chosen_word_answer = eligible_words[id].word

            current_words.add(chosen_word_answer)
            val bundle = bundleOf("address" to chosen_word_location, "answer" to chosen_word_answer)
            supportFragmentManager.commit {
                setReorderingAllowed(true)
                replace<TrainingSpellWordFragment>(R.id.current_fragment, args = bundle) }
        }
        else if (task == 2) {
            //translate image task
            val id = (eligible_words.indices).random()
            val chosen_word_location = eligible_words[id].image_location
            val chosen_word_answer = eligible_words[id].word

            current_words.add(chosen_word_answer)
            val bundle = bundleOf("address" to chosen_word_location, "answer" to chosen_word_answer)
            supportFragmentManager.commit {
                setReorderingAllowed(true)
                replace<TrainingTranslateImageFragment>(R.id.current_fragment, args = bundle) }

        }
        //2 WORDS TIER
        else if (task == 3) {
//            //Connect images to translations task
//            val id_1 = (allWords.indices).random()
//            val id_2 = (allWords.indices).random()
//
//            while (id_1 == id_2) {
//                val id_2 = (allWords.indices).random()
//            }
//
//            val chosen_word_1_location = allWords[id_1].image_location
//            val chosen_word_1_answer = allWords[id_1].translation
//            val chosen_word_2_location = allWords[id_2].image_location
//            val chosen_word_2_answer = allWords[id_2].translation
//            val bundle = bundleOf("address_1" to chosen_word_1_location, "answer_1" to chosen_word_1_answer, "address_2" to chosen_word_2_location, "answer_2" to chosen_word_2_answer)
//            supportFragmentManager.commit {
//                setReorderingAllowed(true)
//                replace<TrainingConnectFragment>(R.id.current_fragment, args = bundle) }
            setNewTask()
        }
        else if (task == 4) {
            //choose image task with 2 words
            val word_picker = (eligible_words.indices).shuffled().take(2)
            val correct_word = eligible_words[word_picker[0]].word
            val correct_picture = eligible_words[word_picker[0]].image_location
            val other_picture_1 = eligible_words[word_picker[1]].image_location

            current_words.add(correct_word)
            val bundle = bundleOf("correct_word" to correct_word, "correct_picture" to correct_picture, "other_picture_1" to other_picture_1)
            supportFragmentManager.commit {
                setReorderingAllowed(true)
                replace<TrainingChoosePictureTwoFragment>(R.id.current_fragment, args = bundle) }
        }
        else if (task == 5) {
            //choose word task with 2 words
            val word_picker = (eligible_words.indices).shuffled().take(2)
            val correct_word = eligible_words[word_picker[0]].word
            val correct_picture = eligible_words[word_picker[0]].image_location
            val other_word_1 = eligible_words[word_picker[1]].word

            current_words.add(correct_word)
            val bundle = bundleOf("correct_word" to correct_word, "correct_picture" to correct_picture, "other_word_1" to other_word_1)
            supportFragmentManager.commit {
                setReorderingAllowed(true)
                replace<TrainingChooseWordTwoFragment>(R.id.current_fragment, args = bundle) }
        }
        //4 AND MORE WORDS TIER
        else if (task == 6) {
            //choose word task
            val word_picker = (eligible_words.indices).shuffled().take(4)
            val correct_word = eligible_words[word_picker[0]].word
            val correct_picture = eligible_words[word_picker[0]].image_location
            val other_word_1 = eligible_words[word_picker[1]].word
            val other_word_2 = eligible_words[word_picker[2]].word
            val other_word_3 = eligible_words[word_picker[3]].word

            current_words.add(correct_word)
            val bundle = bundleOf("correct_word" to correct_word, "correct_picture" to correct_picture, "other_word_1" to other_word_1, "other_word_2" to other_word_2, "other_word_3" to other_word_3)
            supportFragmentManager.commit {
                setReorderingAllowed(true)
                replace<TrainingChooseWordFragment>(R.id.current_fragment, args = bundle) }
        }
        else if (task == 7) {
            //choose image task
            val word_picker = (eligible_words.indices).shuffled().take(4)
            val correct_word = eligible_words[word_picker[0]].word
            val correct_picture = eligible_words[word_picker[0]].image_location
            val other_picture_1 = eligible_words[word_picker[1]].image_location
            val other_picture_2 = eligible_words[word_picker[2]].image_location
            val other_picture_3 = eligible_words[word_picker[3]].image_location

            current_words.add(correct_word)
            val bundle = bundleOf("correct_word" to correct_word, "correct_picture" to correct_picture, "other_picture_1" to other_picture_1, "other_picture_2" to other_picture_2, "other_picture_3" to other_picture_3)
            supportFragmentManager.commit {
                setReorderingAllowed(true)
                replace<TrainingChoosePictureFragment>(R.id.current_fragment, args = bundle) }
        }
        //USER MODE TIER
        else if (task == -1) {
            //user task
            val word_picker = (eligible_words.indices).shuffled().take(1)
            val correct_word = eligible_words[word_picker[0]].word
            val correct_translation = eligible_words[word_picker[0]].translation

            current_words.add(correct_word)
            val bundle = bundleOf("correct_word" to correct_word, "correct_translation" to correct_translation)
            supportFragmentManager.commit {
                setReorderingAllowed(true)
                replace<TrainingUserFragment>(R.id.current_fragment, args = bundle) }
        }
    }

    /**
     * preda vysledok ulohy aktivite na zobrazenie ohodnotenia
     */
    private fun informResult(result: Boolean, answer: String?) {
        val postTimer = object: CountDownTimer(2000, 1000) {
            override fun onTick(millisUntilFinished: Long) {

            }

            override fun onFinish() {
                setNewTask()
            }
        }

        val bundle = bundleOf("result" to result, "answer" to answer)
        supportFragmentManager.commit {
            setReorderingAllowed(true)
            replace<TrainingResultFragment>(R.id.current_fragment, args = bundle) }

        postTimer.start()
    }

    /**
     * pripravuje list kategorii a vytvara dialog na vyber kategorie
     */
    private fun showCategoryPickDialog() {
        val bundle = bundleOf("category_list" to categories.toTypedArray())
        val dialog = CategoryPickFragment()
        dialog.arguments = bundle
        dialog.show(supportFragmentManager, "category_pick")
    }

    /**
     * po ukonceni treningu vytvara dialog na zobrazenie statistik treningu
     */
    private fun showStatisticsDialog() {
        //zapis vysledkov treninigu do sharedPreferences
        val global_statistics = getSharedPreferences("global_statistics", Context.MODE_PRIVATE)

        var saved_completed_tasks = global_statistics.getInt("completed_tasks", 0)
        var saved_correct_tasks = global_statistics.getInt("correct_tasks", 0)
        saved_completed_tasks += completed_tasks
        saved_correct_tasks += correct_tasks

        var session_count = global_statistics.getInt("session_count", 0)
        session_count += 1

        val editor = global_statistics.edit()
        editor.apply {
            putInt("completed_tasks", saved_completed_tasks)
            putInt("correct_tasks", saved_correct_tasks)
            putInt("session_count", session_count)
        }.apply()

        //nacitaj fragment
        val bundle = bundleOf("completed_tasks" to completed_tasks, "correct_tasks" to correct_tasks)
        val dialog = TrainingStatisticsFragment()
        dialog.arguments = bundle
        dialog.show(supportFragmentManager, "statistics")
    }

    /**
     * vytvorenie listu kategorii
     */
    private fun getCategories() {
        for (i in allWords.indices) {
            categories.add(allWords[i].category)
        }
        val temp_categories = categories.distinct()
        categories = temp_categories.toMutableList()
    }

    /**
     * vyfiltruje zoznam vsetkych slov podla zvolenej kategorie a nasledne ich rozdeli podla toho, ci uz boli naucene
     */
    private fun setWords() {
        for (i in allWords.indices) {
            val current_word = allWords[i]
            if (current_word.category == picked_category) {
                if (current_word.user_created)
                {
                    userMode = true
                }
                if (current_word.learned) {
                    eligible_words.add(current_word)
                } else {
                    not_yet_learned_words.add(current_word)
                }
            }
        }
    }

    /**
     * nastavuje atribut max_possible_task, ktory obmedzuje to, ake typy uloh je mozne generovat
     */
    private fun setMaxTask() {
        val word_counter = eligible_words.size
        if (word_counter == 0) {
            max_possible_task = 0
        }
        else if (word_counter == 1) {
            max_possible_task = 2
        }
        else if (word_counter == 2) {
            max_possible_task = 5
        }
        else if (word_counter >= 4) {
            max_possible_task = 7
        }
    }

    /**
     * prehra zvukovy efekt pri ukonceni treningu
     */
    fun playSound() {
        var soundPlayer = MediaPlayer.create(this, R.raw.finish)
        soundPlayer.start()
    }

    /**
     * pri odchode z aktivity pomocou tlacisla 'spat' zastavuje hlavny timer a uklada doterajsie statistiky
     */
    override fun onBackPressed() {
        super.onBackPressed()
        timer.cancel()

        //zapis vysledkov treninigu do sharedPreferences
        val global_statistics = getSharedPreferences("global_statistics", Context.MODE_PRIVATE)

        var saved_completed_tasks = global_statistics.getInt("completed_tasks", 0)
        var saved_correct_tasks = global_statistics.getInt("correct_tasks", 0)
        saved_completed_tasks += completed_tasks
        saved_correct_tasks += correct_tasks

        val editor = global_statistics.edit()
        editor.apply {
            putInt("completed_tasks", saved_completed_tasks)
            putInt("correct_tasks", saved_correct_tasks)
        }.apply()
        Toast.makeText(this, R.string.statistics_saving, Toast.LENGTH_SHORT).show()
    }

    /**
     * ukoncenie zivotneho cyklu
     */
    override fun onDestroy() {
        super.onDestroy()
        timer.cancel()
    }
}