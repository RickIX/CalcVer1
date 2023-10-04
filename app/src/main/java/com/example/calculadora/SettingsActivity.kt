package com.example.calculadora

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Switch
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate

class SettingsActivity : AppCompatActivity() {

    @SuppressLint("UseSwitchCompatOrMaterialCode")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        // Encontre o elemento Switch na interface do usuário
        val switchDarkMode = findViewById<Switch>(R.id.switchDarkMode)

        // Defina o estado inicial do Switch com base nas preferências de modo noturno do aplicativo
        switchDarkMode.isChecked = AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES

        // Configure um ouvinte de alterações para o Switch, para alternar entre os modos claro e escuro
        switchDarkMode.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                // Ative o Modo Escuro do aplicativo
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                // Exiba uma mensagem de feedback curta
                showToast("Modo Escuro ativado")
            } else {
                // Desative o Modo Escuro do aplicativo
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                // Exiba uma mensagem de feedback curto
                showToast("Modo Claro ativado")
            }
        }
    }

    // Método para exibir uma mensagem de feedback curta
    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
