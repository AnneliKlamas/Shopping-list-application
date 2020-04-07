package com.example.shoppinglistapplication

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        loadData()
        val addButton = findViewById<Button>(R.id.addButton)
        addButton.setOnClickListener { addItem() }
        val deleteCheckedButton = findViewById<Button>(R.id.deleteCheckedButton)
        deleteCheckedButton.setOnClickListener { deleteItem() }
        val deleteAllButton = findViewById<Button>(R.id.deleteAllButton)
        deleteAllButton.setOnClickListener { deleteAll(true) }
        val refreshButton = findViewById<Button>(R.id.refreshButton)
        refreshButton.setOnClickListener {
            deleteAll(false)
            loadData()}
    }

    /**
     * Function that loads data from Cloud Firestore to application's table
     * Uses: addItemToTable()
     * Is used by: onCreate()
     */
    fun loadData(){
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
                            val array = itemInfo.value as ArrayList<*>
                            val amount = array[0] as String
                            val isChecked = array[1] as Boolean
                            addItemToTable(key, amount, isChecked, shoppingList)
                            val deleteCheckedButton = findViewById<Button>(R.id.deleteCheckedButton)
                            val deleteAllButton = findViewById<Button>(R.id.deleteAllButton)
                            deleteCheckedButton.setVisibility(View.VISIBLE)
                            deleteAllButton.setVisibility(View.VISIBLE)
                            shoppingList.setVisibility(View.VISIBLE)
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

    /**
     * Function that puts one item into the tabel but not in the database.
     * Uses: none
     * Is used by: addItem(); loadData()
     */
    fun addItemToTable(key:String,value:String, isChecked:Boolean, shoppingList: TableLayout){
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
        val checkBox = CheckBox(this)
        if (isChecked){
            checkBox.setChecked(true)
        }
        row.addView(checkBox)
        shoppingList.addView(row)
    }

    /**
     * Function that puts data into database
     * Uses: none
     * Is used by: deleteAll(); addItem()
     */
    fun updateItem(amount: String, checkBox: Boolean, name: String){
        val db = FirebaseFirestore.getInstance()
        val array = ArrayList<Any>()
        array.add(amount)
        array.add(checkBox)
        val item = hashMapOf<String, ArrayList<Any>>(
            name to array
        )
        db.collection("shoppingList").document("jTGLEUh")
            .set(item as Map<String, Any>, SetOptions.merge())
            .addOnSuccessListener {
                Log.d(
                    "Added",
                    "DocumentSnapshot successfully written!"
                )
            }
            .addOnFailureListener { e -> Log.w("noAdded", "Error writing document", e) }
    }

    /**
     * Function that deletes selected items
     * Uses: deleteFromDatabase()
     * Is used by: onCreate()
     */
    fun deleteItem(){
        val shoppingList = findViewById<TableLayout>(R.id.shoppingList)
        val removable = ArrayList<Int>()

        //Proovi ka shoppingList.childCount downTo 1 ja siis võibolla
        // polegi vaja reverse ja ka teist tsüklit
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
        if (shoppingList.childCount==1){
            deleteCheckedButton.setVisibility(View.GONE)
            deleteAllButton.setVisibility(View.GONE)
            shoppingList.setVisibility(View.GONE)
        }
    }

    /**
     * Function that deletes specified item (key) from database
     * Uses: none
     * Is used by: deleteItem(); deleteAll()
     */
    fun deleteFromDatabase(item: String){
        val db = FirebaseFirestore.getInstance()
        val docRef = db.collection("shoppingList").document("jTGLEUh")
        val updates = hashMapOf<String, Any>(
            item to FieldValue.delete()
        )
        docRef.update(updates).addOnCompleteListener { }
    }

    /**
     * Function that deletes all items. Is also used when refreshing table.
     * Uses: deleteFromDatabase(); updateItem()
     * Is used by: onCreate()
     */
    fun deleteAll(fromdatabase: Boolean){
        val shoppingList = findViewById<TableLayout>(R.id.shoppingList)
        while (shoppingList.childCount>1){
            val row = shoppingList.getChildAt(1) as TableRow
            val name = row.getChildAt(0) as TextView
            val amount = row.getChildAt(1) as TextView
            val checkBox = row.getChildAt(2) as CheckBox
            if(fromdatabase){
                deleteFromDatabase(name.text.toString())
            }
            else {
                updateItem(amount.text.toString(), checkBox.isChecked, name.text.toString())
            }
            shoppingList.removeViewAt(1)
            deleteCheckedButton.setVisibility(View.GONE)
            deleteAllButton.setVisibility(View.GONE)
            shoppingList.setVisibility(View.GONE)
        }
    }

    /**
     * Function that checks if there already exist item that is being added at the moment
     * Uses: none
     * Is used by: addItem()
     */
    fun checkIfItemExists(item:String, amount:String) :Boolean{
        var shoppingList = findViewById<TableLayout>(R.id.shoppingList)

        for (i in 1 until shoppingList.childCount){
            val row = shoppingList.getChildAt(i) as TableRow
            val name = row.getChildAt(0) as TextView
            val oldAmount = row.getChildAt(1) as TextView
            if(item.equals(name.text.toString())){

                val intent = Intent(this, PopActivity::class.java)
                intent.putExtra("name", item)
                intent.putExtra("amount", amount)
                intent.putExtra("oldAmount", oldAmount.text.toString())
                startActivity(intent)
                return true
            }
        }
        return false
    }

    /**
     * Function that adds item
     * Uses: checkIfItemExists(); addItemToTable()
     * Is used by: onCreate()
     */
    fun addItem(){
        var nameField = findViewById<EditText>(R.id.itemToAdd)
        var name = nameField.text.toString()
        var amountField = findViewById<EditText>(R.id.amountToAdd)
        var amount= amountField.text.toString()

        if (name.trim()==""){
            nameField.setError("Empty value!")
        }

        else{
            //val errorCharacters = mapOf("." to ",", "/" to "|", "*" to "×", "~" to "-")

            if (name.contains(".")){
                name = name.replace(".",",")
            }
            if (name.contains("/")){
                name = name.replace("/","|")
            }
            if (name.contains("*")){
                name = name.replace("*","×")
            }
            if (name.contains("~")){
                name = name.replace("~","-")
            }
            if (name.contains("[") ){
                name = name.replace("[","|")
            }
            if (name.contains("]") ){
                name = name.replace("]","|")
            }
            if(!checkIfItemExists(name, amount)){

            updateItem(amount, false, name)

            val row = TableRow(this)
            row.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT)

            val shoppingList = findViewById<TableLayout>(R.id.shoppingList)
            addItemToTable(name, amount, false, shoppingList)
                deleteCheckedButton.setVisibility(View.VISIBLE)
                deleteAllButton.setVisibility(View.VISIBLE)
                shoppingList.setVisibility(View.VISIBLE)
            }
            nameField.setText("")
            amountField.setText("")
        }
    }
    override fun onStop() {
        // call the superclass method first
        super.onStop()
        deleteAll(false)
    }

}
