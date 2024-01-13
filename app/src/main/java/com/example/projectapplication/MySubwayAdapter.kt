package com.example.projectapplication

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.projectapplication.databinding.ItemSubwayBinding


class MySubwayViewHolder(val binding: ItemSubwayBinding): RecyclerView.ViewHolder(binding.root)

class MySubwayAdapter(val context: Context, val datas: MutableList<ItemSubwayModel>?): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun getItemCount(): Int {
        return datas?.size ?: 0
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder
        = MySubwayViewHolder(ItemSubwayBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val binding = (holder as MySubwayViewHolder).binding

        //add......................................
        val model = datas!![position] // item_subway와 binding해줌.
        binding.itemTitle.text = model.STATION_NM
        binding.itemName.text =  model.FACI_NM
        binding.itemArea.text = "At" + model.LOCATION
        binding.itemPos.text = model.USE_YN
    }
}

