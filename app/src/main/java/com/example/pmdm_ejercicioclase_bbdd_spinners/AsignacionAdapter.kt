package com.example.pmdm_ejercicioclase_bbdd_spinners

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.pmdm_ejercicioclase_bbdd_spinners.databinding.ItemAsignacionBinding

// Clase de datos simple para guardar la info de cada fila
data class Asignacion(val persona: String, val empleo: String)

class AsignacionAdapter(private val lista: List<Asignacion>) : RecyclerView.Adapter<AsignacionAdapter.ViewHolder>() {

    class ViewHolder(val binding: ItemAsignacionBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // Inflamos el diseño de la fila (item_asignacion.xml)
        val binding = ItemAsignacionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // Ponemos el texto en los TextViews
        val item = lista[position]
        holder.binding.tvNombrePersonaItem.text = item.persona
        holder.binding.tvNombreEmpleoItem.text = item.empleo
    }

    override fun getItemCount() = lista.size
}