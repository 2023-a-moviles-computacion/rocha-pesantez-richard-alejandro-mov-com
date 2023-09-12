package com.example.myapplication

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

class EsqliteHelperBar(
    contexto: Context?,
) : SQLiteOpenHelper(
    contexto,
    "moviles",
    null,
    9
){
    override fun onCreate(db: SQLiteDatabase?) {
        Log.e("EsqliteHelperBar", "onCreate called")
        val scriptSQLCrearTablaBar =
            """
            CREATE TABLE BAR(
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            nombre VARCHAR(50),
            fecha VARCHAR(50),
            open BOOLEAN,
            stackvalue DOUBLE
        )
        """.trimIndent()
        db?.execSQL(scriptSQLCrearTablaBar)
    }
    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS BAR")
        onCreate(db)
    }
    fun crearFarmacia(
        nombre: String,
        fecha: String,
        open: Boolean,
        stackValue:Double,
    ): Boolean {
        val basedatosEscritura = writableDatabase
        val valoresAGuardar = ContentValues()
        valoresAGuardar.put("nombre", nombre)
        valoresAGuardar.put("fecha", fecha)
        valoresAGuardar.put("open",open)
        valoresAGuardar.put("stackValue",stackValue)
        val resultadoGuardar = basedatosEscritura
            .insert(
                "BAR",
                null,
                valoresAGuardar
            )
        basedatosEscritura.close()
        return if (resultadoGuardar.toInt() == -1) false else true
    }
    fun eliminarBarFormulario(id: Int): Boolean {
        val conexionEscritura = writableDatabase
        val parametrosCosnultaDelete = arrayOf(id.toString())
        val resultadoEliminacion = conexionEscritura
            .delete(
                "BAR",
                "id=?",
                parametrosCosnultaDelete
            )
        conexionEscritura.close()
        return if (resultadoEliminacion.toInt() == -1) false else true
    }
    fun actualizarBarFormulario(
        id: Int,
        nombre: String,
        fecha: String,
        open: Boolean,
        stackValue:Double
    ): Boolean {
        val conexionEscritura = writableDatabase
        val valoresAActualizar = ContentValues()
        valoresAActualizar.put("nombre", nombre)
        valoresAActualizar.put("fecha", fecha)
        valoresAActualizar.put("open",open)
        valoresAActualizar.put("stackValue",stackValue)
        val parametrosConsultaActualizar = arrayOf(id.toString())
        val resultadoActualizacion = conexionEscritura
            .update(
                "BAR",
                valoresAActualizar,
                "id=?",
                parametrosConsultaActualizar
            )
        conexionEscritura.close()
        return if (resultadoActualizacion.toInt()==-1)false else true
    }
    fun consultaBarPorId(
        id:Int
    ): BBar{
        val baseDatosLectura =readableDatabase
        val scriptConsultaLectura = """
            SELECT * FROM BAR WHERE ID = ?
        """.trimIndent()
        val parametrosConsultaLectura = arrayOf(id.toString())
        val resultadoConsultaLectura = baseDatosLectura.rawQuery(
            scriptConsultaLectura,
            parametrosConsultaLectura,
        )
        val existeUsuario = resultadoConsultaLectura.moveToFirst()
        val usuarioEncontrado = BBar(0,"","",false,0.0)
        val arreglo = arrayListOf<BBar>()
        if(existeUsuario){
            do{
                val existeUsuario = resultadoConsultaLectura.getInt(0)
                val nombre = resultadoConsultaLectura.getString(1)
                val fecha = resultadoConsultaLectura.getString(2)
                val open = resultadoConsultaLectura.getInt(3) != 0
                val stackValue = resultadoConsultaLectura.getDouble(4)

                if(id != null){
                    usuarioEncontrado.id = id
                    usuarioEncontrado.nombre = nombre
                    usuarioEncontrado.fecha = fecha
                    usuarioEncontrado.open = open
                    usuarioEncontrado.stackValue = stackValue
                }
            }while(resultadoConsultaLectura.moveToNext())
        }
        resultadoConsultaLectura.close()
        baseDatosLectura.close()
        return usuarioEncontrado

    }
    fun obtenerTodasLosBares(): ArrayList<BBar> {
        val listaFarmacias = ArrayList<BBar>()
        val baseDatosLectura = readableDatabase
        val scriptConsultaLectura = "SELECT * FROM BAR"
        val resultadoConsultaLectura = baseDatosLectura.rawQuery(scriptConsultaLectura, null)

        if (resultadoConsultaLectura.moveToFirst()) {
            do {
                val id = resultadoConsultaLectura.getInt(0)
                val nombre = resultadoConsultaLectura.getString(1)
                val fecha = resultadoConsultaLectura.getString(2)
                val open = resultadoConsultaLectura.getInt(3) != 0
                val stackValue = resultadoConsultaLectura.getDouble(4)

                val bar = BBar(id, nombre, fecha, open, stackValue)
                listaFarmacias.add(bar)
            } while (resultadoConsultaLectura.moveToNext())
        }

        resultadoConsultaLectura.close()
        baseDatosLectura.close()
        return listaFarmacias
    }

}