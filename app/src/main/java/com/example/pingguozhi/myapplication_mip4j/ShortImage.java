package com.example.pingguozhi.myapplication_mip4j;
import org.apache.commons.lang.ArrayUtils;
public class ShortImage extends Image{
    protected short[] pixelArray;
    protected short max = Short.MIN_VALUE;
    protected short min = Short.MAX_VALUE;
    protected int windowCenter = Integer.MIN_VALUE;
    protected int windowWidth = Integer.MIN_VALUE;
    protected int rescaleIntercept = 0;

    protected ShortImage() {
        this.pixelArray = new short[1];
    }

    protected ShortImage(int w, int h) {
        this.width = w;
        this.height = h;
        this.pixelArray = new short[w * h];
    }

    public ShortImage(int w, int h, short[] pixels) {
        this.width = w;
        this.height = h;
        this.pixelArray = pixels;
    }

    protected void setPixel(int x, int y, int v) {
        this.pixelArray[(y * this.width) + x] = (short) v;

        if (v > this.max)
            this.max = (short) v;
        if (v < this.min)
            this.min = (short) v;
    }

    public short getPixel(int x, int y) {
        if (x >= this.width || x < 0 || y >= this.height || y < 0)
            return Short.MIN_VALUE;

        return this.pixelArray[(y * this.width) + x];
    }

    public short[] getPixelArray() {
        return this.pixelArray;
    }

    public short getMax() {
        if (this.max == Short.MIN_VALUE) {
            for (short v : this.pixelArray)
                if (v > this.max)
                    this.max = v;
        }
        return this.max;
    }

    public short getMin() {
        if (this.min == Short.MAX_VALUE) {
            for (short v : this.pixelArray)
                if (v < this.min)
                    this.min = v;
        }
        return this.min;
    }

    public int getWindowCenter() {
        if (this.windowCenter == Integer.MIN_VALUE)
            this.windowCenter = (this.max - this.min) / 2 + this.min;
        return this.windowCenter;
    }

    public int getWindowWidth() {
        if (this.windowWidth == Integer.MIN_VALUE)
            this.windowWidth = this.max - this.min;
        return this.windowWidth;
    }

    @Override
    public String toString() {
        String toString = super.toString();

        toString += (String.format("%30s", "WindowCenter ") + this.windowCenter + "\n");
        toString += (String.format("%30s", "WindowWidth ") + this.windowWidth + "\n");

        return toString;
    }

    protected void thresholding(int t) {
        for (int y = 0; y < this.height; y++)
            for (int x = 0; x < this.width; x++)
                this.pixelArray[(y * this.width) + x] = this.pixelArray[(y * this.width) + x] > t ? this.pixelArray[(y * this.width) + x] : 0;

    }

    protected void flipVertically() {
        ArrayUtils.reverse(this.pixelArray);

        for (int y = 0; y < this.height; y++) {
            short[] sub = ArrayUtils.subarray(this.pixelArray, (y * this.width), ((y + 1) * this.width));
            ArrayUtils.reverse(sub);

            for (int x = 0; x < this.width; x++)
                this.pixelArray[(y * this.width) + x] = sub[x];
        }
    }
}
