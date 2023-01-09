package com.example.rgr.db

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.rgr.R

class MyAdapter(list: ArrayList<String>): RecyclerView.Adapter<MyAdapter.MyHolder>() {

    private var arrayList = list

    class MyHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        val noteTitle = itemView.findViewById<TextView>(R.id.note)

        fun setData(title: String) {
            noteTitle.text = title
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        val inflater = LayoutInflater.from(parent.context)

        return MyHolder(inflater.inflate(R.layout.note_template, parent, false))
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        holder.setData(arrayList.get(position))
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }

    fun updateAdapter(listItems: List<String>) {
        arrayList.clear()
        arrayList.addAll(listItems)

        notifyDataSetChanged()
    }
}