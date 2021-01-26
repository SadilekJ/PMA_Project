package com.example.pma_project;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import static com.example.pma_project.MainActivity.data;

public class HistoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
    }

    public void onHomeClick(View v)
    {
        this.finish();
    }

    public void showItems(View v)
    {
        data.iterator();
    }
}
