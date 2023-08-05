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

class ingreso_bares : AppCompatActivity() {
    private val arreglo = BaseBares.arregloBares
    private var nextId = arreglo.size + 1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ingreso_bares)

        val botonRegresarCV = findViewById<ImageButton>(R.id.imbBackCV)
        botonRegresarCV
            .setOnClickListener {
                irActividad(Bares::class.java)
            }
        val botonAnadirC = findViewById<Button>(R.id.btnGuardarC)
        botonAnadirC
            .setOnClickListener{
                val nombre = findViewById<EditText>(R.id.etNombreC)
                val aforo = findViewById<EditText>(R.id.etAforo)
                val area = findViewById<EditText>(R.id.etArea)
                val inputFecha = findViewById<EditText>(R.id.etFechaIntegracion)
                val fechaString = inputFecha.text.toString()

                // Create a SimpleDateFormat to parse the date input
                val dateFormat = SimpleDateFormat("yyyy-MM-dd")
                val fechaIntegracion: Date = try {
                    dateFormat.parse(fechaString)
                } catch (e: Exception) {
                    Date()
                }

                val check= findViewById<RadioButton>(R.id.rbtAutorSi)
                val autor = if(check.isChecked){true}else{false}
                val coctels = ArrayList<Coctel>()


                val bar = Bar(nextId, nombre.text.toString(),aforo.text.toString().toInt(),area.text.toString().toDouble(),fechaIntegracion,autor,coctels)
                BaseBares.arregloBares.add(bar)
                nextId  ++

                irActividad(Bares::class.java)
            }
    }

    fun irActividad(
        clase: Class<*>
    ){
        val intent= Intent(this,clase)
        startActivity(intent)
    }

}