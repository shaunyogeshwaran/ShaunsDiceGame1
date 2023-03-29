package com.example.dicegame

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.dicegame.databinding.ActivityGameBinding
import com.example.dicegame.model.Score
import kotlin.random.Random

class GameActivity : AppCompatActivity() {
    lateinit var binding: ActivityGameBinding
    private val score = Score()

    private val dices = arrayOf(
        R.drawable.die_face_1,
        R.drawable.die_face_2,
        R.drawable.die_face_3,
        R.drawable.die_face_4,
        R.drawable.die_face_5,
        R.drawable.die_face_6
    )

    var diceValuesComputer: List<Int> = listOf()
    var diceValuesHuman: List<Int> = listOf()
    var indicesToReRoll: MutableList<Int> = mutableListOf()
    var isFastTime = true
    var isFinished = false
    var isTie = false
    var rollCount = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGameBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // init game board
        setScore()


        binding.buttonThrow.setOnClickListener {

            if (isFinished) {
                Toast.makeText(this, "Game finished", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            showLoadingDialog()
            // When click roll count increase by 1
            rollCount += 1

            // For first time roll both dice
            if (isFastTime) {
                diceValuesComputer = rollDice()
                diceValuesHuman = rollDice()
                isFastTime = false
            } else {
                if (!selectScoreToKeepComputer(diceValuesComputer))
                    diceValuesComputer = rollDice()
                diceValuesHuman = reRollDice(diceValuesHuman, indicesToReRoll)
                indicesToReRoll = mutableListOf()
            }

            setDiceImages()
            clearBackground()

            // Calculate the score
            if (rollCount > 2 || isTie) {
                calculateScore()
            }


        }

        binding.buttonScore.setOnClickListener {
            if (isFinished) return@setOnClickListener
            if (isFastTime) {
                Toast.makeText(this, "Throw to start the game.", Toast.LENGTH_SHORT).show()
            } else if (rollCount == 0) {
                Toast.makeText(this, "Throw again", Toast.LENGTH_SHORT).show()
            } else {
                calculateScore()
            }

            indicesToReRoll = mutableListOf()
            clearBackground()
        }

        binding.imgHum1.setOnClickListener {

            if (isFastTime || rollCount == 0) return@setOnClickListener

            if (indicesToReRoll.contains(0)) {
                indicesToReRoll.remove(0)
                binding.imgHum1.setBackgroundResource(0)
            } else {
                indicesToReRoll.add(0)
                binding.imgHum1.setBackgroundResource(R.drawable.about_bg)
            }
        }
        binding.imgHum2.setOnClickListener {
            if (isFastTime || rollCount == 0) return@setOnClickListener
            if (indicesToReRoll.contains(1)) {
                indicesToReRoll.remove(1)
                binding.imgHum2.setBackgroundResource(0)
            } else {
                indicesToReRoll.add(1)
                binding.imgHum2.setBackgroundResource(R.drawable.about_bg)
            }
        }
        binding.imgHum3.setOnClickListener {
            if (isFastTime || rollCount == 0) return@setOnClickListener
            if (indicesToReRoll.contains(2)) {
                indicesToReRoll.remove(2)
                binding.imgHum3.setBackgroundResource(0)
            } else {
                indicesToReRoll.add(2)
                binding.imgHum3.setBackgroundResource(R.drawable.about_bg)
            }
        }
        binding.imgHum4.setOnClickListener {
            if (isFastTime || rollCount == 0) return@setOnClickListener
            if (indicesToReRoll.contains(3)) {
                indicesToReRoll.remove(3)
                binding.imgHum4.setBackgroundResource(0)
            } else {
                indicesToReRoll.add(3)
                binding.imgHum4.setBackgroundResource(R.drawable.about_bg)
            }
        }
        binding.imgHum5.setOnClickListener {
            if (isFastTime || rollCount == 0) return@setOnClickListener
            if (indicesToReRoll.contains(4)) {
                indicesToReRoll.remove(4)
                binding.imgHum5.setBackgroundResource(0)
            } else {
                indicesToReRoll.add(4)
                binding.imgHum5.setBackgroundResource(R.drawable.about_bg)
            }
        }


    }

    private fun clearBackground() {
        binding.imgHum1.setBackgroundResource(0)
        binding.imgHum2.setBackgroundResource(0)
        binding.imgHum3.setBackgroundResource(0)
        binding.imgHum4.setBackgroundResource(0)
        binding.imgHum5.setBackgroundResource(0)
    }

    private fun setScore() {
        binding.textViewScore.text = getScoreText()
    }

    private fun getScoreText(): String = "H:${score.humanScore}/C:${score.computerScore}"

    private fun setDiceImages() {
        binding.imgComp1.setImageResource(dices[diceValuesComputer[0]])
        binding.imgComp2.setImageResource(dices[diceValuesComputer[1]])
        binding.imgComp3.setImageResource(dices[diceValuesComputer[2]])
        binding.imgComp4.setImageResource(dices[diceValuesComputer[3]])
        binding.imgComp5.setImageResource(dices[diceValuesComputer[4]])

        binding.imgHum1.setImageResource(dices[diceValuesHuman[0]])
        binding.imgHum2.setImageResource(dices[diceValuesHuman[1]])
        binding.imgHum3.setImageResource(dices[diceValuesHuman[2]])
        binding.imgHum4.setImageResource(dices[diceValuesHuman[3]])
        binding.imgHum5.setImageResource(dices[diceValuesHuman[4]])
    }

    private fun rollDice(): List<Int> {
        return List(5) { Random.nextInt(0, 6) }
    }

    private fun reRollDice(diceValues: List<Int>, indicesToReRoll: List<Int>): List<Int> {
        return List(5) { i ->
            if (!indicesToReRoll.contains(i)) {
                Random.nextInt(0, 6)
            } else {
                diceValues[i]
            }
        }
    }

    private fun calculateScore() {
        rollCount = 0
        println("Calculating score")

        // Calculate the score
        println(diceValuesHuman)
        println(diceValuesComputer)
        val humanScore = diceValuesHuman.sum() + 5
        val computerScore = diceValuesComputer.sum() + 5
        println("Human: $humanScore Computer: $computerScore")

        // Sum with previous
        score.computerScore += computerScore
        score.humanScore += humanScore

        // Update scoreboard
        setScore()

        // check for winner
        if (score.computerScore == 101 && score.humanScore == 101) { //  tie
            isTie = true
        } else if (score.computerScore >= 101) { // computer win
            showWinnerDialog(true)
            isFinished = true
        } else if (score.humanScore >= 101) { // human win
            showWinnerDialog(false)
            isFinished = true
        }

    }

    private fun selectScoreToKeepComputer(diceValues: List<Int>): Boolean {
        val score = diceValues.sum()
        return score >= 20
    }

    private fun showWinnerDialog(winnerIsComputer: Boolean) {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_result, null)
        val dialogBuilder = AlertDialog.Builder(this).setView(dialogView)
        dialogBuilder.setCancelable(false)
        val tvWinner = dialogView.findViewById<TextView>(R.id.tvWinner)
        val tvScore = dialogView.findViewById<TextView>(R.id.tvScore)
        val buttonClose = dialogView.findViewById<Button>(R.id.buttonClose)
        val ivMascot = dialogView.findViewById<ImageView>(R.id.ivMascot)

        if (winnerIsComputer) {
            ivMascot.setImageResource(R.drawable.robot)
            tvWinner.text = "Computer Win"
        }

        tvScore.text = getScoreText()

        val messageBoxInstance = dialogBuilder.show()
        buttonClose.setOnClickListener {
            messageBoxInstance.dismiss()
        }
    }

    private fun showLoadingDialog() {
        val customDialog = Dialog(this)
        customDialog.setContentView(R.layout.dialog_loading)
        customDialog.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        customDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
//        customDialog.window?.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
        customDialog.show()
        Handler(Looper.getMainLooper()).postDelayed({
            customDialog.dismiss()
        }, 2000)
    }
}