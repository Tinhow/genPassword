package com.example.genPass

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.CheckBox
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import java.util.Random

class PasswordConfigActivity : AppCompatActivity() {

    private lateinit var descriptionEditText: TextInputEditText
    private lateinit var lengthSeekBar: SeekBar
    private lateinit var uppercaseCheckBox: CheckBox
    private lateinit var numbersCheckBox: CheckBox
    private lateinit var specialCharsCheckBox: CheckBox
    private lateinit var generateButton: Button
    private lateinit var alterButton: Button
    private lateinit var deleteButton: Button
    private lateinit var cancelButton: Button
    private lateinit var seekBarValueTextView: TextView

    private val passwordList = mutableListOf<Password>()
    private lateinit var passwordListAdapter: ArrayAdapter<Password>

    @SuppressLint("NewApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.password_config_activity)

        descriptionEditText = findViewById(R.id.textDescription)
        lengthSeekBar = findViewById(R.id.seekBar)
        uppercaseCheckBox = findViewById(R.id.uppercaseCheckBox)
        numbersCheckBox = findViewById(R.id.numbersCheckBox)
        specialCharsCheckBox = findViewById(R.id.specialCharsCheckBox)
        generateButton = findViewById(R.id.generateButton)
        alterButton = findViewById(R.id.alterButton)
        deleteButton = findViewById(R.id.deleteButton)
        cancelButton = findViewById(R.id.cancelButton)
        seekBarValueTextView = findViewById(R.id.seekBarValueTextView)

        //define o valor do seekbar
        lengthSeekBar.min = 4
        lengthSeekBar.max = 20
        // Define o valor inicial para o SeekBar
        lengthSeekBar.progress = 4

        lengthSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                seekBarValueTextView.text = progress.toString()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                // Não é necessário implementar nada aqui
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                // Não é necessário implementar nada aqui
            }
        })


        generateButton.setOnClickListener {
            // Implemente a lógica para gerar a senha com base nas configurações especificadas
            val generatedPassword = generatePassword()

            // Crie um Intent para retornar a senha gerada
            val resultIntent = Intent()
            resultIntent.putExtra("generatedPassword", generatedPassword)
            setResult(Activity.RESULT_OK, resultIntent)

            finish()
        }


        alterButton.setOnClickListener {
            // Implemente a lógica para atualizar a senha com base nas configurações especificadas
            val updatedPassword = generatePassword()

            // Retorne a senha atualizada para a MainActivity
            val resultIntent = Intent()
            resultIntent.putExtra("updatedPassword", updatedPassword)
            setResult(Activity.RESULT_OK, resultIntent)

            // Finalize a atividade da PasswordConfigActivity e retorne à MainActivity
            finish()
        }

        deleteButton.setOnClickListener {
            // Implemente a lógica para excluir a senha (você precisa implementar isso)
            // Retorne uma indicação de que a senha foi excluída para a MainActivity
        }

        cancelButton.setOnClickListener {
            setResult(Activity.RESULT_CANCELED)
            finish()
        }
    }

    private fun generatePassword(): String {
        val length = lengthSeekBar.progress
        val useUppercase = uppercaseCheckBox.isChecked
        val useNumbers = numbersCheckBox.isChecked
        val useSpecialChars = specialCharsCheckBox.isChecked

        val random = Random()

        // Lista de grupos de caracteres
        val characterGroups = mutableListOf<String>()

        if (useUppercase) {
            characterGroups.add("ABCDEFGHIJKLMNOPQRSTUVWXYZ")
        }
        if (useNumbers) {
            characterGroups.add("0123456789")
        }
        if (useSpecialChars) {
            characterGroups.add("!@#$%&*+")
        }

        // Se nenhum grupo estiver selecionado, misture todos os grupos e inclua letras maiúsculas
        if (characterGroups.isEmpty()) {
            characterGroups.add("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!@#$%^&*+")
        }

        val passwordBuilder = StringBuilder()

        // Gere a senha misturando os grupos de caracteres selecionados
        for (i in 0 until length) {
            // Escolha aleatoriamente um grupo de caracteres
            val randomGroupIndex = random.nextInt(characterGroups.size)
            val randomGroup = characterGroups[randomGroupIndex]

            // Escolha aleatoriamente um caractere do grupo selecionado
            val randomCharIndex = random.nextInt(randomGroup.length)
            val randomChar = randomGroup[randomCharIndex]

            passwordBuilder.append(randomChar)
        }

        return passwordBuilder.toString()
    }
}
