package com.example.aramideshelloworldapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.google.android.material.snackbar.Snackbar

// This Kotlin file is where user interaction will be handled
class  MainActivity : AppCompatActivity() {
    lateinit var flashcardDatabase: FlashcardDatabase
    var allFlashcards = mutableListOf<Flashcard>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val flashcardQuestion = findViewById<TextView>(R.id.flashcard_question)
        val flashcardAnswer = findViewById<TextView>(R.id.flashcard_answer)
        val addButton = findViewById<ImageView>(R.id.add_question_button)
        val editButton = findViewById<ImageView>(R.id.edit_question)
        val nextButton = findViewById<ImageView>(R.id.next_button)

        var currentCardDisplayedIndex = 0

        flashcardDatabase = FlashcardDatabase(this)
        allFlashcards = flashcardDatabase.getAllCards().toMutableList()

        if(allFlashcards.isNotEmpty()) {
            flashcardQuestion.text = allFlashcards[0].question
            flashcardAnswer.text = allFlashcards[0].answer
        }

        if (allFlashcards.size > 0) {
            findViewById<TextView>(R.id.flashcard_question).text = allFlashcards[0].question
            findViewById<TextView>(R.id.flashcard_answer).text = allFlashcards[0].answer
        }

        flashcardQuestion.setOnClickListener{
            flashcardQuestion.visibility = View.INVISIBLE
            flashcardAnswer.visibility = View.VISIBLE


        }
        flashcardAnswer.setOnClickListener{
            flashcardAnswer.visibility = View.INVISIBLE
            flashcardQuestion.visibility = View.VISIBLE
        }


        val resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                result ->
                val data: Intent? = result.data
                if (data != null) {
                    val string1 = data.getStringExtra("Question")
                    val string2 = data.getStringExtra("Answer")
                    flashcardQuestion.text = string1
                    flashcardAnswer.text = string2

                    // Log the value of the strings for easier debugging
                    Log.i("MainActivity", "string1: $string1")
                    Log.i("MainActivity", "string2: $string2")
                    Snackbar.make(findViewById(R.id.flashcard_question), "Successfully created card", Snackbar.LENGTH_SHORT).show()


                    if (string1 != null && string2 != null) {
                        flashcardDatabase.insertCard(Flashcard(string1, string2))
                        // Update set of flashcards to include new card
                        allFlashcards = flashcardDatabase.getAllCards().toMutableList()
                    } else {
                        Log.e("TAG", "Missing question or answer to input into database. Question is $string1 and answer is $string2")
                    }
                }
                else {
                    Log.i("MainActivity", "Returned null data from AddCardActivity")
                }
        }
        val editResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result ->
            val data: Intent? = result.data
            if (data != null){
                val string1 = data.getStringExtra("Question")
                val string2 = data.getStringExtra("Answer")


                flashcardQuestion.text = string1
                flashcardAnswer.text = string2


                if (!string1.isNullOrEmpty() && !string2.isNullOrEmpty() ) {
                    flashcardDatabase.updateCard(Flashcard(string1, string2))
                    allFlashcards = flashcardDatabase.getAllCards().toMutableList()
                }
            }
            else {
                Log.i("MainActivity", "Returned Null dta from AddCardActivity")
            }
        }
        addButton.setOnClickListener {
            val intent = Intent(this, AddCardActivity::class.java)
            resultLauncher.launch(intent)
        }
        nextButton.setOnClickListener(){
            if(allFlashcards.isEmpty() or (allFlashcards.size==1)){
                Snackbar.make(findViewById(R.id.flashcard_question), "No card left, create Card", Snackbar.LENGTH_SHORT).show()
            }
            else{

            }
        }
        editButton.setOnClickListener {
            val intent = Intent(this, AddCardActivity::class.java)
            val editquestion = flashcardQuestion.text.toString()
            val editanswer = flashcardAnswer.text.toString()
            intent.putExtra("stringKey1", editquestion)
            intent.putExtra("stringKey2", editanswer)
            resultLauncher.launch(intent)
            findViewById<ImageView>(R.id.next_button).setOnClickListener {
                // advance our pointer index so we can show the next card
                if (allFlashcards.size == 0) {
                    return@setOnClickListener
                }
                currentCardDisplayedIndex++
                // make sure we don't get an IndexOutOfBoundsError if we are viewing the last indexed card in our list
                if(currentCardDisplayedIndex >= allFlashcards.size) {
                    Snackbar.make(
                        findViewById<TextView>(R.id.flashcard_question), // This should be the TextView for displaying your flashcard question
                        "You've reached the end of the cards, going back to start.",
                        Snackbar.LENGTH_SHORT)
                        .show()
                    currentCardDisplayedIndex = 0
                }
                allFlashcards = flashcardDatabase.getAllCards().toMutableList()
                val (question, answer) = allFlashcards[currentCardDisplayedIndex]
                //val question= allFlashcards[currentCardDisplayedIndex].question
                //val answer= allFlashcards[currentCardDisplayedIndex].answer
                //question1.text = question
                //correctanswer.text = answer //ans or correct ans

                findViewById<TextView>(R.id.flashcard_answer).text = answer
                findViewById<TextView>(R.id.flashcard_question).text = question

            }

        }
        }

    }
