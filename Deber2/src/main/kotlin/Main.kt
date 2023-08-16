import org.sqlite.SQLiteDataSource

data class Bar(
    val id: Int,
    var nombreBar: String,
    var aforo: Int,
    var area: Double,
    var tienePark: Boolean,
    var fechaDeFundacion: String, //yyyy-mm-dd
)

class Coctel(
    val id: Int,
    var nombreCoctel: String?,
    var contenido: Int?,
    var precio: Double?,
    var esAlcoholica: Boolean,
    var fechaRegistro: String?,
    var barId: Int
)

class Bar_Control {
    private val dataSource: SQLiteDataSource

    init {
        dataSource = SQLiteDataSource()
        dataSource.url = "jdbc:sqlite:bar-coctel1.db"
        createBarTable()
    }

    private fun createBarTable() {
        dataSource.connection.use { connection ->
            val statement = connection.createStatement()
            val createTableSQL = """
                CREATE TABLE IF NOT EXISTS BAR (
                    id INTEGER PRIMARY KEY,
                    nombreBar TEXT,
                    aforo INTEGER,
                    area REAL,
                    tienePark INTEGER,
                    fechaDeFundacion TEXT
                );
            """
            statement.executeUpdate(createTableSQL)
        }
    }

    fun existsBar(id: Int): Boolean {
        dataSource.connection.use { connection ->
            val selectSQL = "SELECT COUNT(*) FROM BAR WHERE id = ?"
            connection.prepareStatement(selectSQL).use { preparedStatement ->
                preparedStatement.setInt(1, id)
                val resultSet = preparedStatement.executeQuery()
                return resultSet.getInt(1) > 0
            }
        }
    }

    fun create(bar: Bar) {
        if (existsBar(bar.id)) {
            println("El identificador del bar ya está en uso.")
            return
        }
        dataSource.connection.use { connection ->
            val insertSQL = """
                INSERT INTO BAR(id, nombreBar, aforo, area, tienePark, fechaDeFundacion)
                VALUES (?, ?, ?, ?, ?, ?);
            """
            connection.prepareStatement(insertSQL).use { preparedStatement ->
                preparedStatement.setInt(1, bar.id)
                preparedStatement.setString(2, bar.nombreBar)
                preparedStatement.setInt(3, bar.aforo)
                preparedStatement.setDouble(4, bar.area)
                preparedStatement.setBoolean(5, bar.tienePark)
                preparedStatement.setString(6, bar.fechaDeFundacion)
                preparedStatement.executeUpdate()
            }
        }
        println("Bar creado correctamente.")
    }

    fun readAll(): List<Bar> {
        val bars = mutableListOf<Bar>()
        dataSource.connection.use { connection ->
            val selectSQL = "SELECT * FROM BAR"
            connection.prepareStatement(selectSQL).use { preparedStatement ->
                val resultSet = preparedStatement.executeQuery()
                while (resultSet.next()) {
                    bars.add(
                        Bar(
                            resultSet.getInt("id"),
                            resultSet.getString("nombreBar"),
                            resultSet.getInt("aforo"),
                            resultSet.getDouble("area"),
                            resultSet.getBoolean("tienePark"),
                            resultSet.getString("fechaDeFundacion")
                        )
                    )
                }
            }
        }
        return bars
    }

    fun update(bar: Bar) {
        dataSource.connection.use { connection ->
            val updateSQL = """
                UPDATE BAR
                SET nombreBar = ?, aforo = ?, area = ?, tienePark = ?, fechaDeFundacion = ?
                WHERE id = ?;
            """
            connection.prepareStatement(updateSQL).use { preparedStatement ->
                preparedStatement.setString(1, bar.nombreBar)
                preparedStatement.setInt(2, bar.aforo)
                preparedStatement.setDouble(3, bar.area)
                preparedStatement.setBoolean(4, bar.tienePark)
                preparedStatement.setString(5, bar.fechaDeFundacion)
                preparedStatement.setInt(6, bar.id)
                preparedStatement.executeUpdate()
            }
        }
        println("Bar actualizado correctamente.")
    }

