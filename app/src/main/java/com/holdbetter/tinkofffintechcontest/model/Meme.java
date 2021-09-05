package com.holdbetter.tinkofffintechcontest.model;

import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Meme implements Cloneable {
    @SerializedName("id")
    public int id;
    @SerializedName("description")
    public String description;
    @SerializedName("gifURL")
    public String url;
    @SerializedName("previewURL")
    public String previewUrl;
    @SerializedName("type")
    public String type;
    @SerializedName("width")
    public int width;
    @SerializedName("height")
    public int height;
    @Expose
    public boolean isCache = false;

    @NonNull
    @Override
    public String toString() {
        return "Meme{" +
                "id=" + id +
                ", description='" + description + '\'' +
                ", url='" + url + '\'' +
                ", previewUrl='" + previewUrl + '\'' +
                ", type='" + type + '\'' +
                ", width=" + width +
                ", height=" + height +
                '}';
    }

    @NonNull
    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
