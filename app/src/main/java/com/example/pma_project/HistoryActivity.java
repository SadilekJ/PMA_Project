package com.example.pma_project;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import static com.example.pma_project.MainActivity.data;

public class HistoryActivity extends AppCompatActivity {

    private RecyclerView dataEntries;
    private RecyclerView.Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        ArrayList<Data> dataEntries = data;

        this.dataEntries = (RecyclerView)findViewById(R.id.dataEntries);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        this.dataEntries.setLayoutManager(mLayoutManager);

        adapter = new DataAdapter(dataEntries);
        this.dataEntries.setAdapter(adapter);
    }

}
