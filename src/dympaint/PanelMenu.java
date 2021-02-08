package dympaint;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class PanelMenu extends JPanel {

    Button btnRectangle, btnBrush, btnCircle, btnMainColor, btnTemp, btnUndo, btnRedo, btnFill, btnEraser, btnLine, btnColorPicker, btnZoomIn, btnZoomOut;
    Button[] sideColorButtons = new Button[8];
    int[] sideColors = {0xff0000, 0x0000ff, 0x00ff00, 0x00ffff, 0xff9900, 0x000000, 0xffff00, 0x996633};
    int zoomDefaultValue = 20;
    Slider strokeSlider, zoomSlider;
    JPanel sideColorsPanel = new JPanel();
    JLabel zoomVal;

    public PanelMenu() {
        btnRectangle = new Button("Rectangle", Button.TOOL_BUTTON);
        btnBrush = new Button("Brush", Button.TOOL_BUTTON);
        btnCircle = new Button("Circle", Button.TOOL_BUTTON);
        btnMainColor = new Button(null, Button.COLOR_BUTTON_MAIN);
        btnUndo = new Button("Undo", Button.TOOL_BUTTON);
        strokeSlider = new Slider(7, 30, 5, true);
        zoomSlider = new Slider(zoomDefaultValue, 100, 20, true);
        btnRedo = new Button("Redo", Button.TOOL_BUTTON);
        btnFill = new Button("Fill", Button.TOOL_BUTTON);
        btnEraser = new Button("Eraser", Button.TOOL_BUTTON);
        btnLine = new Button("Line", Button.TOOL_BUTTON);
        btnColorPicker = new Button("Color Picker", Button.TOOL_BUTTON);
        zoomVal = new JLabel("%100");
        btnZoomIn = new Button("+", Button.ZOOM_BUTTON_IN);
        btnZoomOut = new Button("-", Button.ZOOM_BUTTON_OUT);
        btnTemp = btnBrush;
        btnBrush.setEnabled(false);
        setPreferredSize(new Dimension(200, 0));
        setBackground(new Color(0x123456));
        setLayout(new FlowLayout(FlowLayout.CENTER, 15, 15));

        sideColorsPanel.setBackground(this.getBackground());
        sideColorsPanel.setPreferredSize(new Dimension(200, 100));
        sideColorsPanel.setLayout(this.getLayout());
        
        zoomVal.setPreferredSize(new Dimension(150,20));
        zoomVal.setFont(GUI.font);
        zoomVal.setForeground(Color.white);
        zoomVal.setHorizontalAlignment(JLabel.CENTER);
        add(btnBrush);
        add(btnRectangle);
        add(btnCircle);
        add(btnLine);
        add(strokeSlider);
        add(btnMainColor, BorderLayout.CENTER);
        //Adding sideColorButtons to their own panel which exist in PanelMenu
        for (int i = 0; i < sideColorButtons.length; i++) {
            sideColorButtons[i] = new Button(null, Button.COLOR_BUTTON_SIDE);
            sideColorButtons[i].setBackground(new Color(sideColors[i]));
            sideColorsPanel.add(sideColorButtons[i]);
        }
        add(sideColorsPanel);
        add(btnFill);
        add(btnColorPicker);
        add(btnEraser);
        add(btnUndo);
        add(btnRedo);
        add(zoomSlider);
        add(zoomVal);
        add(btnZoomIn);
        add(btnZoomOut);

    }
}
