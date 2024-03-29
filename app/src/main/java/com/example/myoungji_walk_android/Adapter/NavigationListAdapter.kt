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
            val pointIndexTextView = view.findViewById<TextView>(R.id.pointIndexTextView)
            when(guide.type){
                "직진" -> {
                    arrowTextView.text = "직진"
                    arrowImageView.setImageResource(R.drawable.baseline_straight_24)
                }
                "좌회전" -> {
                    arrowTextView.text = "좌측 방향"
                    arrowImageView.setImageResource(R.drawable.baseline_turn_left_24)
                }
                "우회전" -> {
                    arrowTextView.text = "우측 방향"
                    arrowImageView.setImageResource(R.drawable.baseline_turn_right_24)
                }
            }
            distanceItemTextView.text = guide.distance
            pointIndexTextView.text = "${guide.pointIndex}번 포인트에서 "
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