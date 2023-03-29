package com.example.dicegame

import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ViewGroup
import com.example.dicegame.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding:ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonNewGame.setOnClickListener {
            startActivity(
                Intent(
                    this,
                    GameActivity::class.java
                )
            )
        }

        binding.buttonAbout.setOnClickListener {
            showAboutDialog()
        }

    }

    private fun showAboutDialog() {
        val customDialog = Dialog(this)
        customDialog.setContentView(R.layout.dialog_about)
        customDialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        customDialog.show()
    }
}