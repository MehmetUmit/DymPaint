package dympaint;

import java.awt.Color;
import java.awt.Dimension;
import javax.swing.JSlider;

public class Slider extends JSlider {

    public Slider(int val, int max, int ticksSpacing, boolean ticks) {
        setPreferredSize(new Dimension(150, 30));
        this.setBackground(new Color(0x123456));
        this.setMaximum(max);
        this.setValue(val);
        this.setMajorTickSpacing(ticksSpacing);
        this.setPaintTicks(ticks);
        this.setPaintLabels(false);
        this.setFocusable(false);
    }
}
