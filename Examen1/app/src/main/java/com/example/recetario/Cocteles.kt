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

class Cocteles : AppCompatActivity() {

    var arregloCoctels: ArrayList<Coctel>? = null
    private lateinit var adaptador: ArrayAdapter<Coctel>
    var coctelSeleccionado = 0
    var barSeleccionado=0

    val callbackContenidoCIntentExplicito =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ){
                result ->
            if(result.resultCode== Activity.RESULT_OK){
                if(result.data !=null){
                    val data= result.data
                    val nombreModificado = data?.getStringExtra("nombreModificado")
                    val contenidoModificado = data?.getIntExtra("contenidoModificado", 0)
                    val precioModificado = data?.getDoubleExtra("precioModifcado", 0.0)
                    val dateModificado = data?.getStringExtra("dateModificado")
                    val tieneAlcoholModificado = data?.getBooleanExtra("tieneAlcoholModificado",false)
                    val formato = SimpleDateFormat("yyyy-MM-dd")
                    val fechaF = formato.parse(dateModificado)
                    val ingredientesModificado = data?.getStringExtra("ingredientesModificado")
                    val preparacionModificado = data?.getStringExtra("preparacionModificado")

                    val ingredientes = ingredientesModificado?.split(", ")?.toTypedArray()
                    var coctel = Coctel(
                        coctelSeleccionado+1,
                    nombreModificado,
                        contenidoModificado,
                    precioModificado,
                        fechaF,
                    tieneAlcoholModificado,
                        ingredientes,
                    preparacionModificado)

                    BaseBares.arregloBares[barSeleccionado]?.coctels?.set(coctelSeleccionado,
                        coctel
                    )
                    adaptador.notifyDataSetChanged()

                }
            }
        }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cocteles)
        //recibir el parametro
         barSeleccionado = intent.getIntExtra("id", 0)
        val bar = BaseBares.arregloBares.find { it.id == barSeleccionado+1 }
        arregloCoctels = bar?.coctels ?: arrayListOf()

        //Crea el list view y el adaptador
        val listView = findViewById<ListView>(R.id.lvCocteles)
        adaptador = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_1,
            arregloCoctels!!
        )
        listView.adapter = adaptador
        adaptador.notifyDataSetChanged()
        registerForContextMenu(listView)


        // Ir a crear coctel
        val botonCrearCoctel = findViewById<Button>(R.id.btnAgregarC)
        botonCrearCoctel
            .setOnClickListener {
                irActividad(IngresoCocteles::class.java)
            }
        //regresar al home
        val botonRegresarAC = findViewById<ImageButton>(R.id.imbRegresarRC)
        botonRegresarAC
            .setOnClickListener {
                devolverRespuesta()
                irActividad(Bares::class.java)
            }
        //regresar al home
        val botonRegresarAHome = findViewById<ImageButton>(R.id.ibRegresarHR)
        botonRegresarAHome
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
        inflater.inflate(R.menu.menur,menu)
        //obtener el id del Arraylist seleccionado
        val info = menuInfo as AdapterView.AdapterContextMenuInfo
        val id = info.position
        coctelSeleccionado=id
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        return when (item.itemId){
            R.id.mi_editar_coctel ->{
                "${coctelSeleccionado}"
                abrirActividadConParametros(editar_cocteles::class.java,coctelSeleccionado,barSeleccionado)
                return true
            }
            R.id.mi_eliminar_coctel ->{
                "${coctelSeleccionado}"
                abrirDialogo(barSeleccionado,coctelSeleccionado)
                return true
            }
            else -> return super.onContextItemSelected(item)
        }

    }

    fun abrirDialogo(cocineroIndex: Int,recetaIndex: Int) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Desea eliminar")
        builder.setPositiveButton("Aceptar") { dialog, which ->
            BaseBares.arregloBares[cocineroIndex]?.coctels?.removeAt(recetaIndex)
            adaptador.notifyDataSetChanged()
            dialog.dismiss()
        }
        builder.setNegativeButton("Cancelar", null)

        val dialog = builder.create()
        dialog.show()
    }

    fun abrirActividadConParametros(
        clase: Class<*>, recetaSeleccionado:Int,cocineroSeleccionado:Int
    ){
        val intentExplito= Intent(this,clase)
        intentExplito.putExtra("cocinero",cocineroSeleccionado)
        intentExplito.putExtra("receta",recetaSeleccionado)

        callbackContenidoCIntentExplicito.launch(intentExplito)

    }

    fun devolverRespuesta(){
        val intentDevolverParametros = Intent()
        intentDevolverParametros.putExtra("nombreModificado","Pamela")
        intentDevolverParametros.putExtra("edadModificado",33)
        setResult(
            RESULT_OK,
            intentDevolverParametros
        )
        finish()
    }


}