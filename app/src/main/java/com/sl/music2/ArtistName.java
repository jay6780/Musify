package com.sl.music2;
import java.util.ArrayList;
import java.util.List;


public class ArtistName {
    private List<String> artistNames;

    public ArtistName() {
        artistNames = new ArrayList<>();
        artistNames.add("wave to earth");
        artistNames.add("Niki");
        artistNames.add("Bruno Mars");
        artistNames.add("Jeff Bernat");
    }

    public List<String> getArtists() {
        return artistNames;
    }
}
