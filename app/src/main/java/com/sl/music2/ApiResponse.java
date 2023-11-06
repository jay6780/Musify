package com.sl.music2;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ApiResponse {

    @SerializedName("name") // Adjust the field name based on the actual response structure
    private TopArtistsResponse name;

    @SerializedName("toptracks") // Adjust the field name based on the actual response structure
    private TopTracksResponse topTracks;

    // Define getter methods for topArtists and topTracks

    public TopArtistsResponse getTopArtists() {
        return name;
    }

    public TopTracksResponse getTopTracks() {
        return topTracks;
    }

    @SerializedName("artist") // Adjust the field name based on the actual response structure
    private List<Artist> artists;

    // Define getter method for artists

    public List<Artist> getArtists() {
        return artists;
    }
}
