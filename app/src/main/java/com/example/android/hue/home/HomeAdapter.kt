package com.example.android.hue.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.android.hue.R
import com.example.android.hue.CardItemViewHolder
import com.example.android.hue.database.light.Light
import kotlinx.android.synthetic.main.card_item_view.view.*

class HomeAdapter: RecyclerView.Adapter<CardItemViewHolder>() {

    var data = listOf<Light>()

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: CardItemViewHolder, position: Int) {
        val item = data[position]
        holder.cardView.card_view_text.text = item.name
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardItemViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater
            .inflate(R.layout.card_item_view, parent, false) as CardView
        return CardItemViewHolder(view)
    }
}