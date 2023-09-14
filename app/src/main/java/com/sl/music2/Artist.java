package com.sl.music2;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Artist {

    @SerializedName("name")
    private String name;

    @SerializedName("image")
    private List<ArtistImage> images;

    @SerializedName("url") // Add the field for artist URL
    private String artistUrl;

    @SerializedName("mbid") // Add the field for artist ID
    private String artistId;

    public String getName() {
        return name;
    }

    public List<ArtistImage> getImages() {
        return images;
    }

    public String getImageUrl() {
        if (images != null && !images.isEmpty()) {
            // You can choose a desired size (e.g., "medium" or "large")
            for (ArtistImage image : images) {
                if ("medium".equals(image.getSize())) {
                    return image.getUrl();
                }
            }
            // If "medium" size is not available, return the first available image URL
            return images.get(0).getUrl();
        }
        return null;
    }

    public String getArtistUrl() {
        return artistUrl;
    }

    public String getArtistId() {
        return artistId;
    }
}