    fun delete(id: Int) {
        dataSource.connection.use { connection ->
            val deleteSQL = "DELETE FROM BAR WHERE id = ?"
            connection.prepareStatement(deleteSQL).use { preparedStatement ->
                preparedStatement.setInt(1, id)
                preparedStatement.executeUpdate()
            }
        }
        println("Bar eliminado.")
    }
}

class Cocteles_Control {
    private val dataSource: SQLiteDataSource

    init {
        dataSource = SQLiteDataSource()
        dataSource.url = "jdbc:sqlite:bar-coctel2.db"
        createCoctelTable()
    }

    private fun createCoctelTable() {
        dataSource.connection.use { connection ->
            val statement = connection.createStatement()
            val createTableSQL = """
                CREATE TABLE IF NOT EXISTS COCTELES (
                    id INTEGER PRIMARY KEY,
                    nombreCoctel TEXT,
                    contenido INTEGER,
                    precio REAL,
                    esAlcoholica INTEGER,
                    fechaRegistro TEXT,
                    barId INTEGER,
                    FOREIGN KEY (barId) REFERENCES BAR(id)
                );
            """
            statement.executeUpdate(createTableSQL)
        }
    }

    fun existsCoctel(id: Int): Boolean {
        dataSource.connection.use { connection ->
            val selectSQL = "SELECT COUNT(*) FROM COCTELES WHERE id = ?"
            connection.prepareStatement(selectSQL).use { preparedStatement ->
                preparedStatement.setInt(1, id)
                val resultSet = preparedStatement.executeQuery()
                return resultSet.getInt(1) > 0
            }
        }
    }

    fun create(coctel: Coctel, barId: Int) {
        if (existsCoctel(coctel.id)) {
            println("El identificador del coctel ya está en uso.")
            return
        }
        dataSource.connection.use { connection ->
            val insertSQL = """
                INSERT INTO COCTELES (id, nombreCoctel, contenido, precio, esAlcoholica, fechaRegistro, barId)
                VALUES (?, ?, ?, ?, ?, ?, ?);
            """
            connection.prepareStatement(insertSQL).use { preparedStatement ->
                preparedStatement.setInt(1, coctel.id)
                preparedStatement.setString(2, coctel.nombreCoctel)
                preparedStatement.setInt(3, coctel.contenido!!)
                preparedStatement.setDouble(4, coctel.precio!!)
                preparedStatement.setBoolean(5, coctel.esAlcoholica)
                preparedStatement.setString(6, coctel.fechaRegistro)
                preparedStatement.setInt(7, barId)
                preparedStatement.executeUpdate()
            }
        }
        println("Coctel creado correctamente.")
    }

    fun readAll(): List<Coctel> {
        val cocteles = mutableListOf<Coctel>()
        dataSource.connection.use { connection ->
            val selectSQL = "SELECT * FROM COCTELES"
            connection.prepareStatement(selectSQL).use { preparedStatement ->
                val resultSet = preparedStatement.executeQuery()
                while (resultSet.next()) {
                    cocteles.add(
                        Coctel(
                            resultSet.getInt("id"),
                            resultSet.getString("nombreCoctel"),
                            resultSet.getInt("contenido"),
                            resultSet.getDouble("precio"),
                            resultSet.getBoolean("esAlcoholica"),
                            resultSet.getString("fechaRegistro"),
                            resultSet.getInt("barId")
                        )
                    )
                }
            }
        }
        return cocteles
    }

    fun update(coctel: Coctel) {
        dataSource.connection.use { connection ->
            val updateSQL = """
                UPDATE COCTELES
                SET nombreCoctel = ?, contenido = ?, precio = ?, esAlcoholica = ?, fechaRegistro = ?, barId = ?
                WHERE id = ?;
            """
            connection.prepareStatement(updateSQL).use { preparedStatement ->
                preparedStatement.setString(1, coctel.nombreCoctel)
                preparedStatement.setInt(2, coctel.contenido!!)
                preparedStatement.setDouble(3, coctel.precio!!)
                preparedStatement.setBoolean(4, coctel.esAlcoholica)
                preparedStatement.setString(5, coctel.fechaRegistro)
                preparedStatement.setInt(6, coctel.id)
                preparedStatement.executeUpdate()
            }
        }
        println("Coctel actualizado correctamente.")
    }

