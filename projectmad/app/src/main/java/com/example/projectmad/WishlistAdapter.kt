package com.example.projectmad;

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.projectmad.Lists.Companion.removeProduct
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.*


class WishlistAdapter(private val wishlist: List<Prod>) : RecyclerView.Adapter<WishlistViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WishlistViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_view, parent, false)
        return WishlistViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: WishlistViewHolder, position: Int) {
        val product = wishlist[position]
        holder.productNameTextView.text = product.name
        holder.productDateTextView.text = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(product.date)
        holder.removeButton.setOnClickListener {
            val db = FirebaseDatabase.getInstance("https://madproject-19757-default-rtdb.asia-southeast1.firebasedatabase.app")
            db.getReference("wishlist").child(product.id).removeValue()
            removeProduct(product)
            notifyItemRemoved(position)

        }
    }

    override fun getItemCount(): Int {
        return wishlist.size
    }
}