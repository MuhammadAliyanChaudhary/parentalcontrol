package com.example.parentalcontrol.model;

import android.net.Uri;

public class ImageModelFirebase {
    private String id;
    private String title;
    private String folderName;
    private String size;
    private String path;
    private String uri;

    public ImageModelFirebase() {
    }

    public ImageModelFirebase(String id, String title, String folderName, String size, String path, String uri) {
        this.id = id;
        this.title = title;
        this.folderName = folderName;
        this.size = size;
        this.path = path;
        this.uri = uri;
    }

    // Getters and setters for the fields
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

    public String getFolderName() {
        return folderName;
    }

    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }
}
