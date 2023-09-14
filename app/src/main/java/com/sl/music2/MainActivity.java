package com.sl.music2;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private RecyclerView musicRecycler;
    private CombinedAdapter combinedAdapter;
    private LastFmApi lastFmApi;
    private EditText artistNameInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        musicRecycler = findViewById(R.id.music_recycler);
        musicRecycler.setLayoutManager(new LinearLayoutManager(this));

        artistNameInput = findViewById(R.id.artist_name_input); // Initialize the EditText
        ImageButton fetchTracksButton = findViewById(R.id.fetch_button); // Initialize the Button

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://ws.audioscrobbler.com/2.0/") // Last.fm API base URL
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        lastFmApi = retrofit.create(LastFmApi.class);

        fetchTracksButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fetchTopTracks(); // Call the method to fetch tracks when the button is clicked
            }
        });
    }

    private void fetchTopTracks() {
        String artistName = artistNameInput.getText().toString().trim();
        if (artistName.isEmpty()) {
            Toast.makeText(MainActivity.this, "Please enter an artist's name.", Toast.LENGTH_SHORT).show();
            return;
        }

        String apiKey = "90aaa3a08d33f161172a020e1d53ae13";

        Call<ApiResponse> call = lastFmApi.getTopTracks(artistName, apiKey);
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful()) {
                    ApiResponse apiResponse = response.body();
                    if (apiResponse != null && apiResponse.getTopTracks() != null && apiResponse.getTopTracks().getTracks() != null) {
                        List<Track> topTracks = apiResponse.getTopTracks().getTracks();

                        List<CombinedItem> combinedItems = new ArrayList<>();
                        for (Track track : topTracks) {
                            combinedItems.add(new CombinedItem(track));
                        }

                        combinedAdapter = new CombinedAdapter(combinedItems, new CombinedAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(CombinedItem item) {
                                String musicUrl;
                                if (item.isArtist()) {
                                    musicUrl = item.getArtist().getArtistUrl();
                                } else {
                                    musicUrl = item.getTrack().getTrackUrl();
                                }

                                // Launch WebView activity with the clicked item's music URL
                                Intent intent = new Intent(MainActivity.this, WebViewActivity.class);
                                intent.putExtra(WebViewActivity.EXTRA_URL, musicUrl);
                                overridePendingTransition(0, 0); // Disable animation
                                startActivity(intent);
                            }
                        });

                        musicRecycler.setAdapter(combinedAdapter);
                    } else {
                        Toast.makeText(MainActivity.this, "Please enter music Artist name only.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(MainActivity.this, "Failed to fetch data.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Network request failed.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}