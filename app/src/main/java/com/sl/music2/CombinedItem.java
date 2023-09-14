package com.sl.music2;
public class CombinedItem {

    private Artist artist;
    private Track track;
    private boolean isArtist;

    public CombinedItem(Artist artist) {
        this.artist = artist;
        this.isArtist = true;
    }

    public CombinedItem(Track track) {
        this.track = track;
        this.isArtist = false;
    }

    public Artist getArtist() {
        return artist;
    }

    public Track getTrack() {
        return track;
    }

    public boolean isArtist() {
        return isArtist;
    }

    public String getImageUrl() {
        if (isArtist) {
            return artist.getImageUrl();
        } else {
            return track.getImageUrl();
        }
    }
}
