package com.example.recetario

import java.text.SimpleDateFormat
import java.util.*

data class Bar(
    val id: Int,
    var nombre: String?,
    var aforo: Int?,
    var area: Double?,
    var fechaIntegracion: Date?,
    var tieneParqueadero: Boolean?,
    var coctels: ArrayList<Coctel>?
) {
    override fun toString(): String {
        val formato = SimpleDateFormat("yyyy-MM-dd")
        val fecha = formato.format(fechaIntegracion)
        return "BAR ID#${id} \nNombre: ${nombre} \nAforo: ${aforo} \nArea(m2): ${area}\nFecha de integración: ${fecha}\nTiene parqueadero: ${tieneParqueadero}\nCocteles: ${coctels.toString()}\n\n"
    }
}
