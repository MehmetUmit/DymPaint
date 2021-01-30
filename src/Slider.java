package dympaint;

import java.awt.Color;
import java.awt.Dimension;
import javax.swing.JSlider;

public class Slider extends JSlider {

    public Slider() {
        this.setBackground(Color.yellow);
        setPreferredSize(new Dimension(150, 30));
        this.setBackground(new Color(0x123456));
        this.setMaximum(30);
        this.setValue(7);
        this.setMajorTickSpacing(5);
        this.setPaintTicks(true);
    }
}
