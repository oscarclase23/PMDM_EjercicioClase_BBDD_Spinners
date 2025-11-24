package com.example.pmdm_ejercicioclase_bbdd_spinners

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class AdminSQLiteOpenHelper(context: Context) : SQLiteOpenHelper(context, "GestionEmpleos.db", null, 1) {

    override fun onCreate(db: SQLiteDatabase) {
        // 1. Tabla EMPLEOS
        // idEmpleo: Clave primaria (número automático)
        // nombreEmpleo: Texto
        db.execSQL("CREATE TABLE Empleos (idEmpleo INTEGER PRIMARY KEY AUTOINCREMENT, nombreEmpleo TEXT)")

        // 2. Tabla PERSONAS
        // idPersona: Clave primaria
        // nombrePersona: Texto
        // idEmpleo: Clave foránea (guardará el ID del empleo asignado). Al principio puede estar vacío.
        db.execSQL("CREATE TABLE Personas (idPersona INTEGER PRIMARY KEY AUTOINCREMENT, nombrePersona TEXT, idEmpleo INTEGER)")
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // Si cambiamos la versión de la app, borramos y creamos de nuevo (para limpiar errores antiguos)
        db.execSQL("DROP TABLE IF EXISTS Personas")
        db.execSQL("DROP TABLE IF EXISTS Empleos")
        onCreate(db)
    }
}