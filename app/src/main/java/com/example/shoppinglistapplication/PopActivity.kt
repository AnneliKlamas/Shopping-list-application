package com.example.shoppinglistapplication

import android.content.Intent
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.widget.Button
import android.widget.TableLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions


class PopActivity : AppCompatActivity() {

    private var name = ""
    private var amount = ""
    private var oldAmount = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_pop)

        val bundle = intent.extras
        name = bundle?.getString("name", "name") ?: ""
        amount = bundle?.getString("amount", "amount") ?: ""
        oldAmount = bundle?.getString("oldAmount", "oldAmount") ?: ""

        val dm = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(dm)

        val width = dm.widthPixels
        val height = dm.heightPixels

        getWindow().setLayout((width*0.8).toInt(), (height*0.4).toInt())

        val existingItemText = findViewById<TextView>(R.id.existingItemText)
        existingItemText.setText("Item: " + name)
        existingItemText.setText(name)
        val existingAmountText = findViewById<TextView>(R.id.existingAmountText)
        existingAmountText.setText("Amount: " + amount)
        existingAmountText.setText(oldAmount)
        //val newAmountText = findViewById<TextView>(R.id.newItemAmount)
        //newAmountText.setText(amount)

        val cancelButton = findViewById<Button>(R.id.cancelButton)
        //val replaceButton = findViewById<Button>(R.id.replaceButton)
        cancelButton.setOnClickListener {
            finish()
        }
        //replaceButton.setOnClickListener {
          //  addItem(name, amount)
            //startActivity(Intent(
              //  applicationContext,
                //MainActivity::class.java)
            //)}


    }
    override fun onBackPressed() {
    }

    //fun addItem(name:String, amount:String){
      //  updateItem(amount, false, name)
        //val shoppingList = findViewById<TableLayout>(R.id.shoppingList)
    //}

    //fun updateItem(amount: String, checkBox: Boolean, name: String){
      //  val db = FirebaseFirestore.getInstance()
        //val array = ArrayList<Any>()
        //array.add(amount)
        //array.add(checkBox)
        //val item = hashMapOf<String, ArrayList<Any>>(
          //  name to array
        //)
        //db.collection("shoppingList").document("jTGLEUh")
          //  .set(item as Map<String, Any>, SetOptions.merge())
            //.addOnSuccessListener {
              //  Log.d(
                //    "Added",
                  //  "DocumentSnapshot successfully written!"
                //)
            //}
            //.addOnFailureListener { e -> Log.w("noAdded", "Error writing document", e) }
    //}
}