package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ContextMenu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.WindowCompat
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.Date

class ListViewCoctelFirestore : AppCompatActivity() {
    var idItemSeleccionado = 0
    var barId =""
    val arreglo: ArrayList<BCoctel> = arrayListOf()
    lateinit var adaptador: ArrayAdapter<BCoctel>
    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_view_coctel_firestore)

        val nombreFarmacia = intent.getStringExtra("nombre_bar")
        val nombrefarm = intent.getStringExtra("nombre_ba")
        barId= intent.getStringExtra("id_bar").toString()


        val selectedItemTextView = findViewById<TextView>(R.id.selectedItem_vm)
        selectedItemTextView.text = nombreFarmacia
        if(nombrefarm!= null){
            selectedItemTextView.text = nombrefarm
        }

        adaptador = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_1,
            arreglo
        )
        val listView = findViewById<ListView>(R.id.listaMedicinas)
        listView.adapter =  adaptador
        adaptador.notifyDataSetChanged()
        registerForContextMenu(listView)


        val botonCrearBBD = findViewById<Button>(R.id.btn_crear_bdd_m)
        botonCrearBBD.setOnClickListener {

            crearDatosPrueba(barId!!.toInt())
        }
        //Visualizar Todos los Datos
        val botonObtenerTodasBBD = findViewById<Button>(R.id.btn_obtener_todas_bdd_m)
        botonObtenerTodasBBD.setOnClickListener {visualizarDatos(adaptador, barId!!)}

    }

    private fun visualizarDatos(adaptador: ArrayAdapter<BCoctel>, farmaciaId: String) {
        val db = Firebase.firestore
        val cocteles= db.collection("medicines")
        limpiarArreglo()
        adaptador.notifyDataSetChanged()
        cocteles
            .whereEqualTo("bar", farmaciaId)
            .get()
            .addOnSuccessListener {querySnapshot->
                for (document in querySnapshot) {
                    arreglo.
                    add(
                        BCoctel(
                            document.id.toInt(),
                            document.data?.get("nombre") as String?,
                            document.data?.get("fecha") as String?,
                            (document.data?.get("caducado") as String?).toBoolean(),
                            (document.data?.get("precio") as String?)?.toDouble(),
                            (document.data?.get("bar") as String?)?.toInt(),
                        )
                    )
                }
                adaptador.notifyDataSetChanged()
                Toast.makeText(this, "Coctel are displayed!", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {  }
    }

    private fun crearDatosPrueba(farmaciaId: Int) {
        val db = Firebase.firestore
        val medicines = db.collection("medicines")

        val nombre = findViewById<EditText>(R.id.input_nombre_m)
        val fecha = findViewById<EditText>(R.id.input_fecha_m)
        val caducado = findViewById<EditText>(R.id.input_Caducado_m)
        val precio = findViewById<EditText>(R.id.input_precio_m)
        val data1 = hashMapOf(
            "nombre" to nombre.text.toString(),
            "fecha" to fecha.text.toString(),
            "caducado" to caducado.text.toString(),
            "precio" to precio.text.toString(),
            "bar" to farmaciaId.toString()
        )
        val identificador = (Date().time / 1000).toInt()
        medicines
            .document(identificador.toString())
            .set(data1)
            .addOnSuccessListener {
                clearEditText(nombre, fecha, caducado, precio)
                Toast.makeText(this, "Coctel was created!", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {  }
    }
    fun clearEditText(vararg editTexts: EditText) {
        for (editText in editTexts) {
            editText.text.clear()
        }
    }
    fun limpiarArreglo() {
        arreglo.clear()
    }

    override fun onCreateContextMenu(
        menu: ContextMenu?,
        v: View?,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        super.onCreateContextMenu(menu, v, menuInfo)
        val inflater = menuInflater
        inflater.inflate(R.menu.menumedicina, menu)
        val info = menuInfo as AdapterView.AdapterContextMenuInfo
        val id = info.position
        idItemSeleccionado = id
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.editar_medicina->{
                val medicina = arreglo[idItemSeleccionado]
                val nombreMedicina = medicina.nombre
                val idMedicina = medicina.id
                val textView5 = findViewById<TextView>(R.id.textView5)
                textView5.visibility = View.INVISIBLE
                val intent = Intent(this, EditarCoctelFirestore::class.java)
                intent.putExtra("nombre_coctel", nombreMedicina)
                intent.putExtra("id_coctel", idMedicina)
                intent.putExtra("id_bar", barId.toInt())
                val selectedItem = findViewById<TextView>(R.id.selectedItem_vm)
                intent.putExtra("nombre_bar",selectedItem.text)
                startActivity(intent)
                true
            }
            R.id.eliminar_medicina->{
                val selectedItemID = idItemSeleccionado
                val selectedMedicine= arreglo[selectedItemID]
                val selectedMedicineId= selectedMedicine.id
                eliminarRegistro(selectedMedicineId)
                true
            }
            else -> super.onContextItemSelected(item)
        }
    }

    private fun eliminarRegistro(selectedMedicineId: Int) {
        val db = Firebase.firestore
        val refMedicin = db.collection("medicines")
        refMedicin
            .document(selectedMedicineId.toString())
            .delete()
            .addOnSuccessListener {
                adaptador.notifyDataSetChanged()
                Toast.makeText(this, " Coctel has been deleted", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {  }
    }
}