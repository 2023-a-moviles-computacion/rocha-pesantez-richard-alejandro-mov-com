package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.WindowCompat

class EditarCoctel : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editar_coctel)

    val nombreCoctel = intent.getStringExtra("nombre_coctel")
    val idCoctel = intent.getIntExtra("id_coctel", -1)
        val idBar = intent.getIntExtra("id_bar", -1)
        val nombreBar = intent.getStringExtra("nombre_bar")
    val selectedItemTextView = findViewById<TextView>(R.id.selectedItem_em)
    selectedItemTextView.text = nombreCoctel

        val botonActualizarBDD = findViewById<Button>(R.id.btn_actualizar_bdd_em)
        botonActualizarBDD.setOnClickListener {
            val nombre = findViewById<EditText>(R.id.input_nombre_em)
            val fecha = findViewById<EditText>(R.id.input_fecha_em)
            val caducado = findViewById<EditText>(R.id.input_caducado_em)
            val precio = findViewById<EditText>(R.id.input_precio_em)
            EBaseDeDatos.tablaCoctel!!.actualizarMedicinaFormulario(
                idCoctel,
                nombre.text.toString(),
                fecha.text.toString(),
                caducado.text.toString().toBoolean(),
                precio.text.toString().toDouble(),
                idBar
            )
            Toast.makeText(this, "Se ha actualizado con éxito", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, ListViewCoctel::class.java)
            intent.putExtra("nombre_bar", nombreBar)
            startActivity(intent)
            finish()
        }

    }
}