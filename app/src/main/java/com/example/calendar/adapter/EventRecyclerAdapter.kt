package com.example.calendar.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.calendar.DB.DBOpenHelper
import com.example.calendar.Events
import com.example.calendar.OnClickItemListener
import com.example.calendar.R

class EventRecyclerAdapter(var context: Context, private var listEvent: MutableList<Events>,
                           private var listener : OnClickItemListener
) :
    RecyclerView.Adapter<EventRecyclerAdapter.ViewHolder>() {

    private lateinit var  dbOpen : DBOpenHelper
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val time :TextView = itemView.findViewById(R.id.item_event_time)
        val title :TextView = itemView.findViewById(R.id.item_event_title)
        val delete : ImageView = itemView.findViewById(R.id.item_event_delete)
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
        holder.delete.setOnClickListener{
//            deleteEvent(listEvent[position].Event,listEvent[position].Date,listEvent[position].Time)
//            listEvent.removeAt(position)
//            notifyItemRemoved(position)
            listener.onClick(position)
        }
    }

    private fun deleteEvent(event: String , date: String, time : String){
        dbOpen = DBOpenHelper(context)
        val db  = dbOpen.writableDatabase
        dbOpen.deleteEvent(event,date,time,db)
        dbOpen.close()
    }
}