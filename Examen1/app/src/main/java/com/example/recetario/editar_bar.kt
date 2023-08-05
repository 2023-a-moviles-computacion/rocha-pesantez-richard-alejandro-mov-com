package com.example.recetario

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import java.text.SimpleDateFormat

class editar_bar : AppCompatActivity() {

    val arregloBares= BaseBares.arregloBares

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editar_bar)

        //recibir parametros
        val id = intent.getIntExtra("id",0)

        val bar=arregloBares.get(id)
        //nombre
        val nombreText = findViewById<EditText>(R.id.etENombre)
        val nombreInicial = bar.nombre.toString()
        nombreText.setText(nombreInicial)

        //aforo
        val aforoText = findViewById<EditText>(R.id.edEEdad)
        val aforoInicial = bar.aforo.toString()
        aforoText.setText(aforoInicial)


        //area
        val areaText = findViewById<EditText>(R.id.etEPuntuacion)
        val areaInicial = bar.area.toString()
        areaText.setText(areaInicial)


        //fecha integracion
        val fechaText = findViewById<EditText>(R.id.etEFecha)
        val formato = SimpleDateFormat("yy-MM-dd")
        val fechaInicial = formato.format(bar.fechaIntegracion)
        fechaText.setText(fechaInicial)

        //tieneParkeadero
        val tieneParkText = findViewById<EditText>(R.id.etEAutor)
        val tieneParkInicial = bar.tieneParqueadero.toString()
        tieneParkText.setText(tieneParkInicial)


        val botonRegresarECH = findViewById<ImageButton>(R.id.imbERegresar)
        botonRegresarECH
            .setOnClickListener {
                irActividad(Bares::class.java)
            }
        val botonEditarC = findViewById<Button>(R.id.btnEditar)
        botonEditarC
            .setOnClickListener {
                val nombre = nombreText.text.toString()
                val aforo = aforoText.text.toString()
                val area = areaText.text.toString()
                val fecha = fechaText.text.toString()
                val tieneAlcohol = tieneParkText.text.toString()

                val nuevococinero = BaseBares.arregloBares[id]
                nuevococinero.nombre=nombre
                nuevococinero.aforo = aforo.toInt()
                nuevococinero.area = area.toDouble()
                val formato = SimpleDateFormat("yyyy-MM-dd")
                val fechaF = formato.parse(fecha)
                nuevococinero.fechaIntegracion =fechaF
                nuevococinero.tieneParqueadero = tieneAlcohol.toBoolean()

                val intentDevolverParametros = Intent()
                intentDevolverParametros.putExtra("nombreModificado",nombre)
                intentDevolverParametros.putExtra("aforoModificado",aforo.toInt())
                intentDevolverParametros.putExtra("areaModificado",area.toFloat())
                intentDevolverParametros.putExtra("dateModificado",fecha)
                intentDevolverParametros.putExtra("tieneAlcoholModificado",tieneAlcohol.toBoolean())

                setResult(
                    RESULT_OK,
                    intentDevolverParametros
                )
                finish()

            }

    }


    fun irActividad(
        clase: Class<*>
    ){
        val intent= Intent(this,clase)
        startActivity(intent)
    }
}