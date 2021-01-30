package dympaint;

import java.awt.Color;

public interface ColorChooserListener {

    public static final boolean PRESSED = true;
    public static final boolean MOVED = false;

    public void colorPicked(Color c, boolean type);
}
