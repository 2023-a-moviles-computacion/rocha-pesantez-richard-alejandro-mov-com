package com.example.deber1consola

import com.google.gson.Gson
import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.text.SimpleDateFormat
import java.util.*



val barFile: File by lazy { File("Bar.txt") }
val coctelFile: File by lazy { File("Cocteles.txt") }
fun main() {

    if (!barFile.exists()) {
        barFile.createNewFile()
    }
    if (!coctelFile.exists()) {
        coctelFile.createNewFile()
    }

    val lector = Scanner(System.`in`)

    while (true) {

        println("=====>> BAR - COCTELES <<=====")
        println("1. BAR") //BAR
        println("2. COCTELES") //COCTELES
        println("0. SALIR")
        println("________________________________")
        print("DIGITE SU OPCION -->")
        when (lector.nextInt()) {
            1 -> {
                println("=====>>ADMINISTRAR BAR")
                println("1. Agregar Bar")
                println("2. Mostrar Bares")
                println("3. Actualizar Bar")
                println("4. Borrar Bar")
                println("5. Regresar")
                println("________________________________")
                print("DIGITE SU OPCION -->")


                when (lector.nextInt()) {
                    1 -> {
                        println(">>AGREGAR BAR")
                        print("Nombre:");
                        val nombre = readLine()
                        print("Aforo:");
                        val aforo = lector.nextInt()
                        print("Area:");
                        val areaM2 = lector.nextFloat()
                        print("Fecha de creacion (yyyy-mm-dd):")
                        val fechaCreacionString = readLine()
                        val formato = SimpleDateFormat("yyyy-MM-dd")
                        val fechaCreacion = formato.parse(fechaCreacionString)
                        print("Tiene parqueadero (true/false):")
                        val tieneParqueadero = lector.nextBoolean()

                        //Cocteles
                        val cocteles = mutableListOf<Coctel>()

                        while (true) {
                            print("Deseas agregar un nuevo coctel? (s/n):")
                            val eleccion = lector.next()
                            if (eleccion.equals("s", ignoreCase = true)) {
                                val coctelesExistentes = Coctel.mostrarCocteles()
                                coctelesExistentes.forEach { println(it) }
                                println("Digite el ID del Coctel:")
                                val coctelID = lector.nextInt()
                                val coctelParaAgregar =
                                    Coctel.mostrarCocteles().find { it.id == coctelID }
                                if (coctelParaAgregar != null) {
                                    cocteles.add(coctelParaAgregar)
                                    println("Coctel agregado con exito.")
                                } else {
                                    println("El coctel con el el ID $coctelID no encontrada o ya existe.")
                                }

                            } else {
                                break
                            }
                        }

                        val bar = Bar(
                            Bar.idDisponible(),
                            nombre,
                            aforo,
                            areaM2,
                            fechaCreacion,
                            tieneParqueadero,
                            cocteles.toTypedArray()
                        )

                        Bar.crearBar(bar)
                        println("Bar guardado.")

                    } //agregar bar
                    2 -> {
                        println(">>MOSTRAR BAR")
                        val bares = Bar.mostrarBar()
                        if (bares.isNotEmpty()) {
                            println("Bares:")
                            bares.forEach { println(it) }
                        } else {
                            println("Bar no encontrado.")
                        }
                    } //mostrar
                    3 -> {
                        println(">>ACTUALIZAR BAR")
                        println("Bares existentes:")
                        val baresExistentes = Bar.mostrarBar()
                        baresExistentes.forEach { println(it) }

                        println("\nIngrese el ID del bar para actualizar:")
                        val id = readLine()!!.toInt()
                        val barParaActualizar = baresExistentes.find { it.id == id }

                        if (barParaActualizar != null) {
                            println(
                                "Ingrese el atributo a actualizar (nombre, aforo, " +
                                        "areaM2, creacion, tieneParqueadero, cocteles):"
                            )
                            val atributo = readLine()

                            when (atributo) {
                                "nombre" -> {
                                    println("Nuevo nombre:")
                                    barParaActualizar.nombre = readLine()!!
                                }

                                "aforo" -> {
                                    println("Nueva aforo:")
                                    barParaActualizar.aforo = readLine()!!.toInt()
                                }

                                "areaM2" -> {
                                    println("Nueva area en metros cuadrados:")
                                    barParaActualizar.areaM2 = readLine()!!.toFloat()
                                }

                                "creacion" -> {
                                    println("Fecha de creacion (yyyy-MM-dd):")
                                    val integracionSF = readLine()!!
                                    val formato = SimpleDateFormat("yyyy-MM-dd")
                                    barParaActualizar.creacion = formato.parse(integracionSF)
                                }

                                "tieneParqueadero" -> {
                                    println("Tiene parqueadero (true/false):")
                                    barParaActualizar.tieneParqueadero = readLine()!!.toBoolean()
                                }

                                "cocteles" -> {
                                    println("Número de cocteles:")
                                    val numCocteles = readLine()!!.toInt()
                                    val cocteles = mutableListOf<Coctel>()
                                    for (i in 1..numCocteles) {
                                        println("Coctel $i:")
                                        val recetaId = readLine()!!.toInt()
                                        val receta =
                                            Coctel.mostrarCocteles().find { it.id == recetaId }
                                        if (receta != null) {
                                            cocteles.add(receta)
                                        }
                                    }
                                    barParaActualizar.cocteles = cocteles.toTypedArray()

                                }

                                else -> {
                                    println("Atributo no válido.")
                                    return
                                }
                            }

                            Bar.actualizarBar(barParaActualizar)
                            println("Bar actualizado.")

                        } else {
                            println("El bar con la ID $id, no existe.")
                        }


                    } // actualizar
                    4 -> {
                        println("Bares existentes:")
                        val baresExistentes = Bar.mostrarBar()
                        baresExistentes.forEach { println(it) }

                        println("Digite el ID del bar a borrar:")
                        val id = lector.nextInt()
                        Bar.borrarBar(id)
                        println("Bar borrado de manera exitosa")
                    }  //Borrar
                    5 -> {
                        break
                    }

                    else -> {
                        println("Opcion invalida.")
                    }
                }
            } //admin Bar
            2 -> {
                println("=====>> ADMINISTRACION DE COCTELES")
                println("1. Agregar Coctel")
                println("2. Mostrar Cocteles")
                println("3. Actualizar Coctel")
                println("4. Borrar Coctel")
                println("5. Regresar")
                println("________________________________")
                print("DIGITE SU OPCION -->")
                when (lector.nextInt()) {
                    1 -> {
                        println("=====>> AGREGANDO UN COCTEL")
                        print("Digite los detalles del Coctel\n")
                        print("Nombre:");
                        val nombre = readLine()
                        print("Fecha de creacion (yyyy-MM-dd):")
                        val fechaCreacionString = readLine()
                        val formato = SimpleDateFormat("yyyy-MM-dd")
                        val fechaCreacion = formato.parse(fechaCreacionString)

                        print("Contenido (mL):")
                        val contenido = lector.nextInt()
                        print("Precio: $")
                        val precio = lector.nextFloat()

                        print("Es una bebida alcoholica. ")
                        var esAlcoholica: Boolean = false;
                        var inputValido = false

                        while (!inputValido) {
                            val esAlcoholicaInput = lector.nextLine()
                            if (esAlcoholicaInput.equals("true", ignoreCase = true)) {
                                esAlcoholica = true
                                inputValido = true
                            } else if (esAlcoholicaInput.equals("false", ignoreCase = true)) {
                                esAlcoholica = false
                                inputValido = true
                            } else {
                                println("Por favor, ingresa 'true' o 'false':")
                            }
                        }
                        lector.nextLine() // Consumir el salto de línea anterior
                        print("Ingredientes (separa con una coma):");
                        val ingredientes = lector.nextLine().split(",").map { it.trim() }.toTypedArray()
                        println("Ingredientes ingresados:")
                        for (ingrediente in ingredientes) {
                            println(ingrediente)
                        }
                        print("Preparacion: ")
                        val preparacion: String = lector.nextLine()
                        //println("Ingredientes: ${ingredientes.joinToString()}")
                        val coctel = Coctel(
                            Coctel.idDisponible(),
                            nombre,
                            fechaCreacion,
                            contenido,
                            precio,
                            esAlcoholica,
                            ingredientes,
                            preparacion
                        )
                        Coctel.crearCoctel(coctel)
                        println("Coctel creado con exito.")
                    } //agregar
                    2 -> {
                        val cocteles = Coctel.mostrarCocteles()
                        if (cocteles.isNotEmpty()) {
                            println("Cocteles:")
                            cocteles.forEach { println(it) }
                        } else {
                            println("Cocteles no encontradas.")
                        }
                    } //mostrar
                    3 -> {
                        println("Cocteles existentes:")
                        val coctelesExistentes = Coctel.mostrarCocteles()
                        coctelesExistentes.forEach { println(it) }

                        println("\nIngrese el ID del coctel a actualizar:")
                        val id = readLine()!!.toInt()
                        val coctelParaActualizar = coctelesExistentes.find { it.id == id }

                        if (coctelParaActualizar != null) {
                            println(
                                "Ingrese el atributo a actualizar (nombre, creacion, contenidoML, " +
                                        "precio, esAlcoholica, ingredientes, preparacion):"
                            )
                            val atributo = readLine()

                            when (atributo) {
                                "nombre" -> {
                                    println("Nuevo nombre:")
                                    coctelParaActualizar.nombre = readLine()!!
                                }

                                "creacion" -> {
                                    println("Fecha de creación (yyyy-MM-dd):")
                                    val creacionSF = readLine()!!
                                    val formato = SimpleDateFormat("yyyy-MM-dd")
                                    coctelParaActualizar.creacion = formato.parse(creacionSF)
                                }

                                "precio" -> {
                                    println("Nuevo precio:")
                                    coctelParaActualizar.precio = readLine()!!.toFloat()
                                }

                                "contenidoML" -> {
                                    println("Nuevo contenido (mL):")
                                    coctelParaActualizar.contenidoML = readLine()!!.toInt()
                                }

                                "esAlcoholica" -> {
                                    println("El coctel contiene alcohol? (true/false):")
                                    coctelParaActualizar.esAlcoholica = readLine()!!.toBoolean()
                                }

                                "ingredientes" -> {
                                    println("Número de ingredientes:")
                                    val numIngredientes = readLine()!!.toInt()
                                    val ingredientes = mutableListOf<String>()
                                    for (i in 1..numIngredientes) {
                                        println("Ingrediente $i:")
                                        val ingrediente = readLine()!!
                                        ingredientes.add(ingrediente)
                                    }
                                    coctelParaActualizar.ingredientes = ingredientes.toTypedArray()
                                }

                                "preparacion" -> {
                                    println("Nueva preparación:")
                                    coctelParaActualizar.preparacion = readLine()!!
                                }

                                else -> {
                                    println("Atributo no válido.")
                                    return
                                }
                            }

                            Coctel.actualizarCoctel(coctelParaActualizar)
                            println("Coctel actualizado exitosamente.")
                        } else {
                            println("Coctel con el ID $id no encontrada.")
                        }

                    } //actualizar
                    4 -> {
                        println("Cocteles existentes:")
                        val coctelesExistentes = Coctel.mostrarCocteles()
                        coctelesExistentes.forEach { println(it) }

                        println("Ingrese el ID del coctel a borrar:")
                        val id = lector.nextInt()
                        Coctel.borrarCoctel(id)
                        println("Coctel borrado exitosamente.")
                    } //borrar
                    else -> {
                        println("Opcion invalida.")
                    }

                }

            } //admin cocteles
            0 -> {
                println("Saliendo...")
                return
            } //salir
            else -> {
                println("Opcion invalida.")
            }
        }


    }

}

