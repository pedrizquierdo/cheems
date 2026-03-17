package mx.itson.cheems

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import mx.itson.cheems.entities.Winner

class MainActivity : AppCompatActivity(), View.OnClickListener {

    var gameOverCard = 0
    var cheems_master = 0
    var cardCount = 0
    var gameOver = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        //Winner().save(this, "Nicolas Becerra", "Nicotrans")
        //Winner().getAll(this)

        val btnListWinners = findViewById<View>(R.id.btn_list_winners) as Button
        btnListWinners.setOnClickListener(this)

        val btnNewWinner = findViewById<View>(R.id.btn_new_winner) as Button
        btnNewWinner.setOnClickListener(this)
        btnNewWinner.visibility = View.GONE
        start()
        Toast.makeText(this, "Bienvenido Jugador", Toast.LENGTH_LONG).show()
    }

    fun start() {
        cardCount = 0
        gameOver = false
        for (i in 1 .. 12) {
            val btnCard = findViewById<View>(
                resources.getIdentifier("card$i", "id", this.packageName)

            ) as ImageButton
            btnCard.isEnabled=true
            btnCard.setOnClickListener(this)
            btnCard.setBackgroundResource(R.drawable.cheems_question)

            val btnRes = findViewById<View>(R.id.btnRes)
            btnRes.setOnClickListener(this)
        }
        cheems_master = (1..12).random()
        gameOverCard = (1 .. 12).random()

        findViewById<Button>(R.id.btn_new_winner).visibility = View.GONE
        Log.d("Valor de la carta fracasada", "La carta fracasada es ${gameOverCard.toString()}")
        Log.d("Valor de la carta master", "La carta master es ${cheems_master.toString()}")
    }

    fun vibrate(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val vibratorAdmin =
                applicationContext.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
            val vibrator = vibratorAdmin.defaultVibrator
            vibrator.vibrate(VibrationEffect.createOneShot(1500, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            val vibrator = applicationContext.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
            vibrator.vibrate(1500)
        }
    }

    fun flip(card: Int) {
        if (gameOver) return

        val btnCard = findViewById<View>(
            resources.getIdentifier("card$card", "id", this.packageName)
        ) as ImageButton

        if (card == cheems_master) {
            gameOver = true
            vibrate()
            Toast.makeText(this, "¡Encontraste al Cheems Master! Ganaste bro", Toast.LENGTH_LONG).show()
            findViewById<Button>(R.id.btn_new_winner).visibility = View.VISIBLE
            revealAllCards()
        }
        else if (card == gameOverCard) {
            gameOver = true
            vibrate()
            Toast.makeText(this, "¡Perdiste! JAJAJA FRACASADO", Toast.LENGTH_LONG).show()
            findViewById<Button>(R.id.btn_new_winner).visibility = View.GONE
            revealAllCards()
        }
        else {
            btnCard.setBackgroundResource(R.drawable.cheems_ok)
            btnCard.isEnabled = false
            cardCount++

            if (cardCount >=10) {
                gameOver = true
                vibrate()
                Toast.makeText(this, "Ganaste bro", Toast.LENGTH_LONG).show()
                findViewById<Button>(R.id.btn_new_winner).visibility = View.VISIBLE
                revealAllCards()
            }
        }
    }
    fun revealAllCards() {
        for (i in 1..12) {
            val btn = findViewById<View>(
                resources.getIdentifier("card$i", "id", this.packageName)
            ) as ImageButton
            btn.isEnabled = false

            when (i) {
                cheems_master -> btn.setBackgroundResource(R.drawable.cheems_master)
                gameOverCard -> btn.setBackgroundResource(R.drawable.cheems_bad)
                else -> btn.setBackgroundResource(R.drawable.cheems_ok)
            }
        }
    }



    override fun onClick(v: View) {
        when(v.id){
            R.id.btnRes -> start()
            R.id.card1 -> {flip(1)}
            R.id.card2 -> {flip(2)}
            R.id.card3 -> {flip(3)}
            R.id.card4 -> {flip(4)}
            R.id.card5 -> {flip(5)}
            R.id.card6 -> {flip(6)}
            R.id.card7 -> {flip(7)}
            R.id.card8 -> {flip(8)}
            R.id.card9 -> {flip(9)}
            R.id.card10 -> {flip(10)}
            R.id.card11 -> {flip(11)}
            R.id.card12 -> {flip(12)}
            R.id.btn_new_winner -> {
                val intentWinnerForm = Intent(this, WinnerFormActivity::class.java)
                startActivity(intentWinnerForm )
                start()
            }
            R.id.btn_list_winners -> {
                val intentWinnerList = Intent(this, WinnerListActivity::class.java)
                startActivity(intentWinnerList)
            }
        }
        }
    }