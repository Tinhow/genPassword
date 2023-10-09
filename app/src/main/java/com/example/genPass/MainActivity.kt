package com.example.genPass

import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import com.example.genPass.Password
import com.example.genPass.PasswordConfigActivity
import com.example.genPass.R
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {

    private lateinit var listView: ListView
    private lateinit var passwordList: MutableList<Password>
    private lateinit var adapter: ArrayAdapter<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        listView = findViewById(R.id.listView)
        passwordList = mutableListOf()
        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, passwordList.map { it.description })
        listView.adapter = adapter

        val fab = findViewById<FloatingActionButton>(R.id.buttonAdd)
        fab.setOnClickListener {
            val intent = Intent(this, PasswordConfigActivity::class.java)
            startActivityForResult(intent, REQUEST_PASSWORD_CONFIG)
        }

        // Define o evento de clique em um item da lista para copiar a senha para a área de transferência
        listView.setOnItemClickListener { _, _, position, _ ->
            val selectedPassword = passwordList[position]
            // Redirecionar para a tela de edição com os detalhes da senha selecionada
            val intent = Intent(this, PasswordConfigActivity::class.java)
            intent.putExtra("selectedPassword", selectedPassword)
            intent.putExtra("position", position)
            startActivityForResult(intent, REQUEST_PASSWORD_CONFIG)
        }

        listView.setOnItemLongClickListener { _, _, position, _ ->
            val selectedPassword = passwordList[position]

            // Copie a senha para a área de transferência
            copyToClipboard(selectedPassword.description)
            true
        }
    }

    private fun copyToClipboard(text: String) {
        val clipboard = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("Senha", text)
        clipboard.setPrimaryClip(clip)
        Toast.makeText(this, "Senha copiada para a área de transferência", Toast.LENGTH_SHORT).show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_PASSWORD_CONFIG && resultCode == Activity.RESULT_OK) {
            val generatedPassword = data?.getStringExtra("generatedPassword")
            val updatedPassword = data?.getParcelableExtra<Password>("updatedPassword")
            val isPasswordDeleted = data?.getBooleanExtra("deletedPassword", false)

            if (generatedPassword != null) {
                val newPassword = Password(generatedPassword, 0, false, false, false)
                passwordList.add(newPassword) // Adiciona a senha à lista
                adapter.add(newPassword.description)
                adapter.notifyDataSetChanged() // Atualiza a lista com a senha gerada
            } else if (updatedPassword != null) {
                val position = data.getIntExtra("position", -1)
                if (position != -1) {
                    passwordList[position] = updatedPassword
                    adapter.clear()
                    adapter.addAll(passwordList.map { it.description })
                    adapter.notifyDataSetChanged() // Atualiza a lista com a senha atualizada
                }
            } else if (isPasswordDeleted == true) {
                val position = data.getIntExtra("position", -1)
                if (position != -1) {
                    passwordList.removeAt(position)
                    adapter.clear()
                    adapter.addAll(passwordList.map { it.description })
                    adapter.notifyDataSetChanged() // Atualiza a lista após a exclusão
                }
            }
        }
    }

    companion object {
        private const val REQUEST_PASSWORD_CONFIG = 1
    }
}
