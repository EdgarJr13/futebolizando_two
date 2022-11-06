package br.com.edgarjr.futebolizando

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import br.com.edgarjr.futebolizando.databinding.ActivityNameBinding

class NameActivity : AppCompatActivity() {

    private lateinit var activityNameBinding: ActivityNameBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activityNameBinding = ActivityNameBinding.inflate(layoutInflater)
        setContentView(activityNameBinding.root)

        activityNameBinding.btnStart.setOnClickListener {
            if (activityNameBinding.etName.text.toString().isEmpty()) {
                Toast.makeText(this, "Você não inseriu o seu nome!", Toast.LENGTH_SHORT).show()
            } else {
                val intent = Intent(this@NameActivity, PlayActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }
}