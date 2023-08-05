package com.example.recetario

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import java.text.SimpleDateFormat

class editar_cocteles : AppCompatActivity() {
    var arregloCoctels: ArrayList<Coctel>? = null
    var barSeleccionado=0
    var coctelSeleccionado=0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editar_cocteles)
        //recibir el parametro
        barSeleccionado = intent.getIntExtra("bar", 0)
        coctelSeleccionado = intent.getIntExtra("coctel", 0)
        val bar = BaseBares.arregloBares.find { it.id == barSeleccionado+1 }
        arregloCoctels = bar?.coctels ?: arrayListOf()
        var nextId = arregloCoctels?.size?.plus(1) ?: 1
        val coctel=arregloCoctels?.get(coctelSeleccionado)

        //nombre
        val nombreText = findViewById<EditText>(R.id.etENombreR)
        val nombreInicial = coctel?.nombre.toString()
        nombreText.setText(nombreInicial)

        //contenido
        val contenidoText = findViewById<EditText>(R.id.edEPorciones)
        val contenidoInicial = coctel?.contenido.toString()
        contenidoText.setText(contenidoInicial)


        //precio
        val precioText = findViewById<EditText>(R.id.etECalorias)
        val precioInicial= coctel?.precio.toString()
        precioText.setText(precioInicial)


        //fecha integracion
        val fechaText = findViewById<EditText>(R.id.etEFechaCreacion)
        val formato = SimpleDateFormat("yy-MM-dd")
        val fechaInicial = formato.format(coctel?.creacion)
        fechaText.setText(fechaInicial)

        //tienealcohol
        val tieneAlcoholText = findViewById<EditText>(R.id.etEFacil)
        val tieneAlcoholInicial = coctel?.esAlcoholica.toString()
        tieneAlcoholText.setText(tieneAlcoholInicial)

        //Ingredientes
        val ingreText = findViewById<EditText>(R.id.etEIngredientes)
        val ingreInicial = coctel?.ingredientes?.joinToString(", ")
        ingreText.setText(ingreInicial)


        //preparacion
        val prepaText = findViewById<EditText>(R.id.edEPreparacion)
        val prepaInicial = coctel?.preparacion.toString()
        prepaText.setText(prepaInicial)

        val botonRegresarERH = findViewById<ImageButton>(R.id.imbERegresarRH)
        botonRegresarERH
            .setOnClickListener {
                irActividad(Bares::class.java)
            }

        val botonEditarR = findViewById<Button>(R.id.btnEditarR)
        botonEditarR
            .setOnClickListener{
                val nombre = nombreText.text.toString()
                val contenido = contenidoText.text.toString()
                val precio = precioText.text.toString()
                val fecha = fechaText.text.toString()
                val tieneAlcohol = tieneAlcoholText.text.toString()
                val ingredientes = ingreText.text.toString()
                val preparacion=prepaText.text.toString()

                val formato = SimpleDateFormat("yyyy-MM-dd")
                val fechaF = formato.parse(fecha)

                val intentDevolverParametros = Intent()
                intentDevolverParametros.putExtra("nombreModificado",nombre)
                intentDevolverParametros.putExtra("contenidoModificado",contenido.toInt())
                intentDevolverParametros.putExtra("precioModificado",precio.toFloat())
                intentDevolverParametros.putExtra("dateModificado",fecha)
                intentDevolverParametros.putExtra("tieneAlcoholModificado",tieneAlcohol.toBoolean())
                intentDevolverParametros.putExtra("ingredientesModificado",ingredientes)
                intentDevolverParametros.putExtra("preparacionModificado",preparacion)

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