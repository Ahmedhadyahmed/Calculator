package com.example.calc

import android.os.Bundle
import android.view.WindowManager
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.children
import com.example.calc.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private var firstNumber = ""
    private var currentNumber = ""
    private var currentOperator = ""
    private var result = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Enable edge-to-edge layout
        window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)

        // Initialize buttons and set their click listeners
        binding.apply {
            binding.layoutMain.children.filterIsInstance<Button>().forEach { button ->
                button.setOnClickListener {
                    handleButtonClick(button.text.toString())
                }
            }
        }
    }

    // Handle button clicks
    private fun handleButtonClick(buttonText: String) {
        when {
            buttonText.matches(Regex("[0-9]")) -> handleNumberInput(buttonText)
            buttonText.matches(Regex("[+\\-*/]")) -> handleOperatorInput(buttonText)
            buttonText == "=" -> handleEquals()
            buttonText == "." -> handleDecimalPoint()
            buttonText == "c" -> handleClear()
        }
    }

    // Handle number input
    private fun handleNumberInput(buttonText: String) {
        if (currentOperator.isEmpty()) {
            firstNumber += buttonText
            binding.tvResult.text = firstNumber
        } else {
            currentNumber += buttonText
            binding.tvResult.text = currentNumber
        }
        updateFormula()
    }

    // Handle operator input
    private fun handleOperatorInput(buttonText: String) {
        if (firstNumber.isNotEmpty() && currentNumber.isEmpty()) {
            currentOperator = buttonText
            updateFormula()
            binding.tvResult.text = "0"
        }
    }

    // Handle equals button to perform calculation
    private fun handleEquals() {
        if (currentNumber.isNotEmpty() && currentOperator.isNotEmpty()) {
            binding.tvFormula.text = "$firstNumber $currentOperator $currentNumber"
            result = evaluateExpression(firstNumber, currentNumber, currentOperator)
            firstNumber = result
            binding.tvResult.text = result
            currentOperator = ""
            currentNumber = ""
        }
    }

    // Handle decimal point input
    private fun handleDecimalPoint() {
        if (currentOperator.isEmpty()) {
            if (!firstNumber.contains(".")) {
                if (firstNumber.isEmpty()) firstNumber += "0."
                else firstNumber += "."
                binding.tvResult.text = firstNumber
            }
        } else {
            if (!currentNumber.contains(".")) {
                if (currentNumber.isEmpty()) currentNumber += "0."
                else currentNumber += "."
                binding.tvResult.text = currentNumber
            }
        }
        updateFormula()
    }

    // Handle clear button to reset the calculator
    private fun handleClear() {
        currentNumber = ""
        firstNumber = ""
        binding.tvResult.text = "0"
        binding.tvFormula.text = ""
        currentOperator = ""
    }

    // Update the formula display at the top
    private fun updateFormula() {
        if (currentNumber.isEmpty()) {
            binding.tvFormula.text = if (currentOperator.isEmpty()) firstNumber else "$firstNumber $currentOperator"
        } else {
            binding.tvFormula.text = "$firstNumber $currentOperator $currentNumber"
        }
    }

    // Evaluate the expression based on the operator
    private fun evaluateExpression(firstNumber: String, secondNumber: String, operator: String): String {
        return try {
            val num1 = firstNumber.toDouble()
            val num2 = secondNumber.toDouble()
            when (operator) {
                "+" -> (num1 + num2).toString()
                "-" -> (num1 - num2).toString()
                "*" -> (num1 * num2).toString()
                "/" -> if (num2 != 0.0) (num1 / num2).toString() else "Error"
                else -> ""
            }
        } catch (e: NumberFormatException) {
            "Error"
        }
    }
}
