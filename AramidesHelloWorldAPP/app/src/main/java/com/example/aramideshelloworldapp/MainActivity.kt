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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val flashcardQuestion = findViewById<TextView>(R.id.flashcard_question)
        val flashcardAnswer = findViewById<TextView>(R.id.flashcard_answer)
        val addButton = findViewById<ImageView>(R.id.add_question_button)
        val editButton = findViewById<ImageView>(R.id.edit_question)

        flashcardQuestion.setOnClickListener{
            flashcardQuestion.visibility = View.INVISIBLE
            flashcardAnswer.visibility = View.VISIBLE


        }
        flashcardAnswer.setOnClickListener{
            flashcardAnswer.visibility = View.INVISIBLE
            flashcardQuestion.visibility = View.VISIBLE
        }
        //flashcardQuestion.setOnClickListener {
           // flashcardAnswer.isVisible = !flashcardAnswer.isVisible
       // }




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
                }
                else {
                    Log.i("MainActivity", "Returned null data from AddCardActivity")
                }
        }




        addButton.setOnClickListener {
            val intent = Intent(this, AddCardActivity::class.java)
            resultLauncher.launch(intent)
        }


        editButton.setOnClickListener {
            val intent = Intent(this, AddCardActivity::class.java)
            val editquestion = flashcardQuestion.text.toString()
            val editanswer = flashcardAnswer.text.toString()
            intent.putExtra("stringKey1", editquestion)
            intent.putExtra("stringKey2", editanswer)
            resultLauncher.launch(intent)
        }
    }
}