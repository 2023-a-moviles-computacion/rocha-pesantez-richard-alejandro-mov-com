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

class ListViewBar : AppCompatActivity() {
    var idItemSeleccionado = 0
    lateinit var listaBares: ArrayList<BBar>
    lateinit var adapter: ArrayAdapter<BBar>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_view_bar)
        val botonCrearBBD = findViewById<Button>(R.id.btn_crear_bdd_i)
        botonCrearBBD
            .setOnClickListener {
                val nombre = findViewById<EditText>(R.id.input_nombre_i)
                val fecha = findViewById<EditText>(R.id.input_fecha_i)
                val open = findViewById<EditText>(R.id.input_Open_i)
                val stackValue = findViewById<EditText>(R.id.input_stockValor_i)
                EBaseDeDatos.tablaBar!!.crearFarmacia(
                    nombre.text.toString(),
                    fecha.text.toString(),
                    open.text.toString().toBoolean(),
                    stackValue.text.toString().toDouble()
                )
            }
        val botonObtenerTodasBBD = findViewById<Button>(R.id.btn_obtener_todas_bdd_p)
        botonObtenerTodasBBD.setOnClickListener {
            listaBares = EBaseDeDatos.tablaBar!!.obtenerTodasLosBares()

            adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, listaBares)
            val listView = findViewById<ListView>(R.id.listaFarmacias)
            listView.adapter = adapter
            registerForContextMenu(listView)

            val textView4 = findViewById<TextView>(R.id.textView4)
            textView4.text = "Nombre     FechaApertura   ContinuaAbierto   ValorStock"
            textView4.visibility = View.VISIBLE

        }
    }

    override fun onCreateContextMenu(
        menu: ContextMenu?,
        v: View?,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        super.onCreateContextMenu(menu, v, menuInfo)
        val inflater = menuInflater
        inflater.inflate(R.menu.menufarmacia, menu)
        val info = menuInfo as AdapterView.AdapterContextMenuInfo
        val id = info.position
        idItemSeleccionado = id
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.editar_farmacia -> {
                val bar = listaBares[idItemSeleccionado]
                val nombreBar = bar.nombre
                val idBar = bar.id
                val textView4 = findViewById<TextView>(R.id.textView4)
                textView4.visibility = View.INVISIBLE
                val intent = Intent(this, EditarBar::class.java)
                intent.putExtra("nombre_bar", nombreBar)
                intent.putExtra("id_bar", idBar)

                startActivity(intent)

                true
            }
            R.id.eliminar_farmacia -> {
                val bar = listaBares[idItemSeleccionado]
                val id = bar.id

                EBaseDeDatos.tablaBar!!.eliminarBarFormulario(id)

                listaBares.removeAt(idItemSeleccionado)
                adapter.notifyDataSetChanged()

                true
            }
            R.id.anadir_medicina->{
                val bar = listaBares[idItemSeleccionado]
                val nombreBar = bar.nombre
                val idBar = bar.id
                val textView4 = findViewById<TextView>(R.id.textView4)
                textView4.visibility = View.INVISIBLE
                val intent = Intent(this, ListViewCoctel::class.java)
                intent.putExtra("nombre_bar", nombreBar)
                intent.putExtra("id_bar", idBar)
                startActivity(intent)
                true
            }
            else -> super.onContextItemSelected(item)
        }
    }
}
