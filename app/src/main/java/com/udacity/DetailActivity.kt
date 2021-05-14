package com.udacity

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.content_detail.*

class DetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        setSupportActionBar(toolbar)


        val projectNumber = intent.getIntExtra(Intent.EXTRA_INDEX,  -1)
        val status = intent.getStringExtra(Intent.EXTRA_TEXT)
        if (projectNumber != -1){
            val fileName = when(projectNumber) {
                1 -> getString(R.string.option_1)
                2 -> getString(R.string.option_2)
                3 -> getString(R.string.option_3)
                else -> getString(R.string.option_2)
            }

            file_name_text.text = fileName
            status_text.text = status

            if (status != "Success") {
                status_text.setTextColor(Color.RED)
            }
        }

        ok_button.setOnClickListener {
            finish()
        }
    }

}
