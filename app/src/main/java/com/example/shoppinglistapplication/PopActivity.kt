package com.example.shoppinglistapplication

import android.content.Intent
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.widget.Button
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
        existingItemText.setText(name)
        val existingAmountText = findViewById<TextView>(R.id.existingAmountText)
        existingAmountText.setText(oldAmount)
        val newAmountText = findViewById<TextView>(R.id.newItemAmount)
        newAmountText.setText(amount)

        val cancelButton = findViewById<Button>(R.id.cancelButton)
        cancelButton.setOnClickListener {
            finish()
        }
        val replaceButton = findViewById<Button>(R.id.replaceButton)
        replaceButton.setOnClickListener {
            updateItem(name, amount)
            startActivity(Intent(
                applicationContext,
                MainActivity::class.java)
            )}
    }

    /**
     * Function that doesn't allow to use back button
     */
    override fun onBackPressed() {
    }

    /**
     * Function that updates database, when user decides to override his previous item.
     */

    fun updateItem(amount: String, name: String){
        val db = FirebaseFirestore.getInstance()
        val array = ArrayList<Any>()
        array.add(amount)
        array.add(false)
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
}