    fun delete(id: Int) {
        dataSource.connection.use { connection ->
            val deleteSQL = "DELETE FROM COCTELES WHERE id = ?"
            connection.prepareStatement(deleteSQL).use { preparedStatement ->
                preparedStatement.setInt(1, id)
                preparedStatement.executeUpdate()
            }
        }
        println("Coctel eliminado.")
    }
}
/*
    fun requestInt(message: String): Int {
        println(message)
        return readLine()?.toIntOrNull() ?: run {
            println("Por favor, introduce un valor válido.")
            return requestInt(message)
        }
    }

    fun requestDouble(message: String): Double {
        println(message)
        return readLine()?.toDoubleOrNull() ?: run {
            println("Por favor, introduce un valor válido.")
            return requestDouble(message)
        }
    }

    fun requestTeamId(controlEquipos: Bar_Control): Int {
        val teamId = requestInt("Introduce el ID del equipo:")
        if (!controlEquipos.existsBar(teamId)) {
            println("No existe un equipo con el ID proporcionado.")
            return requestTeamId(controlEquipos)
        }
        return teamId
    }
*/
fun main() {
    val controlBar = Bar_Control()
    val controlCocteles = Cocteles_Control()

    while (true) {
        println("--- Seleccione una de las siguientes opciones ---")
        println("1. Bares")
        println("2. Cocteles")
        println("3. Salir")

        when (readLine()?.toIntOrNull()) {
            1 -> {
                while (true) {
                    println("--- Operaciones CRUD de BARES ---")
                    println("1. Crear bar")
                    println("2. Mostrar bares")
                    println("3. Actualizar bar")
                    println("4. Eliminar bar")
                    println("5. Regresar al menú principal")

                    when (readLine()?.toIntOrNull()) {
                        1 -> {
                            println("Introduce el ID del bar:")
                            val id = readLine()?.toIntOrNull() ?: continue
                            println("Introduce el nombre del bar:")
                            val nombre = readLine() ?: continue
                            println("Introduce el aforo del bar:")
                            val aforo = readLine() ?: continue
                            println("Introduce el area m2 del bar:")
                            val aream2 = readLine() ?: continue
                            println("Tiene parqueadero el bar (1-SI | 0-NO):")
                            val tienePark = readLine() ?: continue
                            println("Introduce la fecha de fundación del bar:")
                            val fechaFundacion = readLine() ?: continue

                            val bar = Bar(id, nombre, aforo.toInt(), aream2.toDouble(), tienePark.toBoolean(), fechaFundacion)
                            controlBar.create(bar)
                        }
                        2 -> {
                            val bares = controlBar.readAll()
                            bares.forEach { bar ->
                                println("""
                                    ID: ${bar.id}
                                    Nombre del Bar: ${bar.nombreBar}
                                    Aforo: ${bar.aforo}
                                    Area: ${bar.area}
                                    Tiene Parqueadero: ${bar.tienePark}
                                    Fecha de Fundación: ${bar.fechaDeFundacion}
                                """.trimIndent())
                                println("--------------------------------------")
                            }
                        }
                        3 -> {
                            println("Introduce el ID del bar a actualizar:")
                            val id = readLine()?.toIntOrNull() ?: continue
                            if (!controlBar.existsBar(id)) {
                                println("No existe el bar con el ID proporcionado.")
                                continue
                            }
                            println("Introduce el nuevo nombre del bar:")
                            val nombre = readLine() ?: continue
                            println("Introduce el nuevo aforo del bar:")
                            val aforo = readLine() ?: continue
                            println("Introduce la nueva area m2 del bar:")
                            val aream2 = readLine() ?: continue
                            println("Tiene parqueadero el bar (1-SI | 0-NO):")
                            val tienePark = readLine() ?: continue
                            println("Introduce la nueva fecha de fundación del bar:")
                            val fechaFundacion = readLine() ?: continue

                            val barActualizado = Bar(id, nombre, aforo.toInt(), aream2.toDouble(), tienePark.toBoolean(), fechaFundacion)
                            controlBar.update(barActualizado)
                        }
                        4 -> {
                            println("Introduce el ID del bar a eliminar:")
                            val id = readLine()?.toIntOrNull() ?: continue

                            if (!controlBar.existsBar(id)) {
                                println("No existe un bar con el ID proporcionado.")
                                continue
                            }

                            controlBar.delete(id)
                        }
                        5 -> break
                        else -> println("Opción no válida. Por favor, intente de nuevo.")
                    }
                }
            }
            2 -> {
                while (true) {
                    println("--- Operaciones CRUD de COCTELES ---")
                    println("1. Crear coctel")
                    println("2. Mostrar cocteles")
                    println("3. Actualizar coctel")
                    println("4. Eliminar coctel")
                    println("5. Regresar al menú principal")

                    when (readLine()?.toIntOrNull()) {
                        1 -> {
                            println("Introduce el ID del BAR al que pertenece el coctel: ")
                            val idBAR = readLine()?.toIntOrNull() ?: continue
                            if (!controlBar.existsBar(idBAR)) {
                                println("No existe un bar con el ID proporcionado.")
                                continue
                            }
                            println("Introduce el ID del coctel:")
                            val id = readLine() ?: continue
                            println("Introduce el nombre del coctel:")
                            val nombreCoctel = readLine() ?: continue
                            println("Introduce el contenido del coctel:")
                            val contenido = readLine() ?: continue
                            println("Introduce el precio del coctel:")
                            val precio = readLine() ?: continue
                            println("¿Tiene Alcohol el coctel? (1-SI/0-NO):")
                            val esAlcoholica = readLine() ?: continue
                            println("Introduce la fecha de registro:")
                            val fechaRegistro = readLine() ?: continue


                            val coctel = Coctel(id.toInt(), nombreCoctel, contenido.toInt(), precio.toDouble(), esAlcoholica.toBoolean(), fechaRegistro, idBAR)
                            controlCocteles.create(coctel, idBAR)
                        }
                        2 -> {
                            val cocteles = controlCocteles.readAll()
                            cocteles.forEach { coctel ->
                                println("""
                                    ID: ${coctel.id}
                                    Nombre: ${coctel.nombreCoctel}
                                    Contenido: ${coctel.contenido}
                                    Precio: ${coctel.precio}
                                    Contenido: ${coctel.contenido}
                                    EsAlcoholica: ${coctel.esAlcoholica}
                                    Fecha de Registro: ${coctel.fechaRegistro}
                                    ID Bar: ${coctel.barId}
                                """.trimIndent())
                                println("--------------------------------------")
                            }
                        }
                        3 -> {
                            println("Introduce el ID del BAR al que pertenece el coctel: ")
                            val idBAR = readLine()?.toIntOrNull() ?: continue
                            if (!controlBar.existsBar(idBAR)) {
                                println("No existe un BAR con el ID proporcionado.")
                                continue
                            }
                            println("Introduce el ID del coctel a actualizar:")
                            val id = readLine()?.toIntOrNull() ?: continue

                            if (!controlCocteles.existsCoctel(id)) {
                                println("No existe un coctel con el ID proporcionado.")
                                continue
                            }

                            println("Introduce el NUEVO nombre del coctel:")
                            val nombreCoctel = readLine() ?: continue
                            println("Introduce el nuevo contenido del coctel:")
                            val contenido = readLine()?.toIntOrNull() ?: continue
                            println("Introduce el nuevo precio del coctel:")
                            val precio = readLine()?.toDoubleOrNull() ?: continue
                            println("¿Tiene Alcohol el coctel? (1-SI/0-NO):")
                            val esAlcoholica = readLine()?.toBooleanStrictOrNull() ?: continue
                            println("Introduce la nueva fecha de registro:")
                            val fechaRegistro = readLine() ?: continue

                            val coctelActualizado = Coctel(id, nombreCoctel, contenido, precio, esAlcoholica, fechaRegistro, idBAR)
                            controlCocteles.update(coctelActualizado)
                        }
                        4 -> {
                            println("Introduce el ID del coctel a eliminar:")
                            val id = readLine()?.toIntOrNull() ?: continue

                            if (!controlCocteles.existsCoctel(id)) {
                                println("No existe un coctel con el ID proporcionado.")
                                continue
                            }

                            controlCocteles.delete(id)
                        }
                        5 -> break
                        else -> println("Opción no válida. Por favor, intente de nuevo.")
                    }
                }
            }
            3 -> {
                println("Saliendo del programa.")
                return
            }
            else -> println("Opción no válida. Por favor, intente de nuevo.")
        }
        println()
    }
}

