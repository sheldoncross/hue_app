package com.example.android.hue.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.android.hue.R
import com.example.android.hue.CardItemViewHolder
import com.example.android.hue.database.light.Light
import kotlinx.android.synthetic.main.card_item_view.view.*

class HomeAdapter(var lightSwitchListener: LightSwitchListener):
    RecyclerView.Adapter<CardItemViewHolder>() {

    //List of lights obtained from the database
    var data = listOf<Light>()

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: CardItemViewHolder, position: Int) {
        //Change current item to the next light in the data list
        val item = data[position]
        //Set the text for the card view to equal the current lights name
        holder.cardView.card_view_text.text = item.name
        holder.cardView.light_switch.setOnCheckedChangeListener { _, isChecked ->
            if(isChecked){
                lightSwitchListener.idNumber = item.idNumber
                lightSwitchListener.onButtonClick()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardItemViewHolder {
        //Get the layout inflater from the parent
        val layoutInflater = LayoutInflater.from(parent.context)
        //Inflate the card item view layout as a card view
        val view = layoutInflater
            .inflate(R.layout.card_item_view, parent, false) as CardView
        return CardItemViewHolder(view)
    }
}