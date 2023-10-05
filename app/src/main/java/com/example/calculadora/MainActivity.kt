package com.example.calculadora

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnSettings = findViewById<Button>(R.id.btnSettings)
        btnSettings.setOnClickListener {
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
        }
    }

    private var operacao = false
    private var decimal = true
    private var memoryValue = 0.0

    // Método chamado quando um botão de número é pressionado
    fun numberAction(view: View) {
        // Obtém uma referência para o TextView com o ID "espacoDigitos" na interface do usuário
        val espacoDigitos = findViewById<TextView>(R.id.espacoDigitos)

        // Verifica se o parâmetro "view" é uma instância de Button
        if (view is Button) {
            // Verifica se o texto do botão é um ponto (.)
            if (view.text == ".") {
                // Se a variável "decimal" for verdadeira (true), então...
                if (decimal)
                // Anexa o texto do botão (neste caso, um ponto) ao conteúdo do TextView "espacoDigitos"
                    espacoDigitos.append(view.text)

                // Define a variável "decimal" como false para impedir a adição de mais pontos
                decimal = false
            } else
            // Se o texto do botão não for um ponto, anexa o texto do botão ao TextView "espacoDigitos"
                espacoDigitos.append(view.text)

            // Define a variável "operacao" como true, indicando que uma operação (número ou operador) foi adicionada
            operacao = true
        }
    }


    // Método chamado quando um botão de operação é pressionado
    fun operationAction(view: View) {
        val espacoDigitos = findViewById<TextView>(R.id.espacoDigitos)
        if (view is Button && operacao) {
            espacoDigitos.append(view.text)
            operacao = false
            decimal = true
        }
    }

    // Método para limpar todos os campos
    fun allClearAction(view: View) {
        val espacoDigitos = findViewById<TextView>(R.id.espacoDigitos)
        val resultados = findViewById<TextView>(R.id.resultados)

        espacoDigitos.text = ""
        resultados.text = ""
    }

    // Método para apagar um caractere do campo de entrada
    fun backSpaceAction(view: View) {
        val espacoDigitos = findViewById<TextView>(R.id.espacoDigitos)

        val length = espacoDigitos.length()
        if (length > 0)
            espacoDigitos.text = espacoDigitos.text.subSequence(0, length - 1)
    }

    // Método para calcular os resultados
    private fun calculateResults(): String {
        val digitsOperators = digitsOperators()
        if (digitsOperators.isEmpty()) return ""

        val timesDivision = timesDivisionCalculate(digitsOperators)
        if (timesDivision.isEmpty()) return ""

        val result = addSubtractCalculate(timesDivision)
        return result.toString()
    }

    // Método para calcular adições e subtrações
    private fun addSubtractCalculate(passedList: MutableList<Any>): Float {
        var result = passedList[0] as Float

        for (i in passedList.indices) {
            if (passedList[i] is Char && i != passedList.lastIndex) {
                val operator = passedList[i]
                val nextDigit = passedList[i + 1] as Float
                if (operator == '+')
                    result += nextDigit
                if (operator == '-')
                    result -= nextDigit
            }
        }

        return result
    }

    // Método para calcular multiplicações e divisões
    private fun timesDivisionCalculate(passedList: MutableList<Any>): MutableList<Any> {
        var list = passedList
        while (list.contains('X') || list.contains('÷')) {
            list = calcTimesDiv(list)
        }
        return list
    }

    // Método auxiliar para calcular multiplicações e divisões
    private fun calcTimesDiv(passedList: MutableList<Any>): MutableList<Any> {
        val newList = mutableListOf<Any>()
        var restartIndex = passedList.size

        for (i in passedList.indices) {
            if (passedList[i] is Char && i != passedList.lastIndex && i < restartIndex) {
                val operator = passedList[i]
                val prevDigit = passedList[i - 1] as Float
                val nextDigit = passedList[i + 1] as Float
                when (operator) {
                    'X' -> {
                        newList.add(prevDigit * nextDigit)
                        restartIndex = i + 1
                    }
                    '÷' -> {
                        newList.add(prevDigit / nextDigit)
                        restartIndex = i + 1
                    }
                    else -> {
                        newList.add(prevDigit)
                        newList.add(operator)
                    }
                }
            }

            if (i > restartIndex)
                newList.add(passedList[i])
        }

        return newList
    }

    // Método para separar dígitos e operadores
    private fun digitsOperators(): MutableList<Any> {
        val espacoDigitos = findViewById<TextView>(R.id.espacoDigitos)
        val list = mutableListOf<Any>()
        var currentDigit = ""
        for (character in espacoDigitos.text) {
            if (character.isDigit() || character == '.')
                currentDigit += character
            else {
                list.add(currentDigit.toFloat())
                currentDigit = ""
                list.add(character)
            }
        }

        if (currentDigit != "")
            list.add(currentDigit.toFloat())

        return list
    }

    // Método chamado quando o botão de igual é pressionado
    fun equalsAction(view: View) {
        val resultados = findViewById<TextView>(R.id.resultados)
        resultados.text = calculateResults()
    }

    // Método para adicionar ao valor na memória
    fun addToMemory(view: View) {
        val espacoDigitos = findViewById<TextView>(R.id.espacoDigitos)

        try {
            // Tenta converter o texto em um número
            val currentText = espacoDigitos.text.toString()
            if (currentText.isNotEmpty()) {
                val currentValue = currentText.toDouble()
                memoryValue += currentValue
            }
        } catch (e: NumberFormatException) {
            // Lida com exceção se o texto não puder ser convertido em um número
            val rootView = findViewById<View>(android.R.id.content)
            Snackbar.make(rootView, "Valor inválido. Não foi possível adicionar à memória.", Snackbar.LENGTH_LONG).show()
        }
    }

    // Método para subtrair do valor na memória
    fun subtractFromMemory(view: View) {
        val espacoDigitos = findViewById<TextView>(R.id.espacoDigitos)
                memoryValue = 0.0

    }

    // Método para recuperar o valor da memória
    fun recallMemory(view: View) {
        val espacoDigitos = findViewById<TextView>(R.id.espacoDigitos)

        // Exibe o valor da memória no TextView de trabalho
        espacoDigitos.text = memoryValue.toString()
    }
}
