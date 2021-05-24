package com.example.fridrops

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * trieda reprezentuje entitu v databaze aplikacie, ktora bude uchovavat jednotlive slova slovnika a dodatocne informacie o kazdom slove, napr. jeho 'silu naucenia'
 */
@Entity(tableName = "dictionary")
class Word(
    /**
     * primarny kluc do tabulky - inkrementuje sa automaticky pri pridavani noveho zaznamu
     */
    @PrimaryKey(autoGenerate = true)
    public val id: Int,

    /**
     * text slova
     */
    public val word: String?,

    /**
     * preklad slova
     */
    public val translation: String?,

    /**
     * kategoria slova
     */
    public val category: String?,

    /**
     * sila naucenia - kolko krat bolo slovo uspesne uhadnute
     */
    public val strength: Int,

    /**
     * priznak toho, ci bolo slovo vytvorene uzivatelom
     */
    public val user_created: Boolean,

    /**
     * URI obrazku, ktore je priradene slovu (skonvertovane na string)
     */
    public val image_location: String?,

    /**
     * priznak toho, ci je slovo uz naucene
     */
    public val learned: Boolean

)

