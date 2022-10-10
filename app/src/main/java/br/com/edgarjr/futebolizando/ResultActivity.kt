package br.com.edgarjr.futebolizando

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import br.com.edgarjr.futebolizando.databinding.ActivityResultBinding

class ResultActivity : AppCompatActivity() {

    private lateinit var activityResultBinding : ActivityResultBinding

    var pontuacao = 0
    var acertos = 0
    var erros = 0
    var pulos = 0
    var isKey = false

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activityResultBinding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(activityResultBinding.root)

        pontuacao = intent.extras!!.getInt("pontuacao")
        acertos = intent.extras!!.getInt("acertos")
        pulos = intent.extras!!.getInt("pulos")

        initializeViews()
    }

    @SuppressLint("SetTextI18n")
    private fun initializeViews() {

        activityResultBinding.apply{
            tvScore.text = "Score: $pontuacao"
            tvRight.text = "Correct: $acertos"
            tvWrong.text = "Wrong: $erros"
            tvSkip.text = "SKip: $pulos"

            if (pontuacao >= 6) {
                activityResultBinding.emojiReactionImg.setImageResource(R.drawable.smile)
                Toast.makeText(this@ResultActivity, "Wow Great", Toast.LENGTH_SHORT).show()
            } else {
                activityResultBinding.emojiReactionImg.setImageResource(R.drawable.angry)
                Toast.makeText(this@ResultActivity, "Need Improvement", Toast.LENGTH_SHORT).show()
            }

            tvPlayAgain.setOnClickListener {
                finish()
            }
        }
    }
}