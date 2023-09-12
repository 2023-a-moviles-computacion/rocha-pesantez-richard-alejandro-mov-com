package com.example.myapplication

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

class ESqliteHelperCoctel (
    contexto: Context?,
) : SQLiteOpenHelper(
    contexto,
    "moviles",
    null,
    9
){
    override fun onCreate(db: SQLiteDatabase?) {
        Log.e("ESqliteHelperCoctel", "onCreate called DE COCTEL")
        val scriptSQLCrearTablaCoctel =
            """
            CREATE TABLE COCTEL(
        id INTEGER PRIMARY KEY AUTOINCREMENT,
        nombre VARCHAR(50),
        fecha VARCHAR(50),
        caducado BOOLEAN,
        precio DOUBLE,
        barId INTEGER
    )
        """.trimIndent()
        db?.execSQL(scriptSQLCrearTablaCoctel)
    }
    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS COCTEL")

        onCreate(db)
    }

    fun crearMedicina(
        nombre: String,
        fecha: String,
        caducado: Boolean,
        precio:Double,
        barId: Int
    ): Boolean {
        val basedatosEscritura = writableDatabase
        val valoresAGuardar = ContentValues()
        valoresAGuardar.put("nombre", nombre)
        valoresAGuardar.put("fecha", fecha)
        valoresAGuardar.put("caducado",caducado)
        valoresAGuardar.put("precio",precio)
        valoresAGuardar.put("barId",barId)
        val resultadoGuardar = basedatosEscritura
            .insert(
                "COCTEL",
                null,
                valoresAGuardar
            )
        basedatosEscritura.close()
        return if (resultadoGuardar.toInt() == -1) false else true
    }
    fun eliminarMedicinaFormulario(id: Int): Boolean {
        val conexionEscritura = writableDatabase
        val parametrosCosnultaDelete = arrayOf(id.toString())
        val resultadoEliminacion = conexionEscritura
            .delete(
                "COCTEL",
                "id=?",
                parametrosCosnultaDelete
            )
        conexionEscritura.close()
        return if (resultadoEliminacion.toInt() == -1) false else true
    }
    fun actualizarMedicinaFormulario(
        id: Int,
        nombre: String,
        fecha: String,
        caducado: Boolean,
        precio:Double,
        barId: Int
    ): Boolean {
        val conexionEscritura = writableDatabase
        val valoresAActualizar = ContentValues()
        valoresAActualizar.put("nombre", nombre)
        valoresAActualizar.put("fecha", fecha)
        valoresAActualizar.put("caducado",caducado)
        valoresAActualizar.put("precio",precio)
        val parametrosConsultaActualizar = arrayOf(id.toString())
        val resultadoActualizacion = conexionEscritura
            .update(
                "COCTEL",
                valoresAActualizar,
                "id=?",
                parametrosConsultaActualizar
            )
        conexionEscritura.close()
        return if (resultadoActualizacion.toInt()==-1)false else true
    }
    fun consultaMedicinaPorId(
        id:Int
    ): BCoctel{
        val baseDatosLectura =readableDatabase
        val scriptConsultaLectura = """
            SELECT * FROM COCTEL WHERE ID = ?
        """.trimIndent()
        val parametrosConsultaLectura = arrayOf(id.toString())
        val resultadoConsultaLectura = baseDatosLectura.rawQuery(
            scriptConsultaLectura,
            parametrosConsultaLectura,
        )
        val existeUsuario = resultadoConsultaLectura.moveToFirst()
        val usuarioEncontrado = BCoctel(0,"","",false,0.0,0)
        val arreglo = arrayListOf<BCoctel>()
        if(existeUsuario){
            do{
                val existeUsuario = resultadoConsultaLectura.getInt(0)
                val nombre = resultadoConsultaLectura.getString(1)
                val fecha = resultadoConsultaLectura.getString(2)
                val caducado = resultadoConsultaLectura.getInt(3) != 0
                val precio = resultadoConsultaLectura.getDouble(4)
                val idBar = resultadoConsultaLectura.getInt(5)

                if(id != null){
                    usuarioEncontrado.id = id
                    usuarioEncontrado.nombre = nombre
                    usuarioEncontrado.fecha = fecha
                    usuarioEncontrado.caducado = caducado
                    usuarioEncontrado.precio = precio
                    usuarioEncontrado.barId = idBar
                }
            }while(resultadoConsultaLectura.moveToNext())
        }
        resultadoConsultaLectura.close()
        baseDatosLectura.close()
        return usuarioEncontrado

    }
    fun obtenerTodasLasMedicinas(): ArrayList<BCoctel> {
        val listaCocteles = ArrayList<BCoctel>()
        val baseDatosLectura = readableDatabase
        val scriptConsultaLectura = "SELECT * FROM COCTEL"
        val resultadoConsultaLectura = baseDatosLectura.rawQuery(scriptConsultaLectura, null)

        if (resultadoConsultaLectura.moveToFirst()) {
            do {
                val id = resultadoConsultaLectura.getInt(0)
                val nombre = resultadoConsultaLectura.getString(1)
                val fecha = resultadoConsultaLectura.getString(2)
                val caducado = resultadoConsultaLectura.getInt(3) != 0
                val precio = resultadoConsultaLectura.getDouble(4)
                val barId = resultadoConsultaLectura.getInt(5)
                val coctel = BCoctel(id, nombre, fecha, caducado, precio,barId)
                listaCocteles.add(coctel)
            } while (resultadoConsultaLectura.moveToNext())
        }

        resultadoConsultaLectura.close()
        baseDatosLectura.close()
        return listaCocteles
    }
    fun getMedicinaByFarmaciaId(barId: Int): ArrayList<BCoctel> {
        val listaCoctel = ArrayList<BCoctel>()
        val baseDatosLectura = readableDatabase

        val scriptConsultaLectura = "SELECT * FROM COCTEL WHERE barId = ?"
        val parametrosConsultaLectura = arrayOf(barId.toString())
        val resultadoConsultaLectura = baseDatosLectura.rawQuery(
            scriptConsultaLectura,
            parametrosConsultaLectura
        )

        if (resultadoConsultaLectura.moveToFirst()) {
            do {
                val id = resultadoConsultaLectura.getInt(0)
                val nombre = resultadoConsultaLectura.getString(1)
                val fecha = resultadoConsultaLectura.getString(2)
                val caducado = resultadoConsultaLectura.getInt(3) != 0
                val precio = resultadoConsultaLectura.getDouble(4)
                val barId = resultadoConsultaLectura.getInt(5)
                val medicina = BCoctel(id, nombre, fecha, caducado, precio, barId)
                listaCoctel.add(medicina)
            } while (resultadoConsultaLectura.moveToNext())
        }

        resultadoConsultaLectura.close()
        baseDatosLectura.close()
        return listaCoctel
    }

}