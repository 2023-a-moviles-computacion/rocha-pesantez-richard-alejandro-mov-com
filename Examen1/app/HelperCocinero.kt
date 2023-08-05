import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class HelperCocinero(
    contexto : Context?, //debemos pasar el contexto
) : SQLiteOpenHelper(
    contexto,
    "recetario", //nombre de la BDD
    null,
    1
){
    override fun onCreate(db: SQLiteDatabase?) {
        val scriptSQLCrearTablaEntrenador =
            """
                    CREATE TABLE COCINEROS(
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    nobre VARCHAR(50),
                    descripcion VARCHAR(50)
                    )
            """.trimIndent()
        db?.execSQL(scriptSQLCrearTablaEntrenador)
    }
    override fun onUpgrade(db:SQLiteDatabase?, oldVersion: Int, newVersion Int){}


    fun crearEntrenador(
        nombre:String,
        descipcion: Stirng
    ): Boolean{
        val basedatosEscritura = witableDatabase
        val valoresAGuardar = ContentValues()
        valoresAGuardar.put("nombre",nombre)
        valoresAGuardar.put("descripcion",descripcion)
        val resultadoGuardar = basedatosEscritura
            .insert(
                "ENTRENADOR",//nombre tabla
                null,
                valoresAGuardar//valores
            )
        basedatosEscritura.close()
        return if(resultadoGuardar.toInt() == -1) false else true
    }


    //el arreglo que se tiene aqui solo tiene el id nada mas
    //para estos dos se usa la base de datos de escritura
    fun eliminarEntrenadorFormulario(id:Int):Boolean{
        var conexionEscritura = writableDatabase
        //where ID=?
        val parametrosConsultaDelete = arrayOf(id.toString())
        val resultadoEliminacion = conexionEscritura
            .delete(
                "ENTRENADOR",//nOMBRE TABLA
                "id=?",
                parametrosConsultaDelete
            )
        conexionEscritura.close()
        return if(resultadoEliminacion.toInt() == -1) false else true
    }


    fun actualizarEntrenadorFormulario(
        nombre:String,
        descripcion:String,
        id:Int,
    ):Boolean{
        val conexionEscritura = writableDatabase
        val valoresAActualizar = ContentValues()
        valoresAActualizar.put("nombre", nombre)
        valoresAActualizar.put("descripcion", descripcion)

        //where ID=?
        val parametrosConsultaActualizar = arrayOf(id.toString())
        val resultadoActualizacion = conexionEscritura
            .update(
                "ENTRENADOR", //NOMBRE TABLA
                valoresAActualizar, //valores
                "id=?", //consulta where
                parametrosConsultaActualizar
            )
        conexionEscritura.close()
        return if (resultadoActualizacion.toInt() == -1) false else true
    }

    fun consultarEntrenadorPorID(id: Int): BEntrenador{
        val baseDarosLectura = readableDatabase
        val scriptConsultaLectura = """
            SELECT * FROM ENTRENADOR WHERE ID = ?
           """.trimIndent()
        val parametrosConsultaLectura = arrayOf(id.toString())
        val resultadoConsultaLectura = baseDatosLectura.rawQuery(
            scriptConsultaLectura, //CONSULTA
            parametrosConsultaLectura, //parametros
        )
        //logica de busqueda
        val existeUsuario= resultadoConsultaLectura.moveToFirst()
        val usuarioEncontrado = BEntrenador(0,"","")
        val arreglo = arrayListOf<BEntrenador>()
        if(existeUsuario){
            do{
                val id=resultadoConsultaLectura.getInt(0) //indice 0
                val nombre =  resultadoConsultaLectura.getString(1)
                val descripcion = resultadoConsultaLectura.getstring(2)
                if(id != null){
                    //llenar el arreglo con un nuevo BEntrenador
                    usuarioEncontrado.id =id
                    usuarioEncontrado.nombre = nombre
                    usuarioEncontrado.descripcion=descripcion
                }

            } while (resultadoConsultaLectura.moveToNext())
        }
        resultadoConsultaLectura.close()
        baseDatosLectura.close()
        return usuarioEncontrado
    }

}