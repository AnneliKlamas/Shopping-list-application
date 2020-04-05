package com.example.shoppinglistapplication

import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FieldValue
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
        val deleteCheckedButton = findViewById<Button>(R.id.deleteCheckedButton)
        deleteCheckedButton.setOnClickListener { deleteItem() }
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
        var nameField = findViewById<EditText>(R.id.itemToAdd)
        var name = nameField.text.toString()
        var amountField = findViewById<EditText>(R.id.amountToAdd)
        var amount= amountField.text.toString()
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

        nameField.setText("")
        amountField.setText("")


    }

    fun deleteItem(){
        val shoppingList = findViewById<TableLayout>(R.id.shoppingList)
        val removable = ArrayList<Int>()
        for (i in 1 until shoppingList.childCount){
            val row = shoppingList.getChildAt(i) as TableRow
            val checkBox = row.getChildAt(2) as CheckBox
            if (checkBox.isChecked){
                val item = row.getChildAt(0) as TextView
                deleteFromDatabase(item.text.toString())
                removable.add(i)
            }
        }
        removable.reverse()
        for (i in 0 until removable.size){
            shoppingList.removeViewAt(removable[i])
        }
    }

    fun deleteFromDatabase(item: String){
        val db = FirebaseFirestore.getInstance()
        val docRef = db.collection("shoppingList").document("jTGLEUh")

        val updates = hashMapOf<String, Any>(
            item to FieldValue.delete()
        )

        docRef.update(updates).addOnCompleteListener { }
    }
}