data class Coctel(
    val id: Int,
    var nombre: String?,
    var creacion: Date,
    var contenidoML: Int,
    var precio: Float,
    var esAlcoholica: Boolean,
    var ingredientes: Array<String>,
    var preparacion: String
) {
    companion object {
        //private val rutaArchivoCoctel = "cocteles.txt"

        fun mostrarCocteles(): List<Coctel> {
            val gson = Gson()
            val lineas = coctelFile.readLines()
            val cocteles = mutableListOf<Coctel>()
            for (lineas in lineas){
                val coctel = gson.fromJson(lineas, Coctel::class.java)
                cocteles.add(coctel)
            }
            /*
            val archivoCoctel = File(rutaArchivoCoctel)
            val gson = Gson()
            val lineas = archivoCoctel.readLines()
            val cocteles = mutableListOf<Coctel>()

            for (linea in lineas) {
                val coctel = gson.fromJson(linea, Coctel::class.java)
                cocteles.add(coctel)
            }
*/
            return cocteles
        }

        fun crearCoctel(coctel: Coctel) {
            val cocteles = mostrarCocteles().toMutableList()
            cocteles.add(coctel)
            guardarCoctel(cocteles)
        }

        fun guardarCoctel(cocteles: List<Coctel>) {
            val gson = Gson()
            val lineas = cocteles.map { gson.toJson(it) }
            coctelFile.writeText(lineas.joinToString("\n"))
            /*
            val archivoCocteles = File(rutaArchivoCoctel)
            val gson = Gson()
            val lineas = cocteles.map { gson.toJson(it) }
            archivoCocteles.writeText(lineas.joinToString("\n"))*/
        }

        fun borrarCoctel(id: Int) {
            val cocteles = mostrarCocteles().toMutableList()
            val coctelBorrar = cocteles.find { it.id == id }
            if (coctelBorrar != null) {
                cocteles.remove(coctelBorrar)
                guardarCoctel(cocteles)
            }
        }

        fun idDisponible(): Int {
            val cocteles = mostrarCocteles()
            return cocteles.map { it.id }.maxOrNull()?.plus(1) ?: 1
        }

        fun actualizarCoctel(coctel: Coctel) {
            val cocteles = mostrarCocteles().toMutableList()
            val index = cocteles.indexOfFirst { it.id == coctel.id }
            if (index != -1) {
                cocteles[index] = coctel
                guardarCoctel(cocteles)
            }
        }

    }

    override fun toString(): String {
        val formato = SimpleDateFormat("yyyy-MM-dd")
        val fecha = formato.format(creacion)
        return """"
            IDCoctel: $id
            Nombre: $nombre
            Fecha creacion: $fecha
            Contenido (ml): $contenidoML
            Precio: $precio
            Bebida alcoholica: $esAlcoholica
            Ingredientes: ${ingredientes.joinToString()}
            Preparacion: $preparacion
        """.trimIndent()
    }
}

