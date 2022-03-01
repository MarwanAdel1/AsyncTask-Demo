package com.example.sixthday2.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sixthday2.R;
import com.example.sixthday2.pojo.FilmData;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

public class MyRecyclerAdapter extends RecyclerView.Adapter<MyRecyclerAdapter.ViewHolder> {
    private Handler handler;
    private Context context;
    private List<FilmData> moviesList;


    public MyRecyclerAdapter(Context context, List<FilmData> moviesList) {
        this.context = context;
        this.moviesList = moviesList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.list_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.titleTx.setText(moviesList.get(position).getTitle());
        holder.yearTx.setText(String.valueOf(moviesList.get(position).getReleaseYear()));
        holder.ratingBar.setRating(moviesList.get(position).getRating());
        StringBuilder stringBuilder = new StringBuilder("");
        for (int i = 0; i < moviesList.get(position).getGenre().size(); i++) {
            stringBuilder.append(moviesList.get(position).getGenre().get(i) + "\n");
        }
        holder.genreTx.setText(stringBuilder);

        holder.progressBar.setVisibility(View.VISIBLE);

        holder.imageView.setImageBitmap(moviesList.get(position).getBitmap());

        holder.progressBar.setVisibility(View.INVISIBLE);

        //holder.imageView.setImageBitmap(android.R.color.transparent);
        //setImageView(moviesList.get(position).getImage());
     /*   handler = new Handler(Looper.myLooper()) {
            @Override
            public void handleMessage(@NonNull Message msg) {
                Bitmap bitmap= (Bitmap) msg.obj;

                holder.imageView.setImageBitmap(bitmap);

                holder.progressBar.setVisibility(View.INVISIBLE);

            }
        };*/
    }

    @Override
    public int getItemCount() {
        return moviesList.size();
    }

    /*public void setImageView(String url) {
        MyAsyncTask myAsync = new MyAsyncTask();
        myAsync.execute(url);
    }*/


    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public TextView titleTx, yearTx, genreTx;
        public RatingBar ratingBar;
        public ProgressBar progressBar;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.imageView);
            titleTx = itemView.findViewById(R.id.titleTx);
            yearTx = itemView.findViewById(R.id.releaseYearTx);
            ratingBar = itemView.findViewById(R.id.ratingBar);
            ratingBar.setNumStars(10);
            progressBar = itemView.findViewById(R.id.progressBar);
            genreTx = itemView.findViewById(R.id.genreTx);

        }
    }

/*
    public class MyAsyncTask extends AsyncTask<String, Void, Bitmap> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Bitmap doInBackground(String... strings) {
            Bitmap bitmap = downloadImage(strings[0]);

            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);

            Message msg= new Message();
            msg.obj=bitmap;
            handler.sendMessage(msg);
        }


        public Bitmap downloadImage(String imageUrl) {
            Bitmap bitmap = null;
            InputStream inputStream = null;
            URL url;
            HttpsURLConnection httpsURLConnection = null;

            try {
                url = new URL(imageUrl);
                httpsURLConnection = (HttpsURLConnection) url.openConnection();
                httpsURLConnection.connect();

                if (httpsURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    inputStream = httpsURLConnection.getInputStream();
                    bitmap = BitmapFactory.decodeStream(inputStream);
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return bitmap;
        }

    }*/

}
