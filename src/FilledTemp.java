package dympaint;

import java.awt.Color;

public class FilledTemp {

    public int startIndex, endIndex;
    public Color color;
    public static final int CANVAS_INDEX = -1;

    public FilledTemp(int startIndex, int endIndex, Color color) {
        this.startIndex = startIndex;
        this.endIndex = endIndex;
        this.color = color;
    }
}
