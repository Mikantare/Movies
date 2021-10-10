package com.bespalov.movies.activitys;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bespalov.movies.R;
import com.bespalov.movies.data.MovieAdapter;
import com.bespalov.movies.model.Movie;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private FloatingActionButton floatingActionButton;
    private EditText editTextName;
    private MovieAdapter movieAdapter;
    private ArrayList<Movie> movies;

    private RequestQueue requestQueue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerViewMovies);
        floatingActionButton = findViewById(R.id.floatingActionButton);
        editTextName = findViewById(R.id.editTextName);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.hasFixedSize();
        movies = new ArrayList<>();
        requestQueue = Volley.newRequestQueue(this);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String searchMovie = editTextName.getText().toString().trim();
                if (!searchMovie.equals("")) {
                    getMovies(searchMovie);
                } else {
                    Toast.makeText(MainActivity.this, "Заполните строку поиска", Toast.LENGTH_LONG).show();
                }
            }
        });


    }

    private void getMovies(String searchMovie) {
        String baseUrl = "https://www.omdbapi.com/?apikey=160efbe9";
        String url = baseUrl + "&s=" + searchMovie;
        movies = new ArrayList<>();

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("Search");

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String titlte = jsonObject.getString("Title");
                        String year = jsonObject.getString("Year");
                        String posterUrl = jsonObject.getString("Poster");
                        Movie movie = new Movie();
                        movie.setTitle(titlte);
                        movie.setYear(year);
                        movie.setPosterUrl(posterUrl);

                        movies.add(movie);
                    }

                    movieAdapter = new MovieAdapter(MainActivity.this, movies);
                    recyclerView.setAdapter(movieAdapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        requestQueue.add(request);

    }

}