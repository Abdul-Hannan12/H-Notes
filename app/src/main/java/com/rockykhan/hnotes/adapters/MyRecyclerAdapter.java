package com.rockykhan.hnotes.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rockykhan.hnotes.Note;
import com.rockykhan.hnotes.R;

import java.util.ArrayList;

public class MyRecyclerAdapter extends RecyclerView.Adapter<MyRecyclerAdapter.ViewHolder>{

    ArrayList<Note> array;
    Context context;

    public MyRecyclerAdapter(Context context, ArrayList<Note> array) {
        this.context = context;
        this.array = array;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recycler_adapter_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Note model = array.get(position);
        holder.title.setText(model.getTitle());
        holder.text.setText(model.getText());
    }

    @Override
    public int getItemCount() {
        return array.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView title, text;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.notesTitleText);
            text = itemView.findViewById(R.id.notesText);
        }

    }

}
