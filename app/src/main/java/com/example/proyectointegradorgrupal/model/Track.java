package com.example.proyectointegradorgrupal.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Track implements Serializable {

    @SerializedName("data")
    private ArrayList<Track> data;
    private String id;
    private String title;
    private Double duration;
    private Double trackPosition;
    private String preview;

    @SerializedName("album")
    private Album album;

    public ArrayList<Track> getData() {
        return data;
    }

    public void setTrackList(ArrayList<Track> data) {
        this.data = data;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Double getDuration() {
        return duration;
    }

    public void setDuration(Double duration) {
        this.duration = duration;
    }

    public Double getTrackPosition() {
        return trackPosition;
    }

    public void setTrackPosition(Double trackPosition) {
        this.trackPosition = trackPosition;
    }

    public String getPreview() {
        return preview;
    }

    public void setPreview(String preview) {
        this.preview = preview;
    }

    public void setData(ArrayList<Track> data) {
        this.data = data;
    }

    public Album getAlbum() {
        return album;
    }

    public void setAlbum(Album album) {
        this.album = album;
    }

    public Track(ArrayList<Track> data, String id, String title, Double duration, Double trackPosition, String preview, Album album) {
        this.data = data;
        this.id = id;
        this.title = title;
        this.duration = duration;
        this.trackPosition = trackPosition;
        this.preview = preview;
        this.album = album;
    }

    public Track() {
    }
}
