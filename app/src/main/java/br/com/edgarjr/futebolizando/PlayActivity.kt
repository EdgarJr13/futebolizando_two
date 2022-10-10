package br.com.edgarjr.futebolizando

import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.widget.Button
import android.widget.RadioButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import br.com.edgarjr.futebolizando.databinding.ActivityPlayBinding
import java.util.*
import java.util.concurrent.TimeUnit

class PlayActivity : AppCompatActivity() {

    private lateinit var activityPlayBinding: ActivityPlayBinding
    var trollAudio : MediaPlayer? = null

    private var countDownTimer: CountDownTimer? = null
    private val countDownMilisegundos: Long = 30000
    private val countDownIntervalo: Long = 1000
    private var tempoRestanteMilisegundos: Long = 0
    private var corPadrao: ColorStateList? = null

    private var pontuacao = 0
    private var acertos = 0
    private var erros = 0
    private var pulos = 0
    private var qIndex = 0
    private var updateQueNo = 1

    private var perguntas = arrayOf(
        "Q.1. Quantos gols Neymar Jr. marcou pelo Santos?",
        "Q.2. Quem foi o artilheiro da primeira edição da Copa Libertadores?",
        "Q.3. Por qual clube Fernando Diniz ficou mais tempo empregado?",
        "Q.4. Qual foi a partida de estreia do atleta Reinier Jesus?",
        "Q.5. Qual time ganhou a Champions League 02/03?",
        "Q.6. Em qual ano o Fluminense disputou a Série C do Campeonato Brasileiro?",
        "Q.7. Quantos jogos Willian disputou pelo Corinthians?",
        "Q.8. Em qual ano o Atlético Mineiro foi rejeitado por Nikolas Anelka?",
        "Q.9. Qual o primeiro clube do Brasil a ter virado SAF?",
        "Q.10. Quem foi o artilheiro da Seleção Brasileira na Copa do Mundo de 82?",
        "Q.11. Zico disputou quantas partidas pela Udinese?",
        "Q.12. Quem é o atleta com mais títulos na história do futebol?",
        "Q.13. Quem é o maior artilheiro da história do Campeonato Brasileiro?",
        "Q.14. Quantos títulos brasileiros possui o Flamengo?",
        "Q.15. Quem é o atleta mais novo a marcar um gol em uma final de Champions League?",
        "Q.16. Quem é o atleta que jogou mais partidas seguidas no Campeonato Brasileiro?"
    )


    private var respostas = arrayOf(
        "136 gols",
        "Alberto Spencer",
        "São Paulo",
        "Flamengo x Emelec",
        "AC Milan",
        "1999",
        "85",
        "2014",
        "Cruzeiro",
        "Zico",
        "54 jogos",
        "Daniel Alves",
        "Roberto Dinamite",
        "Hepta",
        "Carlos Alberto",
        "Dario Conca"
    )

    private var opcoes = arrayOf(
        "141 gols",
        "136 gols",
        "129 gols",
        "131 gols",
        "Alberto Spencer",
        "Luizão",
        "Hernán Barcos",
        "Pelé",
        "Fluminense",
        "São Paulo",
        "Audax SP",
        "Athletico Paranaense",
        "Flamengo x San Lorenzo",
        "Flamengo x Emelec",
        "Flamengo x Internacional",
        "Flamengo x Grêmio",
        "Liverpool",
        "AC Milan",
        "Arsenal",
        "Barcelona",
        "1997",
        "1991",
        "1999",
        "1994",
        "88",
        "77",
        "81",
        "85",
        "2012",
        "2014",
        "2016",
        "2015",
        "Cruzeiro",
        "Vasco da Gama",
        "Botafogo",
        "Bahia",
        "Sócrates",
        "Casagrande",
        "Roberto Dinamite",
        "Zico",
        "57 jogos",
        "54 jogos",
        "61 jogos",
        "67 jogos",
        "Lionel Messi",
        "Daniel Alves",
        "Diego Maradona",
        "Thierry Henry",
        "Fred",
        "Romario",
        "Edmundo",
        "Roberto Dinamite",
        "Hepta",
        "Hexa",
        "Penta",
        "Octa",
        "Vinicius Jr.",
        "Cristiano Ronaldo",
        "Peter Crouch",
        "Carlos Alberto",
        "Dario Conca",
        "Rogerio Ceni",
        "Paulo Baier",
        "Marcos"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityPlayBinding = ActivityPlayBinding.inflate(layoutInflater)
        setContentView(activityPlayBinding.root)

        initViews()
    }

