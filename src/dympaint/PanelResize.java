package dympaint;

import java.awt.Dimension;
import java.awt.FlowLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class PanelResize extends JPanel {

    JTextField textFieldWidth;
    JLabel labelWidth;
    JLabel labelHeight;
    JTextField textFieldHeight;

    public PanelResize() {
        Dimension labelSize = new Dimension(70, 30);
        Dimension textFieldSize = new Dimension(100, 30);
        textFieldWidth = new JTextField();
        labelWidth = new JLabel("Width: ");
        labelHeight = new JLabel("Height: ");
        textFieldHeight = new JTextField();
        this.setSize(new Dimension(300, 220));
        labelWidth.setPreferredSize(labelSize);
        labelHeight.setPreferredSize(labelSize);
        textFieldHeight.setPreferredSize(textFieldSize);
        textFieldWidth.setPreferredSize(textFieldSize);
        setLayout(new FlowLayout(FlowLayout.CENTER, 40, 20));
        add(labelWidth);
        add(textFieldWidth);
        add(labelHeight);
        add(textFieldHeight);
    }
}
