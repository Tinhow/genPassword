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
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {

    private lateinit var listView: ListView
    private lateinit var passwordList: MutableList<String>
    private lateinit var adapter: ArrayAdapter<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        listView = findViewById(R.id.listView)
        passwordList = mutableListOf()
        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, passwordList)
        listView.adapter = adapter

        val fab = findViewById<FloatingActionButton>(R.id.buttonAdd)
        fab.setOnClickListener {
            val intent = Intent(this, PasswordConfigActivity::class.java)
            startActivityForResult(intent, REQUEST_PASSWORD_CONFIG)
        }

        // Define o evento de clique em um item da lista para copiar a senha para a área de transferência
        listView.setOnItemClickListener { _, _, position, _ ->
            val selectedPassword = passwordList[position]
            copyToClipboard(selectedPassword)
        }

        listView.setOnItemLongClickListener { _, _, position, _ ->
            val selectedPassword = passwordList[position]

            val intent = Intent(this, PasswordConfigActivity::class.java)
            intent.putExtra("selectedPassword", selectedPassword)
            startActivityForResult(intent, REQUEST_PASSWORD_CONFIG)
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

        if (requestCode == REQUEST_PASSWORD_CONFIG) {
            if (resultCode == Activity.RESULT_OK) {
                val generatedPassword = data?.getStringExtra("generatedPassword")
                val updatedPassword = data?.getStringExtra("updatedPassword")

                if (!generatedPassword.isNullOrEmpty()) {
                    passwordList.add(generatedPassword)
                } else if (!updatedPassword.isNullOrEmpty()) {
                    val position = data?.getIntExtra("position", -1)
                    if (position != null && position >= 0) {
                        passwordList[position] = updatedPassword
                    }
                }
                adapter.notifyDataSetChanged()
            }
        }
    }

    companion object {
        private const val REQUEST_PASSWORD_CONFIG = 1
    }
}
