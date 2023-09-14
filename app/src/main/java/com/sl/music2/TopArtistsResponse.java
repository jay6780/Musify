package com.sl.music2;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TopArtistsResponse {

    @SerializedName("artist") // Adjust the field name based on the actual response structure
    private List<Artist> artists;

    // Define getter method for artists

    public List<Artist> getArtists() {
        return artists;
    }
}
