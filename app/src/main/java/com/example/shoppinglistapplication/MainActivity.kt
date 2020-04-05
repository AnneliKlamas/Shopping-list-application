package com.example.shoppinglistapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.widget.*
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        loadData()


    }

    fun loadData(){
        // Access a Cloud Firestore instance from your Activity
        val db = FirebaseFirestore.getInstance()

        db.collection("shoppingList").document("jTGLEUh")
            .get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    Log.d("exist", "DocumentSnapshot data: ${document.data}")
                    val data = document.data
                    if (data != null) {
                        val shoppingList = findViewById<TableLayout>(R.id.shoppingList)
                        for (itemInfo in data){

                            val row = TableRow(this)
                            row.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT)
                            val item = TextView(this)
                            item.apply {
                                layoutParams = TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                                    TableRow.LayoutParams.WRAP_CONTENT)
                                val key = itemInfo.key
                                text = key
                            }
                            row.addView(item)
                            val amount = TextView(this)
                            amount.apply {
                                layoutParams = TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                                    TableRow.LayoutParams.WRAP_CONTENT)
                                text = itemInfo.value.toString()
                            }
                            row.addView(amount)
                            row.addView(CheckBox(this))
                            shoppingList.addView(row)

                        }
                    }
                }
                else {
                    Log.d("noexist", "No such document")
                }
            }
            .addOnFailureListener { exception ->
                Log.d("errordb", "get failed with ", exception)
            }
    }


}
