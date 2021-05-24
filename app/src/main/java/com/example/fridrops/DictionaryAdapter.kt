package com.example.fridrops

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.dictionary_item.view.*

/**
 * trieda adaptera pre recycler view
 */
class DictionaryAdapter: RecyclerView.Adapter<DictionaryAdapter.WordHolder>() {

    /**
     * list, do ktoreho sa ukladaju slova urcene na zobrazenie do recycler viewu
     */
    private var dictionary = emptyList<Word>()

    /**
     * pomocna trieda pre triedu DictionaryAdapter
     */
    class WordHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

    }

    /**
     * povinna metoda pre fungovanie adaptera
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DictionaryAdapter.WordHolder {
        return WordHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.dictionary_item,
                parent,
                false
            )
        )
    }

    /**
     * povinna metoda pre fungovanie adaptera
     */
    override fun onBindViewHolder(holder: DictionaryAdapter.WordHolder, position: Int) {
        val item = dictionary[position]
        holder.itemView.word.text = item.word
        holder.itemView.translation.text = item.translation
        holder.itemView.strength.text = "Sila : ${item.strength.toString()}"
        holder.itemView.image.setImageURI(Uri.parse(item.image_location))
        holder.itemView.image.layoutParams.height = 200
        holder.itemView.image.layoutParams.width = 200
    }

    /**
     * povinna metoda pre fungovanie adaptera - vrati pocet slov v slovniku
     */
    override fun getItemCount(): Int {
        return dictionary.size
    }

    /**
     * priradii list z parametra do listu v adapteri
     * @param dictionary list slov, ktory sa priradi do listu v adapteri
     */
    fun setWords(dictionary: List<Word>) {
        this.dictionary = dictionary
        notifyDataSetChanged()
    }
}
