package com.sl.music2;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface LastFmApi {

    @GET("?method=chart.getTopArtists&format=json") // Example API endpoint for top artists
    Call<ApiResponse> getTopArtists(
            @Query("api_key") String apiKey
    );

    @GET("?method=artist.getTopTracks&format=json")
    Call<ApiResponse> getTopTracks(
            @Query("artist") String artistName,
            @Query("api_key") String apiKey
    );

}
