package com.example.planetmovieapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.planetmovieapp.Activities.DetailedMovieActivity;
import com.example.planetmovieapp.Objects.Movie;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {
    private ArrayList<Movie> movieLists;
    private Context context;

    public MovieAdapter(Context ct,ArrayList<Movie> movieList){
        context = ct;
        this.movieLists = movieList;
    }
    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.movie_item,parent,false);
        MovieViewHolder mvh = new MovieViewHolder(v);
        return mvh;

    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {

        Movie currentMovie = movieLists.get(position);
        holder.mMovieName.setText(currentMovie.getMovieName());

         Picasso.get()
                 .load(currentMovie.getPosterLink())
                 .error(R.drawable.ic_assistant)
                 .into(holder.mViewImage);

         holder.itemCardView.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 Intent intent = new Intent(context, DetailedMovieActivity.class);
                 intent.putExtra("moviename",currentMovie.getMovieName());
                 intent.putExtra("selected.movie",currentMovie);
                 context.startActivity(intent);
             }
         });
    }

    @Override
    public int getItemCount() {
        return movieLists.size();
    }


     public class MovieViewHolder extends RecyclerView.ViewHolder{
         ImageView mViewImage;
         TextView mMovieName;
         CardView itemCardView;

        public MovieViewHolder(@NonNull View itemView) {
            super(itemView);
            mViewImage = itemView.findViewById(R.id.image_view);
            mMovieName = itemView.findViewById(R.id.movie_name_tv);
            itemCardView = itemView.findViewById(R.id.item_view);

        }
    }

    /*filters the grid view by the entered data to the search bar*/
    public void filterList(ArrayList<Movie> filteredList){
        movieLists=filteredList;
        notifyDataSetChanged();
    }
}
