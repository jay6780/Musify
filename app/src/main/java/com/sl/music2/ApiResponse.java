package com.sl.music2;
import com.google.gson.annotations.SerializedName;

public class ApiResponse {

    @SerializedName("topartists") // Adjust the field name based on the actual response structure
    private TopArtistsResponse topArtists;

    @SerializedName("toptracks") // Adjust the field name based on the actual response structure
    private TopTracksResponse topTracks;

    // Define getter methods for topArtists and topTracks

    public TopArtistsResponse getTopArtists() {
        return topArtists;
    }

    public TopTracksResponse getTopTracks() {
        return topTracks;
    }
}
