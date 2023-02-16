package com.example.calendar

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class EventRecyclerAdapter(var context: Context?, private var listEvent: MutableList<Events>) :
    RecyclerView.Adapter<EventRecyclerAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val time :TextView = itemView.findViewById(R.id.item_event_time)
        val title :TextView = itemView.findViewById(R.id.item_event_title)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.
        from(parent.context).inflate(R.layout.event_item,parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listEvent.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.time.text = listEvent[position].Time
        holder.title.text = listEvent[position].Event
    }
}