    @SuppressLint("SetTextI18n")
    private fun mostrarProximaQuestao() {

        checarResposta()
        activityPlayBinding.apply {
            if(updateQueNo < 16) {
                tvNoOfQues.text = "${updateQueNo + 1}/16"
                updateQueNo++
            }
            if(qIndex <= perguntas.size - 1) {
                tvQuestion.text = perguntas[qIndex]
                radioButton1.text = opcoes[qIndex * 4]
                radioButton2.text = opcoes[qIndex * 4 + 1]
                radioButton3.text = opcoes[qIndex * 4 + 2]
                radioButton4.text = opcoes[qIndex * 4 + 3]
            } else {
                pontuacao = acertos
                val intent = Intent(this@PlayActivity, ResultActivity::class.java)
                intent.putExtra("acertos", acertos)
                intent.putExtra("erros", erros)
                intent.putExtra("pular", pulos)
                startActivity(intent)
                finish()
            }
            radiogrp.clearCheck()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun checarResposta() {

        activityPlayBinding.apply {
            if(radiogrp.checkedRadioButtonId == -1) {
                pulos++
                tempoEsgotadoAlertDialog()
            } else {
                val checkRadioButton = findViewById<RadioButton>(radiogrp.checkedRadioButtonId)
                val checkAnswer = checkRadioButton.text.toString()
                if(checkAnswer == respostas[13]){
                    trollAlertDialog()
                    countDownTimer?.cancel()
                } else if(checkAnswer == respostas[qIndex]) {
                    acertos++
                    txtPlayScore.text = "Pontuação : $acertos"
                    acertoAlertDialog()
                    countDownTimer?.cancel()
                } else {
                    erros++
                    erroAlertDialog()
                    countDownTimer?.cancel()
                }
            }
            qIndex++
        }
    }

    @SuppressLint("SetTextI18n")
    private fun initViews() {

        activityPlayBinding.apply {
            tvQuestion.text = perguntas[qIndex]
            radioButton1.text = opcoes[0]
            radioButton2.text = opcoes[1]
            radioButton3.text = opcoes[2]
            radioButton4.text = opcoes[3]

            //checagem de opção marcada ou não. se estiver selecionada, avisa se está correta ou não!
            nextQuestionBtn.setOnClickListener {
                if (radiogrp.checkedRadioButtonId == -1) {
                    Toast.makeText(this@PlayActivity,
                        "Selecione uma opção!",
                        Toast.LENGTH_SHORT)
                        .show()
                } else {
                    mostrarProximaQuestao()
                }
            }

            tvNoOfQues.text = "$updateQueNo/16"
            tvQuestion.text = perguntas[qIndex]

            corPadrao = quizTimer.textColors

            tempoRestanteMilisegundos = countDownMilisegundos

            startCountDownTimer()
        }
    }

    private fun startCountDownTimer() {
        countDownTimer = object : CountDownTimer(tempoRestanteMilisegundos, countDownIntervalo) {
            override fun onTick(milisAteTerminar: Long) {
                activityPlayBinding.apply {
                    tempoRestanteMilisegundos = milisAteTerminar
                    val segundo = TimeUnit.MILLISECONDS.toSeconds(tempoRestanteMilisegundos).toInt()

                    val timer = String.format(Locale.getDefault(), "Time: %02d", segundo)
                    quizTimer.text = timer

                    if(tempoRestanteMilisegundos < 10000) {
                        quizTimer.setTextColor(Color.RED)
                    } else {
                        quizTimer.setTextColor(corPadrao)
                    }
                }
            }

            override fun onFinish() {
                mostrarProximaQuestao()
            }
        }.start()
    }

    @SuppressLint("SetTextI18n")
    private fun acertoAlertDialog() {
        val builder = AlertDialog.Builder(this@PlayActivity)
        val view = LayoutInflater.from(this@PlayActivity).inflate(R.layout.correct_answer, null)
        builder.setView(view)
        val tvPontuacao = view.findViewById<TextView>(R.id.tvDialogPontuacao)
        val acertoOkBtn = view.findViewById<Button>(R.id.correct_ok)
        tvPontuacao.text = "Pontuação: $acertos"
        val alertDialog = builder.create()
        acertoOkBtn.setOnClickListener {
            tempoRestanteMilisegundos = countDownMilisegundos
            startCountDownTimer()
            alertDialog.dismiss()
        }
        alertDialog.show()
    }

    @SuppressLint("SetTextI18n")
    private fun erroAlertDialog() {
        val builder = AlertDialog.Builder(this@PlayActivity)
        val view = LayoutInflater.from(this@PlayActivity).inflate(R.layout.wrong_answer, null)
        builder.setView(view)

        val tvErroDialogRespCorreta = view.findViewById<TextView>(R.id.tv_wrongDialog)
        val erroOkBtn = view.findViewById<Button>(R.id.wrong_ok)
        tvErroDialogRespCorreta.text = "Resposta Correta: " + respostas[qIndex]
        val alertDialog = builder.create()

        erroOkBtn.setOnClickListener {
            tempoRestanteMilisegundos = countDownMilisegundos
            startCountDownTimer()
            alertDialog.dismiss()
        }
        alertDialog.show()
    }

    @SuppressLint("SetTextI18n")
    private fun tempoEsgotadoAlertDialog() {
        val builder = AlertDialog.Builder(this@PlayActivity)
        val view = LayoutInflater.from(this@PlayActivity).inflate(R.layout.time_over, null)
        builder.setView(view)
        val timeOverOk = view.findViewById<Button>(R.id.timeOver_ok)
        val alertDialog = builder.create()
        timeOverOk.setOnClickListener {
            tempoRestanteMilisegundos = countDownMilisegundos
            startCountDownTimer()
            alertDialog.dismiss()
        }
        alertDialog.show()
    }

    @SuppressLint("SetTextI18n")
    private fun trollAlertDialog() {
        val builder = AlertDialog.Builder(this@PlayActivity)
        val view = LayoutInflater.from(this@PlayActivity).inflate(R.layout.troll_dialog, null)
        builder.setView(view)
        val deyvinhoOk = view.findViewById<Button>(R.id.deyvinho_ok)
        val alertDialog = builder.create()
        playTrollAudio()
        deyvinhoOk.setOnClickListener {
            tempoRestanteMilisegundos = countDownMilisegundos
            startCountDownTimer()
            alertDialog.dismiss()
        }
        alertDialog.show()
    }

    private fun playTrollAudio() {
        if (trollAudio == null) {
            trollAudio = MediaPlayer.create(this, R.raw.among)
            trollAudio!!.isLooping = false
            trollAudio!!.start()
        } else trollAudio!!.start()
    }
}