package dympaint;

import java.awt.Color;

public class Shape {

    static final int RECTANGLE = 0;
    static final int BRUSH = 1;
    static final int CIRCLE = 2;
    static final int LINE = 3;
    static final int ERASER = 4;
    static final int COLOR_PICKER = 5;
    static final int FILL = 6;
    static final int NULL = -1;
    public Color color;
    public float thickness;
    public int type, x, y, width, height;
    private boolean endOfShape = false;

    public Shape(int x, int y, Color color, float thickness, int type) {
        this.x = x;
        this.y = y;
        this.color = color;
        this.thickness = thickness;
        this.type = type;

    }

    public Shape(int x, int y, int width, int height, Color color, float thickness, int type) {
        this.x = x;
        this.y = y;
        this.color = color;
        this.thickness = thickness;
        this.type = type;
        this.height = height;
        this.width = width;

    }

    public boolean isEndOfShape() {
        return endOfShape;
    }

    public void setEndOfShape() {
        endOfShape = true;
    }
}
