package com.example.pma_project

import android.content.Intent
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.pma_project.MainActivity
import java.util.*

class DataAdapter(private val dataEntries: ArrayList<Data>?) : RecyclerView.Adapter<DataAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_history, parent, false) as View
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val dataToShow = dataEntries!![position]
        holder.title.text = dataToShow.title
        holder.date.text = dataToShow.date
        holder.image.setImageBitmap(BitmapFactory.decodeFile(dataToShow.pathToImage))
        //        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(v.getContext(), "Recycle Click" + position, Toast.LENGTH_LONG).show();
//            }
//        });
    }

    override fun getItemCount(): Int {
        return dataEntries?.size ?: 0
    }

    class ViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.titleHistoryPage)
        val date: TextView = view.findViewById(R.id.dateHistoryPage)
        val image: ImageView = view.findViewById(R.id.imageHistoryPage)

        init {
            view.setOnClickListener { v ->
                val intent = Intent(v.context, MainActivity::class.java)
                intent.putExtra("position", adapterPosition)
                v.context.startActivity(intent)
            }
        }
    }
}