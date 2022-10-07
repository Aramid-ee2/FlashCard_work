package com.example.aramideshelloworldapp

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast

class AddCardActivity : AppCompatActivity() {
   // @SuppressLint("CutPasteId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        val s1 = intent.getStringExtra("stringKey1")
        val s2 = intent.getStringExtra("stringKey2")



        val questionEditText = findViewById<EditText>(R.id.Question)
        val answerEditText = findViewById<EditText>(R.id.answer)
        questionEditText.setText(s1)
        answerEditText.setText(s2)


        val cancelButton = findViewById<ImageView>(R.id.cancel_question)
        cancelButton.setOnClickListener{
            finish()
        }

        val saveButton = findViewById<ImageView>(R.id.save_question)
        saveButton.setOnClickListener {
            val gettext = findViewById<EditText>(R.id.Question).text.toString()
            val getanswer = findViewById<EditText>(R.id.answer).text.toString()

            if ((gettext == "") or (getanswer == "")) {
                Log.i("MainActivity", "Returned null data from AddCardActivity")
                val toast = Toast.makeText(applicationContext, "Incomplete Card", Toast.LENGTH_SHORT)
                toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0)
                toast.show()
            }
            else {
                val data = Intent()
                data.putExtra(
                    "Question", gettext
                )
                data.putExtra(
                    "Answer", getanswer
                )
                setResult(RESULT_OK, data)
                finish()
            }

        }
    }
}