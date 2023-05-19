package com.example.projectmad;

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase

public class Rate : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_home3)
        val db = FirebaseDatabase.getInstance("https://madproject-19757-default-rtdb.asia-southeast1.firebasedatabase.app")

        var id = intent.getStringExtra("id")
//        var rate = intent.getDoubleExtra("rating" , 0.0)
//        var ra = rate.toFloat()

        var submit: Button = findViewById(R.id.button9)
        var rating: RatingBar = findViewById(R.id.ratingBar)
        var wishlist: ImageView = findViewById(R.id.imageView17)
        var home: ImageView = findViewById(R.id.imageView12)

        var rate = 0.0 // Initialize rate variable
        db.getReference("products").child(id.toString())
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val rate = snapshot.child("rating").getValue(Double::class.java) ?: 0.0
                        Log.d("TAG", "DocumentSnapshot data: $rating")
                        rating.rating = rate.toFloat()
                    } else {
                        Log.d(TAG, "No such document")
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.d(TAG, "get failed with ", error.toException())
                }
            })

        //rating calculation
        rating.onRatingBarChangeListener = RatingBar.OnRatingBarChangeListener { _, r, _ ->
            Log.d("TAG", "rating: $r")
            rate = (rate?.plus(r))?.div(2)!!
        } // getting average value -

        submit.setOnClickListener {
            db.getReference("products").child(id.toString()).child("rating")
                .setValue(rate)
                .addOnSuccessListener {
                    Log.d(TAG, "DocumentSnapshot successfully updated!")
                    Toast.makeText(this, "Rating submitted", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener { e ->
                    Log.w(TAG, "Error updating document", e)
                }


        }
        // Handle the wishlist icon click
        wishlist.setOnClickListener {
            var intent  = Intent(this, Wishlist::class.java)
            startActivity(intent)
        }
        // Handle the home icon click
        home.setOnClickListener {
            finish()
        }  // Finish the current activity
    }
}
