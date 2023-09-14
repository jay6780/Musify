package com.sl.music2;

import com.google.gson.annotations.SerializedName;

public class TrackArtist {

    @SerializedName("name") // Adjust the field name based on the actual response structure
    private String name;

    // Define getter method for name

    public String getName() {
        return name;
    }
}
