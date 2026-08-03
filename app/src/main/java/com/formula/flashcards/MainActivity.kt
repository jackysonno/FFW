package com.formula.flashcards

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var etFormula: EditText
    private lateinit var btnAdd: Button
    private lateinit var listFormulas: ListView
    private lateinit var adapter: ArrayAdapter<String>
    private val formulaList = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        etFormula = findViewById(R.id.et_formula)
        btnAdd = findViewById(R.id.btn_add)
        listFormulas = findViewById(R.id.list_formulas)

        loadFormulas()

        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, formulaList)
        listFormulas.adapter = adapter

        btnAdd.setOnClickListener {
            val text = etFormula.text.toString().trim()
            if (text.isNotEmpty()) {
                formulaList.add(text)
                saveFormulas()
                adapter.notifyDataSetChanged()
                etFormula.text.clear()
                updateWidgets()
            }
        }

        listFormulas.setOnItemClickListener { _, _, position, _ ->
            formulaList.removeAt(position)
            saveFormulas()
            adapter.notifyDataSetChanged()
            updateWidgets()
            Toast.makeText(this, "Formula removed", Toast.LENGTH_SHORT).show()
        }
    }

    private fun loadFormulas() {
        val prefs = getSharedPreferences("formulas_pref", Context.MODE_PRIVATE)
        val savedSet = prefs.getStringSet("formulas_set", null)
        formulaList.clear()
        if (savedSet != null && savedSet.isNotEmpty()) {
            formulaList.addAll(savedSet)
        } else {
            formulaList.addAll(
                listOf(
                    "Math: sin^2(x) + cos^2(x) = 1",
                    "Physics: F = m * a",
                    "Chemistry: PV = nRT"
                )
            )
            saveFormulas()
        }
    }

    private fun saveFormulas() {
        val prefs = getSharedPreferences("formulas_pref", Context.MODE_PRIVATE)
        prefs.edit().putStringSet("formulas_set", formulaList.toSet()).apply()
    }

    private fun updateWidgets() {
        val intent = Intent(this, FormulaWidget::class.java).apply {
            action = FormulaWidget.ACTION_NEXT
        }
        sendBroadcast(intent)
    }
}
