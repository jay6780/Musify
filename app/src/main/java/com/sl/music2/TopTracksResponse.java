package com.sl.music2;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TopTracksResponse {

    @SerializedName("track") // Adjust the field name based on the actual response structure
    private List<Track> tracks;

    // Define getter method for tracks

    public List<Track> getTracks() {
        return tracks;
    }
}
