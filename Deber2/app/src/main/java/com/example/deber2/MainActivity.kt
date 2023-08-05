package com.example.deber2

import android.location.GnssAntennaInfo.Listener
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    private lateinit var edName: EditText
    private lateinit var edEmail: EditText
    private lateinit var btnAdd: Button
    private lateinit var btnView: Button

    private lateinit var sqliteHelper : SQLiteHelper
    private lateinit var recyclerView: RecyclerView
    private var adapter: StudentAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initView()
        initRecyclerView()
        sqliteHelper = SQLiteHelper(this)

        btnAdd.setOnClickListener{addStudent()}
        btnView.setOnClickListener { getStudents() }
    }

    private fun getStudents(){
        val stdList = sqliteHelper.getAllStudent()
        Log.e("pppp","${stdList.size}")

        //display in Recycler View
        adapter?.addItems(stdList)

    }

    private fun addStudent(){
        val name = edName.text.toString()
        val email = edEmail.text.toString()

        if(name.isEmpty() || email.isEmpty()){
            Toast.makeText(this, "Digite el campo requerido", Toast.LENGTH_SHORT).show()
        }else{
            val std= StudentModel(name=name, email = email)
            val status = sqliteHelper.insertStudent(std)

            //check insert sucess or not
            if(status > -1){
                Toast.makeText(this, "Student Added...", Toast.LENGTH_SHORT).show()
                clearEditText()
                getStudents()
            }else{
                Toast.makeText(this, "No saved...", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun clearEditText(){
        edName.setText("")
        edEmail.setText("")
        edName.requestFocus()
    }


    private fun initRecyclerView(){
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = StudentAdapter()
        recyclerView.adapter = adapter
    }

    private fun initView(){
        edName = findViewById(R.id.edName)
        edEmail = findViewById(R.id.edEmail)
        btnAdd = findViewById(R.id.btnAdd)
        btnView = findViewById(R.id.btnView)
        recyclerView = findViewById(R.id.recyclerView)
    }




}