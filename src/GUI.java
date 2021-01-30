package dympaint;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
//import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class GUI extends JFrame implements ActionListener, ChangeListener, ColorChooserListener {

    static Font font = new Font("Comic Sans", Font.BOLD, 20);
    Dimension screenDimension;
    //private ImageIcon imageIcon = new ImageIcon("icon.png");
    public Color color;
    PanelMenu panelMenu;
    Canvas canvas;

    public GUI() {
        screenDimension = Toolkit.getDefaultToolkit().getScreenSize();
        setTitle("DymPaint");
        panelMenu = new PanelMenu();
        canvas = new Canvas();
        canvas.shapeThickness = panelMenu.slider.getValue();
        setSize(800, 700);

        this.setLocation((int) screenDimension.getWidth() / 2 - getSize().width / 2, (int) screenDimension.getHeight() / 2 - getSize().height / 2);
        add(panelMenu, BorderLayout.EAST);
        add(canvas);

        panelMenu.btnMainColor.addActionListener(this);
        panelMenu.btnCircle.addActionListener(this);
        panelMenu.btnBrush.addActionListener(this);
        panelMenu.btnRectangle.addActionListener(this);
        panelMenu.btnUndo.addActionListener(this);
        panelMenu.slider.addChangeListener(this);
        panelMenu.btnFill.addActionListener(this);
        panelMenu.btnRedo.addActionListener(this);
        panelMenu.btnEraser.addActionListener(this);
        panelMenu.btnLine.addActionListener(this);
        panelMenu.btnColorPicker.addActionListener(this);
        canvas.addColorChooserListener(this);
        for (Button sideColorButton : panelMenu.sideColorButtons) {
            sideColorButton.addActionListener(this);
        }
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public Color returnToPreviousColor(FilledTemp filledTemp) {
        Color previousColor;
        int endOfCount = 0;

        if (filledTemp.startIndex == FilledTemp.CANVAS_INDEX) {
            previousColor = canvas.canvasColor;
            canvas.canvasColor = filledTemp.color;
            canvas.setBackground(filledTemp.color);
            canvas.changeEraserColor();

            return previousColor;
        }
        previousColor = canvas.shapes.get(filledTemp.endIndex).color;
        for (int i = filledTemp.endIndex; i >= filledTemp.startIndex; i--) {
            if (canvas.shapes.get(i).isEndOfShape()) {
                endOfCount++;
            }
            if (endOfCount == 2) {
                return previousColor;
            }
            canvas.shapes.get(i).color = filledTemp.color;

        }
        return previousColor;
    }

    @Override
    public void colorPicked(Color c, boolean t) {
        if (t == ColorChooserListener.PRESSED) {
            canvas.shapeColor = c;
            panelMenu.btnTemp.setEnabled(true);
            canvas.shapeType = Shape.BRUSH;
            panelMenu.btnBrush.setEnabled(false);
            panelMenu.btnTemp = panelMenu.btnBrush;
        }
        panelMenu.btnMainColor.setBackground(c);

    }

    @Override
    public void stateChanged(ChangeEvent ce) {
        canvas.shapeThickness = panelMenu.slider.getValue();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object eventSource = e.getSource();
        Button btn;
        if(eventSource instanceof Button){
            btn = (Button)eventSource;
        }else{
            return;
        }
        if (btn == panelMenu.btnMainColor) {
            color = panelMenu.btnMainColor.colorChooser.showDialog(this, "Color", panelMenu.btnMainColor.getBackground());
            panelMenu.btnMainColor.setBackground(color);
            canvas.shapeColor = color;
        } else if (btn.type == Button.COLOR_BUTTON_SIDE) {
            color = btn.getBackground();
            panelMenu.btnMainColor.setBackground(color);
            canvas.shapeColor = color;
        } else if (btn == panelMenu.btnUndo) {
            undo();
        } else if (btn == panelMenu.btnRedo) {
            redo();
        } else{
            panelMenu.btnTemp.setEnabled(true);
            panelMenu.btnTemp = (Button) eventSource;
            panelMenu.btnTemp.setEnabled(false);
            if (eventSource == panelMenu.btnRectangle) {
                canvas.shapeType = Shape.RECTANGLE;

            } else if (eventSource == panelMenu.btnBrush) {
                canvas.shapeType = Shape.BRUSH;

            } else if (eventSource == panelMenu.btnCircle) {
                canvas.shapeType = Shape.CIRCLE;

            } else if (eventSource == panelMenu.btnLine) {
                canvas.shapeType = Shape.LINE;

            } else if (eventSource == panelMenu.btnFill) {
                canvas.shapeType = Shape.FILL;

            } else if (eventSource == panelMenu.btnEraser) {
                canvas.shapeType = Shape.ERASER;

            } else if (eventSource == panelMenu.btnColorPicker) {
                canvas.shapeType = Shape.COLOR_PICKER;

            }
        }
    }

    private void undo() {
        if (!canvas.filledTempDelay.isEmpty() && !canvas.filledTemps.isEmpty() && !canvas.filledTempDelay.get(canvas.filledTempDelay.size() - 1)) {
            canvas.filledTempDelayRedo.add(canvas.filledTempDelay.get(canvas.filledTempDelay.size() - 1));
            canvas.filledTempsRedo.add(canvas.filledTemps.get(canvas.filledTemps.size() - 1));

            canvas.filledTempsRedo.get(canvas.filledTempsRedo.size() - 1).color = returnToPreviousColor(canvas.filledTemps.get(canvas.filledTemps.size() - 1));

            canvas.filledTempDelay.remove(canvas.filledTempDelay.size() - 1);
            canvas.filledTemps.remove(canvas.filledTemps.size() - 1);

            canvas.repaint();
            return;
        }

        if (!canvas.filledTempDelay.isEmpty() && canvas.filledTempDelay.get(canvas.filledTempDelay.size() - 1)) {
            canvas.filledTempDelayRedo.add(canvas.filledTempDelay.get(canvas.filledTempDelay.size() - 1));
            canvas.filledTempDelay.remove(canvas.filledTempDelay.size() - 1);
        }
        for (int endOfCount = 0, i = canvas.shapes.size() - 1; i >= 0; i--) {
            if (canvas.shapes.get(i).isEndOfShape()) {
                endOfCount++;
            }
            //We have to add shapes to shapesRedo when endOfCount equals 2 too
            canvas.shapesRedo.add(canvas.shapes.get(i));
            if (endOfCount == 2) {
                break;
            }
            canvas.shapes.remove(i);
        }
        canvas.repaint();
    }

    private void redo() {
        if (!canvas.filledTempDelayRedo.isEmpty() && !canvas.filledTempsRedo.isEmpty() && !canvas.filledTempDelayRedo.get(canvas.filledTempDelayRedo.size() - 1)) {
            canvas.filledTempDelay.add(canvas.filledTempDelayRedo.get(canvas.filledTempDelayRedo.size() - 1));
            canvas.filledTemps.add(canvas.filledTempsRedo.get(canvas.filledTempsRedo.size() - 1));

            canvas.filledTemps.get(canvas.filledTemps.size() - 1).color = returnToPreviousColor(canvas.filledTempsRedo.get(canvas.filledTempsRedo.size() - 1));

            canvas.filledTempDelayRedo.remove(canvas.filledTempDelayRedo.size() - 1);
            canvas.filledTempsRedo.remove(canvas.filledTempsRedo.size() - 1);

            canvas.repaint();
            return;
        }
        if (!canvas.filledTempDelayRedo.isEmpty() && canvas.filledTempDelayRedo.get(canvas.filledTempDelayRedo.size() - 1)) {
            canvas.filledTempDelay.add(canvas.filledTempDelayRedo.get(canvas.filledTempDelayRedo.size() - 1));
            canvas.filledTempDelayRedo.remove(canvas.filledTempDelayRedo.size() - 1);
        }
        for (int endOfCount = 0, i = canvas.shapesRedo.size() - 1; i >= 0; i--) {
            if (canvas.shapesRedo.get(i).isEndOfShape()) {
                endOfCount++;
            }
            //In btnUndo case we added shapes when endOfCount equals 2 because of that we have to check shapes for not add them twice
            if (!canvas.shapes.isEmpty() && canvas.shapes.get(canvas.shapes.size() - 1) == canvas.shapesRedo.get(i)) {
                canvas.shapesRedo.remove(i);
                if (endOfCount == 2) {
                    break;
                } else {
                    continue;
                }
            }
            canvas.shapes.add(canvas.shapesRedo.get(i));
            canvas.shapesRedo.remove(i);
            if (endOfCount == 2) {
                break;
            }
        }
        canvas.repaint();
    }

    public static void main(String[] args) {
        new GUI();
    }
}
