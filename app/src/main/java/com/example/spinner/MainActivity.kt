package com.example.spinner

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    lateinit var mainTB: Toolbar
    lateinit var roleS: Spinner
    lateinit var saveBTN: Button
    lateinit var nameET: EditText
    lateinit var surnameET: EditText
    lateinit var ageET: EditText
    lateinit var personsLV: ListView
    lateinit var roleToolbarS: Spinner
    var selectedRole: String = ""
    var persons: MutableList<Person> = mutableListOf()
    var role = mutableListOf(
        "Инженер",
        "Уборщик",
        "Начальник",
        "Слесарь"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        mainTB = findViewById(R.id.mainTB)
        roleS = findViewById(R.id.roleS)
        saveBTN = findViewById(R.id.saveBTN)
        nameET = findViewById(R.id.nameET)
        surnameET = findViewById(R.id.surnameET)
        ageET = findViewById(R.id.ageET)
        personsLV = findViewById(R.id.personsLV)
        roleToolbarS = findViewById(R.id.roleToolbarS)
        setSupportActionBar(mainTB)
        title = "Подбор персонала"
        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            role
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        roleS.adapter = adapter
        roleToolbarS.adapter=adapter
        roleS.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                selectedRole = parent?.getItemAtPosition(position).toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                selectedRole = ""
            }
        }
        val listAdapter = ListAdapter(this, persons)
        personsLV.adapter = listAdapter
        personsLV.setOnItemClickListener { parent, view, position, id ->
            val person = persons[position]
            persons.removeAt(position)
            listAdapter.notifyDataSetChanged()
            Toast.makeText(
                this,
                "Пользователь ${person.name} ${person.surname} удален",
                Toast.LENGTH_LONG
            ).show()
        }

        saveBTN.setOnClickListener {
            val name = nameET.text.toString()
            val surname = surnameET.text.toString()
            val age = ageET.text.toString()
            val person = Person(name, surname, age, selectedRole)
            persons.add(person)
            nameET.text.clear()
            surnameET.text.clear()
            ageET.text.clear()
            selectedRole = role[0]
            listAdapter.notifyDataSetChanged()
        }
        roleToolbarS.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    val selectedPosition = parent.getItemAtPosition(position).toString()
                    val filteredList = persons.filter { it.role == selectedPosition }
                    listAdapter.clear()
                    listAdapter.addAll(filteredList)
                    listAdapter.notifyDataSetChanged()
                }

                override fun onNothingSelected(parent: AdapterView<*>) {}
            }
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.exitApp -> {
                finishAffinity()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

}


