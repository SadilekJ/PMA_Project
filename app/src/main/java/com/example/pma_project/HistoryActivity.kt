package com.example.pma_project

import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import android.os.Bundle
import android.view.View
import com.example.pma_project.R
import com.example.pma_project.MainActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pma_project.DataAdapter

class HistoryActivity : AppCompatActivity() {
    private var dataEntries: RecyclerView? = null
    private var adapter: RecyclerView.Adapter<*>? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)
        val dataEntries = MainActivity.data
        this.dataEntries = findViewById<View>(R.id.dataEntries) as RecyclerView
        val mLayoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this)
        this.dataEntries!!.layoutManager = mLayoutManager
        adapter = DataAdapter(dataEntries)
        this.dataEntries!!.adapter = adapter
    }
}