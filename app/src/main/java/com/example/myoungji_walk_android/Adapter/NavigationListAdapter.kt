package com.example.myoungji_walk_android.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.myoungji_walk_android.Model.Guide
import com.example.myoungji_walk_android.R

class NavigationListAdapter: ListAdapter<Guide, NavigationListAdapter.ItemViewHolder>(differ) {

    inner class ItemViewHolder(val view: View): RecyclerView.ViewHolder(view){
        fun bind(guide: Guide){
            val arrowTextView = view.findViewById<TextView>(R.id.arrowTextView)
            val distanceItemTextView = view.findViewById<TextView>(R.id.distanceItemTextView)
            val arrowImageView = view.findViewById<ImageView>(R.id.arrowImageView)
            when(guide.type){
                1 -> {
                    arrowTextView.text = "직진"
                    arrowImageView.setImageResource(R.drawable.baseline_straight_24)
                }
                2 -> {
                    arrowTextView.text = "왼쪽 방향"
                    arrowImageView.setImageResource(R.drawable.baseline_turn_left_24)
                }
                3 -> {
                    arrowTextView.text = "오른쪽 방향"
                    arrowImageView.setImageResource(R.drawable.baseline_turn_right_24)
                }
            }
            distanceItemTextView.text = guide.distance
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ItemViewHolder(inflater.inflate(R.layout.item_navigation, parent, false))
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    companion object {
        val differ = object: DiffUtil.ItemCallback<Guide>() {
            override fun areItemsTheSame(oldItem: Guide, newItem: Guide): Boolean {
                return oldItem.pointIndex == newItem.pointIndex
            }

            override fun areContentsTheSame(oldItem: Guide, newItem: Guide): Boolean {
                return oldItem == newItem
            }

        }
    }
}