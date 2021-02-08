package dympaint;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Insets;
import javax.swing.JButton;
import javax.swing.JColorChooser;

public class Button extends JButton {

    JColorChooser colorChooser;
    static final int TOOL_BUTTON = 0;
    static final int COLOR_BUTTON_MAIN = 1;
    static final int COLOR_BUTTON_SIDE = 2;
    static final int ZOOM_BUTTON_IN = 3;
    static final int ZOOM_BUTTON_OUT = 4;
    static final int RESIZE_BUTTON = 5;
    public int type;

    public Button(String s, int type) {
        this.type = type;
        setFocusable(false);
        setBorderPainted(false);
        setFocusPainted(false);
        if (type == TOOL_BUTTON) {
            setPreferredSize(new Dimension(150, 30));
            setText(s);
            setBackground(Color.black);
            setForeground(Color.white);
            setFont(GUI.font);
        } else if (type == COLOR_BUTTON_MAIN) {
            colorChooser = new JColorChooser();
            setBackground(Color.BLACK);
            setPreferredSize(new Dimension(50, 50));

        } else if (type == COLOR_BUTTON_SIDE) {
            setPreferredSize(new Dimension(30, 30));
        } else if (type == ZOOM_BUTTON_IN || type == ZOOM_BUTTON_OUT) {
            setPreferredSize(new Dimension(40, 40));
            setText(s);
            setBackground(Color.black);
            setForeground(Color.white);
            setFont(GUI.font);
        } else if (type == RESIZE_BUTTON) {
            setPreferredSize(new Dimension(50, 40));
            setBackground(new Color(0x123456));
            setForeground(Color.white);
            setText(s);
        }
        //For fit the text properly to the button
        setMargin(new Insets(0, 0, 0, 0));
    }
}
