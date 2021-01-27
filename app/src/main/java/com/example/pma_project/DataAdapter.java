package com.example.pma_project;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class DataAdapter extends RecyclerView.Adapter<DataAdapter.ViewHolder> {

    private ArrayList<Data> dataEntries;

    public DataAdapter(ArrayList<Data> dataEntries){
        this.dataEntries = dataEntries;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View v = (View) LayoutInflater.from(parent.getContext()).inflate(R.layout.item_history, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Data dataToShow = dataEntries.get(position);

        holder.title.setText(dataToShow.getTitle());
        holder.date.setText(dataToShow.getDate());

        holder.image.setImageBitmap(BitmapFactory.decodeFile(dataToShow.getPathToImage()));
//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(v.getContext(), "Recycle Click" + position, Toast.LENGTH_LONG).show();
//            }
//        });
    }

    @Override
    public int getItemCount() {
        if (dataEntries != null) {
            return dataEntries.size();
        } else {
            return 0;
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public final View view;
        public final TextView title;
        public final TextView date;
        public final ImageView image;


        public ViewHolder(@NonNull View view) {
            super(view);
            this.view = view;
            title = view.findViewById(R.id.titleHistoryPage);
            date = view.findViewById(R.id.dateHistoryPage);
            image = view.findViewById(R.id.imageHistoryPage);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(),MainActivity.class);
                    intent.putExtra("position", getAdapterPosition());
                    v.getContext().startActivity(intent);
                }
            });
        }
    }
}
