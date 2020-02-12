package com.example.fireapp;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ExampleAdapter extends RecyclerView.Adapter<ExampleAdapter.ExampleViewHolder> {
    private ArrayList<ExampleItem> exampleItemList;
    private ArrayList<Movie> movieLists;

    public static class ExampleViewHolder extends RecyclerView.ViewHolder{
        public ImageView mViewImage;
        public TextView mTextView1;

        public ExampleViewHolder(@NonNull View itemView) {
            super(itemView);
            mViewImage = itemView.findViewById(R.id.image_view);
            mTextView1 = itemView.findViewById(R.id.text1);
        }
    }


    public ExampleAdapter(ArrayList<Movie> movieList){
        this.movieLists = movieList;
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
            Movie currentMovie = movieLists.get(position);
            holder.mTextView1.setText(currentMovie.getMovieName());

        Log.d("koko",""+currentMovie.getPosterLink());


             Picasso.get()
                     .load(currentMovie.getPosterLink())
                     .error(R.drawable.ic_assistant)
                     .into(holder.mViewImage);

    }

    @Override
    public int getItemCount() {

       // return exampleItemList.size();
        return movieLists.size();

    }

//    public void filterList(ArrayList<ExampleItem> filteredList){
//        exampleItemList=filteredList;
//        notifyDataSetChanged();
//    }
    public void filterList(ArrayList<Movie> filteredList){
        movieLists=filteredList;
        notifyDataSetChanged();
    }
}
