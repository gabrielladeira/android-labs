package com.example.android.diceroller

import android.hardware.SensorManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.squareup.seismic.ShakeDetector
import kotlin.random.Random

class MainActivity : AppCompatActivity(), ShakeDetector.Listener {
    lateinit var resultImage: ImageView
    lateinit var rollButton: Button
    lateinit var resultText: TextView
    var isRolling: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        resultImage = findViewById(R.id.result_image)

        resultText = findViewById(R.id.result_text)

        rollButton = findViewById(R.id.roll_button)
        rollButton.setOnClickListener { handleRollDice(30) }


        val sensorManager: SensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        val sd: ShakeDetector = ShakeDetector(this)
        sd.start(sensorManager)
    }

    override fun hearShake() {
        if (!isRolling) {
            handleRollDice(30)
        }
    }

    private fun handleRollDice(number: Int) {
        isRolling = true
        resultText.text = ""
        rollButton.isEnabled = false
        val rolledNumber = rollDice()

        if (number > 0) {
            Handler().postDelayed({ handleRollDice(number - 1) }, (1000 / number).toLong())
        } else {
            isRolling = false
            rollButton.isEnabled = true
            resultText.text =
                resources.getString(R.string.result_text).replace("{0}", rolledNumber.toString())
        }
    }

    private fun rollDice(): Int {
        val number = Random.nextInt(6) + 1
        val drawableResource = when (number) {
            1 -> R.drawable.dice_1
            2 -> R.drawable.dice_2
            3 -> R.drawable.dice_3
            4 -> R.drawable.dice_4
            5 -> R.drawable.dice_5
            else -> R.drawable.dice_6
        }

        resultImage.setImageResource(drawableResource)

        return number
    }
}
