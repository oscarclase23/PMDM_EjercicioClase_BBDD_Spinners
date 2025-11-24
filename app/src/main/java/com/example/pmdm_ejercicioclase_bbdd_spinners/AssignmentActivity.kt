package com.example.pmdm_ejercicioclase_bbdd_spinners

import android.R.layout.simple_spinner_item
import android.database.Cursor
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pmdm_ejercicioclase_bbdd_spinners.databinding.ActivityAssignmentBinding

class AssignmentActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAssignmentBinding
    private lateinit var dbHelper: AdminSQLiteOpenHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAssignmentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dbHelper = AdminSQLiteOpenHelper(this)

        // Configurar el RecyclerView (La lista)
        binding.rvAsignaciones.layoutManager = LinearLayoutManager(this)

        // 1. Cargar datos en los desplegables al iniciar
        cargarSpinners()

        // 2. Cargar la lista de asignaciones existentes
        cargarListaAsignaciones()

        // 3. Acción del botón INSERTAR
        binding.btnInsertarAsignacion.setOnClickListener {
            realizarAsignacion()
        }
    }

    private fun cargarSpinners() {
        val db = dbHelper.readableDatabase

        // --- SPINNER PERSONAS ---
        val cursorP = db.rawQuery("SELECT nombrePersona FROM Personas", null)
        val listaPersonas = ArrayList<String>()
        while (cursorP.moveToNext()) {
            listaPersonas.add(cursorP.getString(0))
        }
        cursorP.close()

        val adapterP = ArrayAdapter(this, simple_spinner_item, listaPersonas)
        adapterP.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerPersonas.adapter = adapterP

        // --- SPINNER EMPLEOS ---
        val cursorE = db.rawQuery("SELECT nombreEmpleo FROM Empleos", null)
        val listaEmpleos = ArrayList<String>()
        while (cursorE.moveToNext()) {
            listaEmpleos.add(cursorE.getString(0))
        }
        cursorE.close()

        val adapterE = ArrayAdapter(this, simple_spinner_item, listaEmpleos)
        adapterE.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerEmpleos.adapter = adapterE

        db.close()
    }

    private fun realizarAsignacion() {
        // Validamos que haya datos seleccionados
        if (binding.spinnerPersonas.selectedItem == null || binding.spinnerEmpleos.selectedItem == null) {
            Toast.makeText(this, "Selecciona persona y empleo", Toast.LENGTH_SHORT).show()
            return
        }

        val personaSeleccionada = binding.spinnerPersonas.selectedItem.toString()
        val empleoSeleccionado = binding.spinnerEmpleos.selectedItem.toString()

        val db = dbHelper.writableDatabase

        // PASO A: Buscamos el ID del empleo seleccionado
        var idEmpleo = -1
        val cursor = db.rawQuery("SELECT idEmpleo FROM Empleos WHERE nombreEmpleo = ?", arrayOf(empleoSeleccionado))
        if (cursor.moveToFirst()) {
            idEmpleo = cursor.getInt(0)
        }
        cursor.close()

        // PASO B: Actualizamos la tabla Personas para asignarle ese empleo
        if (idEmpleo != -1) {
            // SQL: UPDATE Personas SET idEmpleo = X WHERE nombrePersona = Y
            db.execSQL("UPDATE Personas SET idEmpleo = $idEmpleo WHERE nombrePersona = '$personaSeleccionada'")

            Toast.makeText(this, "Asignación realizada", Toast.LENGTH_SHORT).show()

            // Recargamos la lista para ver el cambio
            cargarListaAsignaciones()
        } else {
            Toast.makeText(this, "Error al buscar el empleo", Toast.LENGTH_SHORT).show()
        }

        db.close()
    }

    private fun cargarListaAsignaciones() {
        val db = dbHelper.readableDatabase

        // CONSULTA JOIN (La clave del ejercicio)
        // Seleccionamos NombrePersona y NombreEmpleo uniendo las tablas por el ID
        val sql = """
            SELECT Personas.nombrePersona, Empleos.nombreEmpleo 
            FROM Personas 
            INNER JOIN Empleos ON Personas.idEmpleo = Empleos.idEmpleo
        """.trimIndent()

        val cursor = db.rawQuery(sql, null)
        val listaVisual = ArrayList<Asignacion>()

        while (cursor.moveToNext()) {
            listaVisual.add(
                Asignacion(
                    cursor.getString(0), // Columna nombrePersona
                    cursor.getString(1)  // Columna nombreEmpleo
                )
            )
        }
        cursor.close()
        db.close()

        // Le pasamos la lista al adaptador
        binding.rvAsignaciones.adapter = AsignacionAdapter(listaVisual)
    }
}