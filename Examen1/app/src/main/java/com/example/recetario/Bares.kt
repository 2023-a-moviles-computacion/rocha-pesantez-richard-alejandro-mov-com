package com.example.recetario

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ContextMenu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageButton
import android.widget.ListView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import java.text.SimpleDateFormat

class Bares : AppCompatActivity() {
    private lateinit var adaptador: ArrayAdapter<Bar>
    val arregloBareS= BaseBares.arregloBares
    var bareSSeleccionado = 0

    val callbackContenidoCIntentExplicito =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ){
                result ->
            if(result.resultCode== Activity.RESULT_OK){
                if(result.data !=null){
                    val data= result.data
                    val nombreModificado = data?.getStringExtra("nombreModificado")
                    val aforoModificado = data?.getIntExtra("aforoModificado", 0)
                    val areaModificado = data?.getDoubleExtra("areaModificado", 0.0)
                    val dateModificado = data?.getStringExtra("dateModificado")
                    val tieneParqueaderoModificado = data?.getBooleanExtra("tieneParqueaderoModificado",false)
                    val formato = SimpleDateFormat("yyyy-MM-dd")
                    val fechaF = formato.parse(dateModificado)

                    // Update the corresponding Cocinero object in the arregloCocineros list
                    val bar = arregloBareS[bareSSeleccionado]
                    bar.nombre = nombreModificado
                    bar.aforo = aforoModificado
                    bar.area=areaModificado
                    bar.fechaIntegracion=fechaF
                    bar.tieneParqueadero=tieneParqueaderoModificado
                    BaseBares.arregloBares[bareSSeleccionado] = bar

                    adaptador.notifyDataSetChanged()
                }
            }
        }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bares)

        //Crea el list view y el adaptador
        val listView = findViewById<ListView>(R.id.lvBares)
        adaptador = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_1,
            BaseBares.arregloBares
        )
        listView.adapter = adaptador
        adaptador.notifyDataSetChanged()
        registerForContextMenu(listView)


        val botonAgregarBar = findViewById<Button>(R.id.btnAgregarC)
        botonAgregarBar
            .setOnClickListener {
                irActividad(ingreso_bares::class.java)
            }

        val botonRegresarMC = findViewById<ImageButton>(R.id.btnBackC)
        botonRegresarMC
            .setOnClickListener {
                irActividad(MainActivity::class.java)
            }

        val botonRegresarHC = findViewById<ImageButton>(R.id.imbHomeC)
        botonRegresarHC
            .setOnClickListener {
                irActividad(MainActivity::class.java)
            }
    }

    fun irActividad(
        clase: Class<*>
    ){
        val intent= Intent(this,clase)
        startActivity(intent)
    }

    override fun onCreateContextMenu(
        menu: ContextMenu?,
        v: View?,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        super.onCreateContextMenu(menu, v, menuInfo)
        //Llenamos las opciones del menu
        val inflater = menuInflater
        inflater.inflate(R.menu.menu,menu)
        //obtener el id del Arraylist seleccionado
        val info = menuInfo as AdapterView.AdapterContextMenuInfo
        val id = info.position
        bareSSeleccionado=id
    }


    override fun onContextItemSelected(item: MenuItem): Boolean {
        return when (item.itemId){
            R.id.miEditarB ->{
                "${bareSSeleccionado}"
                abrirActividadConParametros(editar_bar::class.java,bareSSeleccionado)
                return true
            }
            R.id.miBorrarB ->{
                "${bareSSeleccionado}"
                abrirDialogo(bareSSeleccionado)
                return true
            }
            R.id.miCocteles ->{
                "${bareSSeleccionado}"
                abrirActividadConParametros(Cocteles::class.java,bareSSeleccionado)
                return true
            }
            else -> return super.onContextItemSelected(item)
        }

    }


    fun abrirDialogo(cocineroIndex: Int) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Desea eliminar")
        builder.setPositiveButton("Aceptar") { dialog, which ->
            BaseBares.arregloBares.removeAt(cocineroIndex)
            adaptador.notifyDataSetChanged()
            dialog.dismiss()
        }
        builder.setNegativeButton("Cancelar", null)

        val dialog = builder.create()
        dialog.show()
    }

    fun abrirActividadConParametros(
        clase: Class<*>, cocineroSeleccionado:Int
    ){
        val intentExplito= Intent(this,clase)
        intentExplito.putExtra("id",cocineroSeleccionado)

        callbackContenidoCIntentExplicito.launch(intentExplito)

    }



}