data class Bar(
    val id: Int,
    var nombre: String?,
    var aforo: Int,
    var areaM2: Float,
    var creacion: Date,
    var tieneParqueadero: Boolean,
    var cocteles: Array<Coctel>
) {

    companion object {
       // private val rutaArchivoBares = "bares.txt"

        fun mostrarBar(): List<Bar> {
           val gson = Gson()
            val lineas = barFile.readLines()
            val bares = mutableListOf<Bar>()
            for (lineas in lineas){
                val bar = gson.fromJson(lineas, Bar::class.java)
                bares.add(bar)
            }


           //comentado abajo
           // val archivoBar = File(rutaArchivoBares)
            //val gson = Gson()
          //  val lineas = archivoBar.readLines()
          //  val bares = mutableListOf<Bar>()

           // for (linea in lineas) {
           //     val bar = gson.fromJson(linea, Bar::class.java)
           //     bares.add(bar)
          //  }
            return bares
        }

        fun crearBar(bar: Bar) {
            val bares = mostrarBar().toMutableList()
            bares.add(bar)
            guardarBar(bares)
        }

        fun guardarBar(bares: List<Bar>) {
            val gson = Gson()
            val lineas = bares.map { gson.toJson(it) }
            barFile.writeText(lineas.joinToString("\n"))

            /*
            val archivoBar = File(rutaArchivoBares)
            val gson = Gson()
            val lineas = bares.map { gson.toJson(it) }
            archivoBar.writeText(lineas.joinToString("\n"))*/
        }

        fun borrarBar(id: Int) {
            val bares = mostrarBar().toMutableList()
            val barBorrar = bares.find { it.id == id }
            if (barBorrar != null) {
                bares.remove(barBorrar)
                guardarBar(bares)
            }
        }

        fun idDisponible(): Int {
            val bares = mostrarBar()
            return bares.map { it.id }.maxOrNull()?.plus(1) ?: 1
        }

        fun actualizarBar(bar: Bar) {
            val bares = mostrarBar().toMutableList()
            val index = bares.indexOfFirst { it.id == bar.id }
            if (index != -1) {
                bares[index] = bar
                guardarBar(bares)
            }
        }

    }

    override fun toString(): String {
        val formato = SimpleDateFormat("yyyy-MM-dd")
        val fecha = formato.format(creacion)
        return "IDBar: $id \nNombre: $nombre \nAforo: $aforo \nArea m2: $areaM2\n" +
                "Fecha de creacion: $fecha\nTiene parqueadero: $tieneParqueadero\nCocteles: ${cocteles.contentToString()}\n"

    }
}