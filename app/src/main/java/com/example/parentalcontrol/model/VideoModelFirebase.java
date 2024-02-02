package com.example.parentalcontrol.model;

import android.net.Uri;

public class VideoModelFirebase {
    private String id;
    private String title;
    private String folderName;
    private String size;
    private String path;
    private String uri;

    public VideoModelFirebase(String id, String title, String folderName, String size, String path, String uri) {
        this.id = id;
        this.title = title;
        this.folderName = folderName;
        this.size = size;
        this.path = path;
        this.uri = uri;
    }

    // Getter methods
    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getFolderName() {
        return folderName;
    }

    public String getSize() {
        return size;
    }

    public String getPath() {
        return path;
    }

    public String getUri() {
        return uri;
    }
}
