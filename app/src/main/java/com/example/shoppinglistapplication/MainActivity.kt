package com.example.shoppinglistapplication

import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        loadData()
        //loadAutoComplete()
        val addButton = findViewById<Button>(R.id.addButton)
        addButton.setOnClickListener { addItem() }
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
                            val key = itemInfo.key
                            val value = itemInfo.value.toString()
                            addItemToTable(key, value, shoppingList)



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

    fun addItemToTable(key:String,value:String, shoppingList: TableLayout){
        val row = TableRow(this)
        row.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT)
        val item = TextView(this)
        item.apply {
            layoutParams = TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                TableRow.LayoutParams.WRAP_CONTENT)

            text = key
        }
        row.addView(item)
        val amount = TextView(this)
        amount.apply {
            layoutParams = TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                TableRow.LayoutParams.WRAP_CONTENT)
            text = value
        }
        row.addView(amount)
        row.addView(CheckBox(this))
        shoppingList.addView(row)
    }

    /**fun loadAutoComplete(): List<String>{
        val db = FirebaseFirestore.getInstance()
        val list = listOf<String>()

        db.collection("shoppingList").document("aOWMf")
            .get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    Log.d("exist", "DocumentSnapshot data: ${document.data}")


                }
                else {
                    Log.d("noexist", "No such document")
                }
            }
            .addOnFailureListener { exception ->
                Log.d("errordb", "get failed with ", exception)
            }
        return emptyList()
    }*/

    fun addItem(){
        var name = findViewById<EditText>(R.id.itemToAdd).text.toString()
        var amount = findViewById<EditText>(R.id.amountToAdd).text.toString()
        val item = hashMapOf<String, Any>(
            name to amount
        )
        val db = FirebaseFirestore.getInstance()

        db.collection("shoppingList").document("jTGLEUh")
            .set(item, SetOptions.merge())
            .addOnSuccessListener { Log.d("Added", "DocumentSnapshot successfully written!") }
            .addOnFailureListener { e -> Log.w("noAdded", "Error writing document", e) }

        val row = TableRow(this)
        row.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT)

        val shoppingList = findViewById<TableLayout>(R.id.shoppingList)
        addItemToTable(name, amount, shoppingList)
    }
}
