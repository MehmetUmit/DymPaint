package dympaint;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import javax.swing.JPanel;

public class PanelMenu extends JPanel {

    Button btnRectangle, btnBrush, btnCircle, btnMainColor, btnTemp, btnUndo, btnRedo, btnFill, btnEraser, btnLine, btnColorPicker;
    Button[] sideColorButtons = new Button[8];
    int[] sideColors = {0xff0000, 0x0000ff, 0x00ff00, 0x00ffff, 0xff9900, 0x000000, 0xffff00, 0x996633};
    Slider slider;
    JPanel sideColorsPanel = new JPanel();

    public PanelMenu() {
        btnRectangle = new Button("Rectangle", Button.TOOL_BUTTON);
        btnBrush = new Button("Brush", Button.TOOL_BUTTON);
        btnCircle = new Button("Circle", Button.TOOL_BUTTON);
        btnMainColor = new Button(null, Button.COLOR_BUTTON_MAIN);
        btnUndo = new Button("Undo", Button.TOOL_BUTTON);
        slider = new Slider();
        btnRedo = new Button("Redo", Button.TOOL_BUTTON);
        btnFill = new Button("Fill", Button.TOOL_BUTTON);
        btnEraser = new Button("Eraser", Button.TOOL_BUTTON);
        btnLine = new Button("Line", Button.TOOL_BUTTON);
        btnColorPicker = new Button("Color Picker", Button.TOOL_BUTTON);
        btnTemp = btnBrush;
        btnBrush.setEnabled(false);

        setPreferredSize(new Dimension(200, 0));
        setBackground(new Color(0x123456));
        setLayout(new FlowLayout(FlowLayout.CENTER, 15, 15));

        sideColorsPanel.setBackground(this.getBackground());
        sideColorsPanel.setPreferredSize(new Dimension(200, 100));
        sideColorsPanel.setLayout(this.getLayout());
        add(btnBrush);
        add(btnRectangle);
        add(btnCircle);
        add(btnLine);
        add(slider);
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
    }
}
