package com.example.sixthday2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.example.sixthday2.adapter.MyRecyclerAdapter;
import com.example.sixthday2.pojo.FilmData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;


public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ProgressBar progressBar;

    private List<FilmData> moviesList;
    private Handler handler;
    private static final String jsonUrl = "https://api.androidhive.info/json/movies.json";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressBar = findViewById(R.id.progressBar2);
        progressBar.setVisibility(View.VISIBLE);

        recyclerView = findViewById(R.id.recycleView);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        recyclerView.setLayoutManager(linearLayoutManager);


        moviesList = new ArrayList<>();

        getData();


        handler = new Handler(Looper.myLooper()) {
            @Override
            public void handleMessage(@NonNull Message msg) {
                if (msg.obj == null) {
                    MyAsyncTask myAsyncTask = new MyAsyncTask();
                    myAsyncTask.execute(moviesList);
                } else {
                    progressBar.setVisibility(View.INVISIBLE);
                    recyclerView.setAdapter(new MyRecyclerAdapter(MainActivity.this, moviesList));
                }
            }
        };
    }

    public void getData() {
        new Thread() {
            @Override
            public void run() {
                String response = null;
                try {
                    URL url = new URL(jsonUrl);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    InputStream in = new BufferedInputStream(conn.getInputStream());
                    response = convertStreamToString(in);

                } catch (MalformedURLException e) {
                    Log.e("TAG", "MalformedURLException: " + e.getMessage());
                } catch (IOException e) {
                    Log.e("TAG", "IOException: " + e.getMessage());
                } catch (Exception e) {
                    Log.e("TAG", "Exception: " + e.getMessage());
                }


                JSONObject jsonObj = null;
                try {
                    JSONArray movies = new JSONArray(response);
                    for (int i = 0; i < movies.length(); i++) {
                        JSONObject c = movies.getJSONObject(i);
                        String title = c.getString("title");
                        String image = c.getString("image");
                        String rating = c.getString("rating");
                        String releaseYear = c.getString("releaseYear");

                        FilmData movie = new FilmData();

                        JSONArray arrJson = c.getJSONArray("genre");
                        List<String> genre = new ArrayList<String>();

                        for (int j = 0; j < arrJson.length(); j++) {
                            genre.add(arrJson.getString(j));
                        }

                        movie.setTitle(title);
                        movie.setImage(image);
                        movie.setRating(Float.parseFloat(rating));
                        movie.setReleaseYear(Integer.parseInt(releaseYear));
                        movie.setGenre(genre);

                        moviesList.add(movie);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.i("TAG", "Internet : List Downloaded ");
                handler.sendEmptyMessage(0);
            }
        }.start();
    }

    private String convertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line).append('\n');
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }

    public List<FilmData> downloadImages(List<FilmData> movies) {
        Bitmap bitmap = null;
        InputStream inputStream = null;
        URL url;
        HttpsURLConnection httpsURLConnection = null;
        for (int i = 0; i < movies.size(); i++) {
            try {
                url = new URL(movies.get(i).getImage());
                httpsURLConnection = (HttpsURLConnection) url.openConnection();
                httpsURLConnection.connect();

                if (httpsURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    inputStream = httpsURLConnection.getInputStream();
                    bitmap = BitmapFactory.decodeStream(inputStream);
                    movies.get(i).setBitmap(bitmap);
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
        }
        return movies;
    }

    public class MyAsyncTask extends AsyncTask<List<FilmData>, Void, List<FilmData>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected List<FilmData> doInBackground(List<FilmData>... lists) {
            List<FilmData> movies = downloadImages(lists[0]);

            return movies;
        }

        @Override
        protected void onPostExecute(List<FilmData> movies) {
            super.onPostExecute(movies);

            Message msg = new Message();
            msg.obj = movies;
            handler.sendMessage(msg);
        }
    }
}