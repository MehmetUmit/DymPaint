package dympaint;

import java.awt.Color;
import java.awt.Dimension;
import javax.swing.JButton;
import javax.swing.JColorChooser;

public class Button extends JButton {

    JColorChooser colorChooser;
    static final int TOOL_BUTTON = 0;
    static final int COLOR_BUTTON_MAIN = 1;
    static final int COLOR_BUTTON_SIDE = 2;
    public int type;

    public Button(String s, int type) {
        this.type = type;
        setFocusable(false);
        setPreferredSize(new Dimension(150, 30));
        if (type == TOOL_BUTTON) {
            setText(s);
            setBackground(Color.black);
            setForeground(Color.white);
            setFont(GUI.font);
        } else if (type == COLOR_BUTTON_MAIN) {
            colorChooser = new JColorChooser();
            setBackground(Color.BLACK);
            setPreferredSize(new Dimension(50, 50));

        } else if( type == COLOR_BUTTON_SIDE){
            setPreferredSize(new Dimension(30,30));
        }
    }
}
