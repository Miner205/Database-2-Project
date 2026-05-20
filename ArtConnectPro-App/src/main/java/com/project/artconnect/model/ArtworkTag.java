package com.project.artconnect.model;

public class ArtworkTag {
    private int artworkTagId;
    private String name;

    public ArtworkTag() {
    }

    public ArtworkTag(String name) {
        this.name = name;
    }

    public int getArtworkTagId() {
        return artworkTagId;
    }

    public void setArtworkTagId(int artworkTagId) {
        this.artworkTagId = artworkTagId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
