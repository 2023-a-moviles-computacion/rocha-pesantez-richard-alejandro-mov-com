package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.WindowCompat
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class EditarCoctelFirestore : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editar_coctel_firestore)
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

            val updatedData = hashMapOf(
                "nombre" to nombre.text.toString(),
                "fecha" to fecha.text.toString(),
                "caducado" to caducado.text.toString(),
                "precio" to precio.text.toString(),
                "bar" to idBar.toString()
            )
            actualizarRegistro(idCoctel, updatedData)
            val intent = Intent(this, ListViewCoctelFirestore::class.java)
            intent.putExtra("nombre_bar", nombreBar)
            startActivity(intent)
            finish()
        }
    }

    private fun actualizarRegistro(idMedicina: Int, updatedData: HashMap<String, String>) {
        val db = Firebase.firestore
        val refMedicine = db.collection("medicines")
        refMedicine
            .document(idMedicina.toString())
            .update(updatedData as Map<String, Any>)
            .addOnSuccessListener {
                Toast.makeText(this, "Coctel has been updated", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {  }

    }
}