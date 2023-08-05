package com.example.recetario

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.RadioButton
import java.text.SimpleDateFormat
import java.util.Date

class IngresoCocteles : AppCompatActivity() {

    var arregloCoctels: ArrayList<Coctel>? = null
    var barSeleccionado=0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ingreso_cocteles)
        //recibir el parametro
        barSeleccionado = intent.getIntExtra("id", 0)
        val bar = BaseBares.arregloBares.find { it.id == barSeleccionado+1 }
        arregloCoctels = bar?.coctels ?: arrayListOf()
        var nextId = arregloCoctels?.size?.plus(1) ?: 1


        val botonRegresarVr = findViewById<ImageButton>(R.id.imbRegresarV)
        botonRegresarVr.setOnClickListener {
            irActividad(Cocteles::class.java)
        }

        val botonAnadirR = findViewById<Button>(R.id.btnGuardarR)
        botonAnadirR
            .setOnClickListener{
                val nombre = findViewById<EditText>(R.id.etNombre)
                val contenido = findViewById<EditText>(R.id.etContenido)
                val precio = findViewById<EditText>(R.id.etCalorias)
                val inputFecha = findViewById<EditText>(R.id.etCreacion)
                val fechaString = inputFecha.text.toString()

                // Create a SimpleDateFormat to parse the date input
                val dateFormat = SimpleDateFormat("yyyy-MM-dd")
                val fechaIntegracion: Date = try {
                    dateFormat.parse(fechaString)
                } catch (e: Exception) {
                    Date()
                }

                val check= findViewById<RadioButton>(R.id.rbtSi)
                val facil = if(check.isChecked){true}else{false}
                val input = findViewById<EditText>(R.id.etIngredientes)
                val ingredientes = input.text.toString().split(", ").toTypedArray()

                val preparacion = findViewById<EditText>(R.id.etPreparacion)

                val coctel = Coctel(nextId, nombre.text.toString(),contenido.text.toString().toInt(),precio.text.toString().toDouble(),fechaIntegracion,facil,ingredientes,preparacion.text.toString())
                BaseBares.arregloBares[barSeleccionado].coctels?.add(coctel)
                nextId  ++

                irActividad(Cocteles::class.java)
            }
    }

    fun irActividad(
        clase: Class<*>
    ){
        val intent= Intent(this,clase)
        startActivity(intent)
    }
}