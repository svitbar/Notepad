package com.example.rgr.db

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.rgr.EditActivity
import com.example.rgr.R

class MyAdapter(list: ArrayList<ListOfNote>,
                mainActivityContext: Context): RecyclerView.Adapter<MyAdapter.MyHolder>() {

    private var arrayList = list
    private var context = mainActivityContext

    class MyHolder(itemView: View, viewHolderContext: Context): RecyclerView.ViewHolder(itemView) {

        private val noteTitle: TextView = itemView.findViewById(R.id.note)
        private val noteTime: TextView = itemView.findViewById(R.id.timeText)
        private val context = viewHolderContext

        fun setData(item: ListOfNote) {
            noteTitle.text = item.title
            noteTime.text = item.time

            itemView.setOnClickListener {

                val intent = Intent(context, EditActivity::class.java).apply {

                    putExtra(MyIntentConstants.I_TITLE_KEY, item.title)
                    putExtra(MyIntentConstants.I_DESC_KEY, item.desc)
                    putExtra(MyIntentConstants.I_URI_KEY, item.uri)
                    putExtra(MyIntentConstants.I_ID_KEY, item.id)
                }
                context.startActivity(intent)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        val inflater = LayoutInflater.from(parent.context)

        return MyHolder(inflater.inflate(R.layout.note_template, parent, false), context)
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        holder.setData(arrayList[position])
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }

    fun updateAdapter(listItems: ArrayList<ListOfNote>) {
        arrayList.clear()
        arrayList.addAll(listItems)

        notifyDataSetChanged()
    }

    fun removeItem(pos: Int, dbManager: DbManager) {

        dbManager.removeItemFromDb(arrayList[pos].id.toString())
        arrayList.removeAt(pos)
        notifyItemRangeChanged(0, arrayList.size)
        notifyItemRemoved(pos)
    }
}