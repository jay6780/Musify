package com.sl.music2;
import com.google.gson.annotations.SerializedName;

public class ArtistImage {

    @SerializedName("#text") // Adjust the field name based on the actual response structure
    private String url;

    @SerializedName("size") // Adjust the field name based on the actual response structure
    private String size;

    // Define getter methods for url and size

    public String getUrl() {
        return url;
    }

    public String getSize() {
        return size;
    }
}
