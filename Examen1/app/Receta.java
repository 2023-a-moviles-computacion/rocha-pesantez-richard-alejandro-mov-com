


public class Receta(
    val id: Int,
    var nombre: String?,
    var porciones: Int,
    var calorias: Float,
    var creacion: Date,
    var facil: Boolean,
    var ingredientes: Array<String>,
    var preparacion: String
) {
    companion object {
        private const val archivo_recetas = "C:\\Users\\escritorio.virtual6\\Documents\\GitHub\\chicaiza-sandovalin-kharol-victoria-mov-com\\Tareas\\CRUD_Kotlin_Kharol_Chicaiza\\src\\main\\kotlin\\recetas.txt"

        fun desplegarRecetas(): List<Receta> {
            val archivoRecetas = File(archivo_recetas)
            val gson = Gson()
            val lineas = archivoRecetas.readLines()
            val recetas = mutableListOf<Receta>()

            for (linea in lineas) {
                val receta = gson.fromJson(linea, Receta::class.java)
                recetas.add(receta)
            }

            return recetas
        }

        fun crearReceta(receta: Receta) {
            val recetas = desplegarRecetas().toMutableList()
            recetas.add(receta)
            guardarReceta(recetas)
        }

        fun idDisponible(): Int {
            val recetas = desplegarRecetas()
            return recetas.map { it.id }.maxOrNull()?.plus(1) ?: 1
        }

        fun borrarReceta(id: Int) {
            val recetas = desplegarRecetas().toMutableList()
            val recetaBorrar = recetas.find { it.id == id }
            if (recetaBorrar != null) {
                recetas.remove(recetaBorrar)
                guardarReceta(recetas)
            }
        }

        fun guardarReceta(recetas: List<Receta>) {
            val archivoRecetas = File(archivo_recetas)
            val gson = Gson()
            val lineas = recetas.map { gson.toJson(it) }
            archivoRecetas.writeText(lineas.joinToString("\n"))
        }
        fun actualizarReceta(receta: Receta) {
            val recetas = Receta.desplegarRecetas().toMutableList()
            val index = recetas.indexOfFirst { it.id == receta.id }
            if (index != -1) {
                recetas[index] = receta
                Receta.guardarReceta(recetas)
            }
        }
    }

    override fun toString(): String {
        val formato = SimpleDateFormat("yyyy-MM-dd")
        val fecha = formato.format(creacion)
        return """
            Receta #$id
            Nombre: $nombre
            Fecha de creación: $fecha
            Porciones: $porciones
            Calorías: $calorias
            Fácil: $facil
            Ingredientes: ${ingredientes.joinToString(", ")}
            Preparación: $preparacion
        """.trimIndent()
    }
}


