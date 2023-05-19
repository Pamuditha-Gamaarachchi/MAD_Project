package com.example.projectmad

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.FirebaseApp
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.NonCancellable.start
import java.util.*

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        FirebaseApp.initializeApp(this)

        val textViewMultiLine: TextView = findViewById(R.id.TextViewMultiLine)
        val button8: Button = findViewById(R.id.button8)
        val button9: Button = findViewById(R.id.button9)
        val textView: TextView = findViewById(R.id.textView)
        val textView2: TextView = findViewById(R.id.textView2)
        val button6: Button = findViewById(R.id.button6)
        val wishlist: ImageView = findViewById(R.id.imageView2)

        var product: Product? = null

        //database connection
        val db = FirebaseDatabase.getInstance("https://madproject-19757-default-rtdb.asia-southeast1.firebasedatabase.app")

        db.getReference("products")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    println(snapshot.toString())
                    for (productSnapshot in snapshot.children) {
                        Log.d("TAG", productSnapshot.toString())
                        println(productSnapshot.toString())
                        val id = productSnapshot.key
                        val name = productSnapshot.child("name").getValue(String::class.java)
                        val description = productSnapshot.child("description").getValue(String::class.java)
                        val price = productSnapshot.child("price").getValue(Double::class.java)
                        val rating = productSnapshot.child("rating").getValue(Double::class.java)

                        product = if (id != null && name != null && description != null && price != null && rating != null) {
                            Product(id, name, description, price, rating)
                        } else {
                            null
                        }

                        textView.text = product?.name
                        textView2.text = "$ " + product?.price.toString()
                        textViewMultiLine.text = product?.description
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.w(TAG, "Error getting products", error.toException())
                }
            })

        val lists = Lists

        db.getReference("wishlist")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (productSnapshot in snapshot.children) {
                        Log.d("TAG", productSnapshot.toString())
                        val id = productSnapshot.key
                        val name = productSnapshot.child("name").getValue(String::class.java)
                        val date = productSnapshot.child("date").getValue(Date::class.java)

                        if (id != null && name != null && date != null) {
                            lists.addProduct(Prod(id, name, date))
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.w(TAG, "Error getting wishlist", error.toException())
                }
            })


        wishlist.setOnClickListener{
            val intent = Intent(this, Wishlist::class.java)
            startActivity(intent)
        }

        button6.setOnClickListener {
            println(product)
            product?.let {
                val productId = it.id
                val productName = it.name
                val currentDate = Date()

                val prod = Prod(productId, productName, currentDate)
                lists.addProduct(prod)

                val productMap = mapOf(
                    "id" to productId,
                    "name" to productName,
                    "date" to currentDate
                )
                db.getReference("wishlist").child(productId).setValue(productMap)

                Toast.makeText(applicationContext, "Added to the wishlist", Toast.LENGTH_SHORT).show()
            }
        }

        button9.setOnClickListener {
            val intent = Intent(this, Interested::class.java)
            startActivity(intent)
        }

        button8.setOnClickListener {
            val intent = Intent(this, Rate::class.java)
            intent.putExtra("id", product!!.id)
            startActivity(intent)
        }


    }
}