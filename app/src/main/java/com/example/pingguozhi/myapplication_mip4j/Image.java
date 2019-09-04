package com.example.pingguozhi.myapplication_mip4j;

public class Image {
    protected int height;
    protected int width;

    public Image(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public Image() {
        this.width = 1;
        this.height = 1;
    }

    public int getHeight() {
        return this.height;
    }

    public int getWidth() {
        return this.width;
    }

    protected void setHeight(int height) {
        this.height = height;
    }

    protected void setWidth(int width) {
        this.width = width;
    }

    @Override
    public String toString() {
        String toString = "";

        toString += (String.format("%30s", "Width ") + this.width + "\n");
        toString += (String.format("%30s", "Height ") + this.height + "\n");

        return toString;
    }
}

