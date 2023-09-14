package com.sl.music2;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Track {
    @SerializedName("url") // Add the field for track URL
    private String trackUrl;

    @SerializedName("name")
    private String name;

    @SerializedName("artist")
    private TrackArtist artist;

    @SerializedName("image") // Adjust the field name based on the actual response structure
    private List<Image> images;

    @SerializedName("mbid") // Add the field for track ID
    private String trackId;

    // ... Other fields and methods

    public String getName() {
        return name;
    }

    public TrackArtist getArtist() {
        return artist;
    }

    public String getImageUrl() {
        if (images != null && !images.isEmpty()) {
            // Assuming you want to get the largest available image size
            Image image = images.get(images.size() - 1);
            return image.getUrl();
        }
        return null;
    }

    public String getTrackUrl() {
        return trackUrl;
    }

    public String getTrackId() {
        return trackId;
    }
}
