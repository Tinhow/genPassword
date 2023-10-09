package com.example.genPass

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.SeekBar
import android.widget.TextView
import com.example.genPass.Password
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
    private var selectedPassword: Password? = null
    private var position: Int = -1

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

        lengthSeekBar.min = 4
        lengthSeekBar.max = 20
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

        selectedPassword = intent.getParcelableExtra<Password>("selectedPassword")
        position = intent.getIntExtra("position", -1)

        if (selectedPassword != null) {
            populateFields(selectedPassword!!)

            // Exibe os botões de alterar e excluir
            alterButton.visibility = View.VISIBLE
            deleteButton.visibility = View.VISIBLE

            // Esconde o botão "Gerar" no modo de edição
            generateButton.visibility = View.INVISIBLE

            // Configura o clique do botão "Alterar"
            alterButton.setOnClickListener {
                val updatedPassword = createUpdatedPassword()
                returnUpdatedPassword(updatedPassword)
            }

            // Configura o clique do botão "Excluir"
            deleteButton.setOnClickListener {
                returnDeletedPassword()
            }
        } else {
            // Modo de criação de senha, esconde os botões de alterar e excluir
            alterButton.visibility = View.GONE
            deleteButton.visibility = View.GONE

            generateButton.setOnClickListener {
                val generatedPassword = generatePassword()
                returnPassword(generatedPassword)
            }
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

    private fun populateFields(selectedPassword: Password) {
        // Preenche os campos com os dados da senha existente
        descriptionEditText.setText(selectedPassword.description)
        lengthSeekBar.progress = selectedPassword.length
        uppercaseCheckBox.isChecked = selectedPassword.includeUppercase
        numbersCheckBox.isChecked = selectedPassword.includeNumbers
        specialCharsCheckBox.isChecked = selectedPassword.includeSpecialChars
    }

    private fun createUpdatedPassword(): Password {
        val updatedDescription = descriptionEditText.text.toString()
        val updatedLength = lengthSeekBar.progress
        val updatedUppercase = uppercaseCheckBox.isChecked
        val updatedNumbers = numbersCheckBox.isChecked
        val updatedSpecialChars = specialCharsCheckBox.isChecked

        return Password(
            updatedDescription,
            updatedLength,
            updatedUppercase,
            updatedNumbers,
            updatedSpecialChars
        )
    }

    private fun returnPassword(password: String) {
        val resultIntent = Intent()
        resultIntent.putExtra("generatedPassword", password)
        setResult(Activity.RESULT_OK, resultIntent)
        finish()
    }

    private fun returnUpdatedPassword(updatedPassword: Password) {
        val resultIntent = Intent()
        resultIntent.putExtra("updatedPassword", updatedPassword)
        resultIntent.putExtra("position", position) // Passa a posição de volta
        setResult(Activity.RESULT_OK, resultIntent)
        finish()
    }

    private fun returnDeletedPassword() {
        val resultIntent = Intent()
        resultIntent.putExtra("deletedPassword", true)
        resultIntent.putExtra("position", position) // Passa a posição de volta
        setResult(Activity.RESULT_OK, resultIntent)
        finish()
    }
}
