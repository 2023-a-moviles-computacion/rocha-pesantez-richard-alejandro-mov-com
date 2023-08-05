package com.example.recetario
import java.text.SimpleDateFormat
import java.util.*

class Coctel(
    val id: Int,
    var nombre: String?,
    var contenido: Int?,
    var precio: Double?,
    var creacion: Date?,
    var esAlcoholica: Boolean?,
    var ingredientes: Array<String>?,
    var preparacion: String?
) {

    override fun toString(): String {
        val formato = SimpleDateFormat("yyyy-MM-dd")
        val fecha = formato.format(creacion)
        return "\n\tCoctel #${id} \nNombre: ${nombre} \nFecha de creación: ${fecha} \nContenido(mL): ${contenido} \nPrecio: ${precio} \nTiene alcohol: ${esAlcoholica} \nIngredientes: ${ingredientes?.joinToString(", ")} \nPreparación: ${preparacion}\n"
    }
}
