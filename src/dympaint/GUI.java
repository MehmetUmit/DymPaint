package dympaint;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.JFileChooser;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class GUI extends JFrame implements ActionListener, ChangeListener, ColorPickerListener, MousePositionListener {

    static Font font = new Font("Comic Sans", Font.BOLD, 20);
    public Font mousePosFont = new Font(Font.MONOSPACED, Font.BOLD, 20);
    Dimension screenDimension;
    private ImageIcon imageIcon = new ImageIcon(getClass().getResource("/resources/icon.png"));
    Color color;
    Color textColor;
    Color menuBarColor;
    PanelMenu panelMenu;
    JMenuBar menuBar;
    JMenu menuFile;
    JMenuItem menuItemSave, menuItemNew, menuItemOpen;
    Canvas canvas;
    JScrollPane scrollPane;
    JPanel boardCanvas;
    JLabel labelMousePos;
    Button btnResize;

    public GUI() {
        setIconImage(imageIcon.getImage());
        textColor = Color.white;
        menuBarColor = new Color(0x123456);
        screenDimension = Toolkit.getDefaultToolkit().getScreenSize();
        setTitle("DymPaint");
        panelMenu = new PanelMenu();
        boardCanvas = new JPanel();
        canvas = new Canvas();
        labelMousePos = new JLabel("X: " + addSpace("", 5) + "Y: " + addSpace("", 5));
        menuBar = new JMenuBar();
        menuFile = new JMenu("File");
        menuItemNew = new JMenuItem("New");
        menuItemOpen = new JMenuItem("Open");
        menuItemSave = new JMenuItem("Save");
        btnResize = new Button("Resize", Button.RESIZE_BUTTON);

        labelMousePos.setFont(mousePosFont);
        labelMousePos.setForeground(Color.WHITE);
        btnResize.setBackground(menuBarColor);
        btnResize.setForeground(Color.white);
        btnResize.setFocusable(false);
        btnResize.setBorderPainted(false);
        menuFile.setForeground(textColor);
        menuBar.setBackground(menuBarColor);
        menuBar.setBorderPainted(false);

        setJMenuBar(menuBar);
        canvas.shapeThickness = panelMenu.strokeSlider.getValue();
        setSize(1000, 880);
        setMinimumSize(new Dimension(400, 880));
        setLocation((int) screenDimension.getWidth() / 2 - getSize().width / 2, (int) screenDimension.getHeight() / 2 - getSize().height / 2);
        menuBar.add(menuFile);
        menuBar.add(btnResize);
        menuBar.add(Box.createHorizontalGlue());
        menuBar.add(labelMousePos);
        menuFile.add(menuItemNew);
        menuFile.add(menuItemSave);
        menuFile.add(menuItemOpen);
        add(panelMenu, BorderLayout.WEST);

        boardCanvas.setBackground(new Color(0xb3b3b3));
        boardCanvas.setLayout(new GridBagLayout());
        boardCanvas.add(canvas);
        scrollPane = new JScrollPane(boardCanvas);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        add(scrollPane);

        menuItemNew.addActionListener(this);
        menuItemSave.addActionListener(this);
        menuItemOpen.addActionListener(this);

        panelMenu.btnMainColor.addActionListener(this);
        panelMenu.btnCircle.addActionListener(this);
        panelMenu.btnBrush.addActionListener(this);
        panelMenu.btnRectangle.addActionListener(this);
        panelMenu.btnUndo.addActionListener(this);
        panelMenu.strokeSlider.addChangeListener(this);
        panelMenu.zoomSlider.addChangeListener(this);
        panelMenu.btnFill.addActionListener(this);
        panelMenu.btnRedo.addActionListener(this);
        panelMenu.btnEraser.addActionListener(this);
        panelMenu.btnLine.addActionListener(this);
        panelMenu.btnColorPicker.addActionListener(this);
        panelMenu.btnZoomIn.addActionListener(this);
        panelMenu.btnZoomOut.addActionListener(this);
        canvas.addColorChooserListener(this);
        canvas.addMousePositionListener(this);
        btnResize.addActionListener(this);
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

    public void resizeCanvas(int width, int height) {
        canvas.sizeHeight = height;
        canvas.sizeWidth = width;
        if (canvas.imageOpened) {
            canvas.imageTemp = canvas.imageDefault.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        }
        canvas.scaleCanvas();
    }

    public String spaceIntoNum(int num, int totalSpace) {
        String result = "" + num;
        if (num < 0) {
            totalSpace--;
            num *= -1;
        }
        int numberOfDigits = (num == 0) ? 1 : 1 + (int) Math.log10(num);
        int remainderSpace = totalSpace - numberOfDigits;
        return addSpace(result, remainderSpace);

    }

    public String addSpace(String s, int totalSpace) {
        String result = s;
        while (totalSpace > 0) {
            result += " ";
            totalSpace--;
        }
        return result;
    }

    @Override
    public void colorPicked(Color c, boolean t) {
        if (t == ColorPickerListener.PRESSED) {
            canvas.shapeColor = c;
            panelMenu.btnTemp.setEnabled(true);
            canvas.shapeType = Shape.BRUSH;
            panelMenu.btnBrush.setEnabled(false);
            panelMenu.btnTemp = panelMenu.btnBrush;
        }
        panelMenu.btnMainColor.setBackground(c);
    }

    @Override
    public void mousePositionChanged(int x, int y) {
        labelMousePos.setText("X: " + spaceIntoNum(x, 5) + "Y: " + spaceIntoNum(y, 5));
    }

    @Override
    public void mousePositionChanged() {
        labelMousePos.setText("X: " + addSpace("", 5) + "Y: " + addSpace("", 5));
    }

    @Override
    public void stateChanged(ChangeEvent ce) {
        if (ce.getSource() == panelMenu.strokeSlider) {
            canvas.shapeThickness = panelMenu.strokeSlider.getValue();
        } else if (ce.getSource() == panelMenu.zoomSlider) {
            canvas.zoom(panelMenu.zoomSlider.getValue());
            panelMenu.zoomVal.setText("%" + (int) (canvas.widthScale * 100));
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object eventSource = e.getSource();
        Button btn = null;
        JMenuItem selectedMenuItem;
        if (eventSource instanceof Button) {
            btn = (Button) eventSource;
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
            } else if (btn == panelMenu.btnZoomIn) {
                panelMenu.zoomSlider.setValue(panelMenu.zoomSlider.getValue() + 2);
            } else if (btn == panelMenu.btnZoomOut) {
                panelMenu.zoomSlider.setValue(panelMenu.zoomSlider.getValue() - 2);
            } else if (btn == btnResize) {
                PanelResize panelResize = new PanelResize();
                int option = JOptionPane.showConfirmDialog(this, panelResize, "Resize Canvas", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
                if (option == JOptionPane.OK_OPTION) {
                    if (!panelResize.textFieldWidth.getText().equals("") && !panelResize.textFieldHeight.getText().equals("")) {
                        resizeCanvas(Integer.parseInt(panelResize.textFieldWidth.getText()), Integer.parseInt(panelResize.textFieldHeight.getText()));
                    }
                }
            } else {
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
        } else if (eventSource instanceof JMenuItem) {
            selectedMenuItem = (JMenuItem) eventSource;
            if (selectedMenuItem == menuItemSave) {
                save();
            } else if (selectedMenuItem == menuItemNew) {
                newSketch();
            } else if (selectedMenuItem == menuItemOpen) {
                open();
            }
        }
        canvas.revalidate();
        repaint();
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

    public void newSketch() {
        canvas.resetCanvas();
    }

    public void open() {
        JFileChooser fileChooser = new JFileChooser();
        int val = fileChooser.showOpenDialog(this);
        String filePath;
        if (val == JFileChooser.APPROVE_OPTION) {
            filePath = fileChooser.getSelectedFile().getAbsolutePath();
        } else {
            return;
        }
        try {
            canvas.imageDefault = ImageIO.read(new File(filePath));
            canvas.imageTemp = canvas.imageDefault;
            canvas.sizeHeight = canvas.imageDefault.getHeight(this);
            canvas.sizeWidth = canvas.imageDefault.getWidth(this);
            canvas.scaleCanvas();
            canvas.imageOpened = true;
        } catch (IOException ex) {
            ex.getStackTrace();
        }
    }

    public void save() {
        double oldWidthScale = canvas.widthScale;
        double oldHeightScale = canvas.heightScale;
        canvas.widthScale = 1;
        canvas.heightScale = 1;
        canvas.scaleCanvas();
        JFileChooser fileChooser = new JFileChooser();
        int val = fileChooser.showSaveDialog(this);
        String filePath;
        if (val == JFileChooser.APPROVE_OPTION) {
            filePath = fileChooser.getSelectedFile().getAbsolutePath();
        } else {
            return;
        }
        BufferedImage image = new BufferedImage(canvas.sizeWidth, canvas.sizeHeight, BufferedImage.TYPE_INT_RGB);
        canvas.printAll(image.getGraphics());
        canvas.widthScale = oldWidthScale;
        canvas.heightScale = oldHeightScale;
        canvas.scaleCanvas();
        try {
            ImageIO.write(image, "png", new File(filePath + ".png"));
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }
}
