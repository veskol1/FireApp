package com.example.fireapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ExampleAdapter extends RecyclerView.Adapter<ExampleAdapter.ExampleViewHolder> {
    private ArrayList<ExampleItem> exampleItemList;

    public static class ExampleViewHolder extends RecyclerView.ViewHolder{
        public ImageView mViewImage;
        public TextView mTextView1;
        public TextView mTextView2;

        public ExampleViewHolder(@NonNull View itemView) {
            super(itemView);
            mViewImage = itemView.findViewById(R.id.image_view);
            mTextView1 = itemView.findViewById(R.id.text1);
            mTextView2 = itemView.findViewById(R.id.text2);
        }
    }

    public ExampleAdapter(ArrayList<ExampleItem> exampleItems){
        exampleItemList= exampleItems;
    }


    @NonNull
    @Override
    public ExampleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.example_item,parent,false);
        ExampleViewHolder evh = new ExampleViewHolder(v);
        return evh;

    }

    @Override
    public void onBindViewHolder(@NonNull ExampleViewHolder holder, int position) {
            ExampleItem currentItem = exampleItemList.get(position);
            holder.mViewImage.setImageResource(currentItem.getmImage());
            holder.mTextView1.setText(currentItem.getMtext1());
            holder.mTextView2.setText(currentItem.getMtext2());
    }

    @Override
    public int getItemCount() {
        return exampleItemList.size();
    }

    public void filterList(ArrayList<ExampleItem> filteredList){
        exampleItemList=filteredList;
        notifyDataSetChanged();
    }
}
