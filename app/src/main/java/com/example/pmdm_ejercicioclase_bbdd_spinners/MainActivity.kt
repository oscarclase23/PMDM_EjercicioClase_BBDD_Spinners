package com.example.pmdm_ejercicioclase_bbdd_spinners

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.pmdm_ejercicioclase_bbdd_spinners.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Configurar Binding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Ajuste visual de bordes (lo que ya tenías)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // --- BOTÓN 1: AÑADIR PERSONA ---
        binding.btnAddPersona.setOnClickListener {
            val nombre = binding.etNombrePersona.text.toString()

            if (nombre.isNotEmpty()) {
                val admin = AdminSQLiteOpenHelper(this)
                val db = admin.writableDatabase

                val registro = ContentValues()
                registro.put("nombrePersona", nombre)
                // No metemos idEmpleo porque al principio no tiene empleo asignado

                db.insert("Personas", null, registro)
                db.close()

                binding.etNombrePersona.setText("") // Limpiamos el campo
                Toast.makeText(this, "Persona guardada OK", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Escribe un nombre", Toast.LENGTH_SHORT).show()
            }
        }

        // --- BOTÓN 2: AÑADIR EMPLEO ---
        binding.btnAddEmpleo.setOnClickListener {
            val nombreEmpleo = binding.etNombreEmpleo.text.toString()

            if (nombreEmpleo.isNotEmpty()) {
                val admin = AdminSQLiteOpenHelper(this)
                val db = admin.writableDatabase

                val registro = ContentValues()
                registro.put("nombreEmpleo", nombreEmpleo)

                db.insert("Empleos", null, registro)
                db.close()

                binding.etNombreEmpleo.setText("") // Limpiamos el campo
                Toast.makeText(this, "Empleo guardado OK", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Escribe un empleo", Toast.LENGTH_SHORT).show()
            }
        }

        // --- BOTÓN 3: IR A ASIGNACIÓN ---
        binding.btnAsignacion.setOnClickListener {
            val intent = Intent(this, AssignmentActivity::class.java)
            startActivity(intent)
        }
    }
}