package com.example.calculadora


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView


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

    private var canAddOperation = false
    private var canAddDecimal = true
    private var memoryValue = 0.0






    fun numberAction(view: View)
    {
        val workingsTV = findViewById<TextView>(R.id.espacoDigitos)

        if(view is Button)
        {
            if(view.text == ".")
            {
                if(canAddDecimal)
                    workingsTV.append(view.text)

                canAddDecimal = false
            }
            else
                workingsTV.append(view.text)

            canAddOperation = true
        }
    }


    fun operationAction(view: View)
    {
        val workingsTV = findViewById<TextView>(R.id.espacoDigitos)
        if(view is Button && canAddOperation)
        {
            workingsTV.append(view.text)
            canAddOperation = false
            canAddDecimal = true
        }
    }



    //Acha os campos de resultado e digitação, seta os 2 para strings vazias
    fun allClearAction(view: View){
        val workingsTV = findViewById<TextView>(R.id.espacoDigitos)
        val resultsTv = findViewById<TextView>(R.id.resultados)

        workingsTV.text = ""
        resultsTv.text = ""

    }

    fun backSpaceAction(view: View){

        val workingsTV = findViewById<TextView>(R.id.espacoDigitos)

        val length = workingsTV.length()
        if(length > 0)
            workingsTV.text = workingsTV.text.subSequence(0, length - 1)
    }

    private fun calculateResults(): String
    {
        val digitsOperators = digitsOperators()
        if(digitsOperators.isEmpty()) return ""

        val timesDivision = timesDivisionCalculate(digitsOperators)
        if(timesDivision.isEmpty()) return ""

        val result = addSubtractCalculate(timesDivision)
        return result.toString()
    }

    private fun addSubtractCalculate(passedList: MutableList<Any>): Float
    {
        var result = passedList[0] as Float

        for(i in passedList.indices)
        {
            if(passedList[i] is Char && i != passedList.lastIndex)
            {
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

    private fun timesDivisionCalculate(passedList: MutableList<Any>): MutableList<Any>
    {
        var list = passedList
        while (list.contains('x') || list.contains('/'))
        {
            list = calcTimesDiv(list)
        }
        return list
    }

    private fun calcTimesDiv(passedList: MutableList<Any>): MutableList<Any>
    {
        val newList = mutableListOf<Any>()
        var restartIndex = passedList.size

        for(i in passedList.indices)
        {
            if(passedList[i] is Char && i != passedList.lastIndex && i < restartIndex)
            {
                val operator = passedList[i]
                val prevDigit = passedList[i - 1] as Float
                val nextDigit = passedList[i + 1] as Float
                when(operator)
                {
                    'x' ->
                    {
                        newList.add(prevDigit * nextDigit)
                        restartIndex = i + 1
                    }
                    '/' ->
                    {
                        newList.add(prevDigit / nextDigit)
                        restartIndex = i + 1
                    }
                    else ->
                    {
                        newList.add(prevDigit)
                        newList.add(operator)
                    }
                }
            }

            if(i > restartIndex)
                newList.add(passedList[i])
        }

        return newList
    }

    private fun digitsOperators(): MutableList<Any>
    {
        val workingsTV = findViewById<TextView>(R.id.espacoDigitos)
        val list = mutableListOf<Any>()
        var currentDigit = ""
        for(character in workingsTV.text)
        {
            if(character.isDigit() || character == '.')
                currentDigit += character
            else
            {
                list.add(currentDigit.toFloat())
                currentDigit = ""
                list.add(character)
            }
        }

        if(currentDigit != "")
            list.add(currentDigit.toFloat())

        return list
    }


    fun equalsAction(view: View) {
        val resultsTV = findViewById<TextView>(R.id.resultados)
        resultsTV.text = calculateResults()
    }

    fun addToMemory(view: View) {
        val workingsTV = findViewById<TextView>(R.id.espacoDigitos)

        // Verifica se o texto atual no TextView de trabalho é um número válido
        val currentText = workingsTV.text.toString()
        if (currentText.isNotEmpty()) {
            val currentValue = currentText.toDouble()
            memoryValue += currentValue
        }
    }

    fun subtractFromMemory(view: View) {
        val workingsTV = findViewById<TextView>(R.id.espacoDigitos)

        // Verifica se o texto atual no TextView de trabalho é um número válido
        val currentText = workingsTV.text.toString()
        if (currentText.isNotEmpty()) {
            val currentValue = currentText.toDouble()
            memoryValue -= currentValue
        }
    }

    fun recallMemory(view: View) {
        val workingsTV = findViewById<TextView>(R.id.espacoDigitos)

        // Exibe o valor da memória no TextView de trabalho
        workingsTV.text = memoryValue.toString()
    }


}