package com.federicocotogno.minimalnotes.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.federicocotogno.minimalnotes.R
import com.federicocotogno.minimalnotes.data.Note
import com.federicocotogno.minimalnotes.ui.fragments.ListFragmentDirections
import kotlinx.android.synthetic.main.note_item.view.*

class ListAdapter : RecyclerView.Adapter<ListAdapter.MyViewHolder>() {
    private var  notesList = emptyList<Note>()

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        init {

            itemView.setOnClickListener {
                Toast.makeText(itemView.context, "Long press to edit notes.", Toast.LENGTH_SHORT).show()
            }

            itemView.setOnLongClickListener {
                val position = adapterPosition
                Log.d("ListAdapter", position.toString())

                val action = ListFragmentDirections.actionListFragmentToUpdateFragment(notesList[position])
                itemView.findNavController().navigate(action)
                return@setOnLongClickListener true
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.note_item,parent, false))
    }

    override fun getItemCount(): Int {
        return notesList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = notesList[position]
        holder.itemView.tv_title.text = currentItem.title
        holder.itemView.tv_description.text = currentItem.note
        holder.itemView.tv_date.text = currentItem.timeStamp

    }

    fun setData(user: List<Note>) {
        this.notesList = user
        notifyDataSetChanged()
    }
}
