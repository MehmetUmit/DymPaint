package dympaint;

import java.awt.AWTException;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.MouseInfo;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.List;
import java.util.ArrayList;
import javax.swing.JPanel;
import java.awt.RenderingHints;
import java.awt.Robot;
import java.awt.event.MouseAdapter;

public class Canvas extends JPanel {

    public int shapeType, shapeThickness;
    public int mousePressedX, mousePressedY, mouseReleasedX, mouseReleasedY, mouseDraggedX, mouseDraggedY;
    public int width, height, startX, startY;
    public Color shapeColor, colorOfPicker, canvasColor = new Color(238, 238, 238);
    private Robot robot;
    public List<Shape> shapes = new ArrayList<>();
    public List<FilledTemp> filledTemps = new ArrayList<>();
    public List<FilledTemp> filledTempsRedo = new ArrayList<>();
    public List<Shape> shapesRedo = new ArrayList<>();
    private List<ColorPickerListener> colorChooserListeners = new ArrayList<>();
    private List<MousePositionListener> mousePositionListeners = new ArrayList<>();
    public boolean mousePressed = false;
    public List<Boolean> filledTempDelay = new ArrayList<>();
    public List<Boolean> filledTempDelayRedo = new ArrayList<>();
    public Image imageTemp, imageDefault;
    public boolean imageOpened = false;
    public double widthScale = 1.0, heightScale = 1.0;
    public boolean mouseDragged = false;
    public int sizeWidth, sizeHeight;

    public Canvas() {
        sizeWidth = 500;
        sizeHeight = 500;
        this.setPreferredSize(new Dimension(sizeWidth, sizeHeight));
        setBackground(canvasColor);
        shapeColor = Color.black;
        shapeType = Shape.BRUSH;
        try {
            robot = new Robot();
        } catch (AWTException ex) {
            System.out.println("Robot creation failed!\n" + ex);
        }
        this.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                notifyMousePositionListener(e.getX(), e.getY());
                if (shapeType == Shape.COLOR_PICKER) {
                    colorOfPicker = robot.getPixelColor(MouseInfo.getPointerInfo().getLocation().x, MouseInfo.getPointerInfo().getLocation().y);
                    notifyColorChooserListener(colorOfPicker, ColorPickerListener.MOVED);
                }
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                if ((shapeType == Shape.BRUSH || shapeType == Shape.ERASER) && (mouseDraggedX != (int) (e.getX() / widthScale) || mouseDraggedY != (int) (e.getY() / widthScale))) {
                    //We added "filledTempDelay.add(true)" to mouseRelased no need to add here
                    if (shapeType == Shape.BRUSH) {
                        shapes.add(new Shape((int) (e.getX() / widthScale), (int) (e.getY() / widthScale), shapeColor, shapeThickness, shapeType));
                    } else {
                        //Eraser
                        shapes.add(new Shape((int) (e.getX() / widthScale), (int) (e.getY() / widthScale), canvasColor, shapeThickness, shapeType));
                    }
                }
                mouseDraggedX = (int) (e.getX() / widthScale);
                mouseDraggedY = (int) (e.getY() / widthScale);
                mouseDragged = true;
                repaint();
            }
        });
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseExited(MouseEvent e) {
                notifyMousePositionListener();
            }

            @Override
            public void mousePressed(MouseEvent e) {
                //We have to clear redo if shapetype not equal color picker
                if (shapeType != Shape.COLOR_PICKER) {
                    shapesRedo.clear();
                    filledTempsRedo.clear();
                    filledTempDelayRedo.clear();
                }
                mousePressedX = (int) (e.getX() / widthScale);
                mousePressedY = (int) (e.getY() / widthScale);
                if (shapeType == Shape.COLOR_PICKER) {
                    colorOfPicker = robot.getPixelColor(MouseInfo.getPointerInfo().getLocation().x, MouseInfo.getPointerInfo().getLocation().y);
                    notifyColorChooserListener(colorOfPicker, ColorPickerListener.PRESSED);
                } else if (shapeType == Shape.BRUSH) {
                    shapes.add(new Shape(mousePressedX, mousePressedY, shapeColor, shapeThickness, shapeType));
                    filledTempDelay.add(true);
                    shapes.get(shapes.size() - 1).pressed = true;
                    repaint();
                } else if (shapeType == Shape.ERASER) {
                    shapes.add(new Shape(mousePressedX, mousePressedY, canvasColor, shapeThickness, shapeType));
                    filledTempDelay.add(true);
                    shapes.get(shapes.size() - 1).pressed = true;
                    repaint();
                } else if (shapeType == Shape.FILL) {
                    filledTempDelay.add(false);
                    fill(MouseInfo.getPointerInfo().getLocation().x, MouseInfo.getPointerInfo().getLocation().y);
                    repaint();
                }
                mousePressed = true;
            }

            @Override
            public void mouseReleased(MouseEvent e) {

                mousePressed = false;
                if (shapeType == Shape.CIRCLE || shapeType == Shape.LINE || shapeType == Shape.RECTANGLE) {
                    shapes.add(new Shape(startX, startY, width, height, shapeColor, shapeThickness, shapeType));
                }
                if (!shapes.isEmpty() && shapeType != Shape.COLOR_PICKER && shapeType != Shape.FILL) {
                    shapes.get(shapes.size() - 1).setEndOfShape();
                    filledTempDelay.add(true);
                }
                if (!mouseDragged) {

                } else {
                    mouseDragged = false;
                }
            }
        });
    }

    public void resetCanvas() {
        shapes.clear();
        filledTemps.clear();
        filledTempsRedo.clear();
        shapesRedo.clear();
        filledTempDelay.clear();
        filledTempDelayRedo.clear();
        canvasColor = new Color(238, 238, 238);
        if (this.imageOpened == true) {
            imageOpened = false;
            this.imageDefault.flush();
            this.imageTemp.flush();
        }
        setBackground(canvasColor);
        repaint();
    }

    public void drawShapes(Graphics2D g2d) {
        for (int i = 0; i < shapes.size(); i++) {
            g2d.setColor(shapes.get(i).color);
            switch (shapes.get(i).type) {
                case Shape.CIRCLE:
                    g2d.setStroke(new BasicStroke(shapes.get(i).thickness));
                    g2d.drawOval(shapes.get(i).x, shapes.get(i).y, shapes.get(i).width, shapes.get(i).height);
                    break;
                case Shape.BRUSH:
                    g2d.setStroke(new BasicStroke(shapes.get(i).thickness, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                    if (shapes.get(i).pressed) {
                        g2d.drawLine(shapes.get(i).x, shapes.get(i).y, shapes.get(i).x, shapes.get(i).y);
                    } else if (i != 0 && !shapes.get(i - 1).isEndOfShape() && shapes.get(i - 1).type == Shape.BRUSH) {
                        g2d.drawLine(shapes.get(i - 1).x, shapes.get(i - 1).y, shapes.get(i).x, shapes.get(i).y);
                    }
                    break;
                case Shape.RECTANGLE:
                    g2d.setStroke(new BasicStroke(shapes.get(i).thickness));
                    g2d.drawRect(shapes.get(i).x, shapes.get(i).y, shapes.get(i).width, shapes.get(i).height);
                    break;
                case Shape.LINE:
                    g2d.setStroke(new BasicStroke(shapes.get(i).thickness));
                    g2d.drawLine(shapes.get(i).x, shapes.get(i).y, shapes.get(i).width, shapes.get(i).height);
                    break;
                case Shape.ERASER:
                    g2d.setStroke(new BasicStroke(shapes.get(i).thickness, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                    if (shapes.get(i).pressed) {
                        g2d.drawLine(shapes.get(i).x, shapes.get(i).y, shapes.get(i).x, shapes.get(i).y);
                    } else if (i != 0 && !shapes.get(i - 1).isEndOfShape() && shapes.get(i - 1).type == Shape.ERASER) {
                        g2d.drawLine(shapes.get(i - 1).x, shapes.get(i - 1).y, shapes.get(i).x, shapes.get(i).y);
                    }
                default:
                    break;
            }
        }
        //For shapes not added to list yet
        if (mousePressed) {
            switch (shapeType) {
                case Shape.RECTANGLE:
                    g2d.setColor(shapeColor);
                    g2d.setStroke(new BasicStroke(shapeThickness));
                    calculateShapePoint();
                    g2d.drawRect(startX, startY, width, height);
                    break;
                case Shape.CIRCLE:
                    g2d.setStroke(new BasicStroke(shapeThickness));
                    g2d.setColor(shapeColor);
                    calculateShapePoint();
                    g2d.drawOval(startX, startY, width, height);
                    break;
                case Shape.LINE:
                    g2d.setStroke(new BasicStroke(shapeThickness));
                    g2d.setColor(shapeColor);
                    calculateShapePoint();
                    g2d.drawLine(startX, startY, width, height);
                    break;
                default:
                    break;
            }
        }
    }

    public void notifyColorChooserListener(Color c, boolean t) {
        colorChooserListeners.forEach((cl) -> {
            cl.colorPicked(c, t);
        });
    }

    public void addColorChooserListener(ColorPickerListener cl) {
        colorChooserListeners.add(cl);
    }

    public void addMousePositionListener(MousePositionListener mpl) {
        mousePositionListeners.add(mpl);
    }

    public void notifyMousePositionListener(int x, int y) {
        mousePositionListeners.forEach((mpl) -> {
            mpl.mousePositionChanged((int) (x / widthScale), (int) (y / heightScale));
        });
    }

    public void notifyMousePositionListener() {
        mousePositionListeners.forEach((mpl) -> {
            mpl.mousePositionChanged();
        });
    }

    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        super.paintComponent(g2d);
        g2d.scale(widthScale, heightScale);
        if (imageOpened) {
            g2d.drawImage(imageTemp, 0, 0, null);
        }
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        drawShapes(g2d);
    }

    public void zoom(float val) {
        widthScale = val / 20;
        heightScale = val / 20;
        if (widthScale == 0) {
            widthScale = 0.05;
        }
        if (heightScale == 0) {
            heightScale = 0.05;
        }
        scaleCanvas();
    }

    public void scaleCanvas() {
        this.setPreferredSize(new Dimension((int) (sizeWidth * widthScale), (int) (sizeHeight * heightScale)));
        revalidate();
        repaint();
    }

    public void fill(int x, int y) {
        //Color mouseColor = robot.getPixelColor(x, y);
        //int positionX = 0, positionY = 0;
        boolean flag = false;
        int index = 0, startIndex = 0, endIndex = shapes.size() - 1;
        Color tempColor;
        x = mousePressedX;
        y = mousePressedY;

        //Which object will be filled
        for (int i = shapes.size() - 1; i >= 0; i--) {
            //Control for each shape type
            if (Math.abs(shapes.get(i).x - x) <= shapes.get(i).thickness / 2 && Math.abs(shapes.get(i).y - y) <= shapes.get(i).thickness / 2) {
                if (shapes.get(i).type == Shape.ERASER) {
                    break;
                }
                //positionX = shapes.get(i).x;
                //positionY = shapes.get(i).y;
                //mouseColor = shapes.get(i).color;
                index = i;
                flag = true;
                break;
            }
        }
        //Pointer is on the canvas or eraser
        if (flag == false) {
            tempColor = canvasColor;
            canvasColor = shapeColor;
            setBackground(canvasColor);
            filledTemps.add(new FilledTemp(FilledTemp.CANVAS_INDEX, FilledTemp.CANVAS_INDEX, tempColor));
            changeEraserColor();
            return;
        } else {//Pointer is on the other shapes
            tempColor = shapes.get(index).color;
            for (int i = index; i < shapes.size(); i++) {
                shapes.get(i).color = shapeColor;
                if (shapes.get(i).isEndOfShape()) {
                    endIndex = i;
                    break;
                }
            }
            //We already changed tempColor of the end of shape before previous for loop
            //No need start from index again
            for (int i = --index; i >= 0; i--) {
                if (shapes.get(i).isEndOfShape()) {
                    startIndex = i;
                    break;
                }
                shapes.get(i).color = shapeColor;
            }
        }
        filledTemps.add(new FilledTemp(startIndex, endIndex, tempColor));
    }

    public void changeEraserColor() {
        for (int i = shapes.size() - 1; i >= 0; i--) {
            if (shapes.get(i).type == Shape.ERASER) {
                shapes.get(i).color = canvasColor;
            }
        }
    }

    public void calculateShapePoint() {
        if (shapeType == Shape.LINE) {
            startX = mousePressedX;
            startY = mousePressedY;
            width = mouseDraggedX;
            height = mouseDraggedY;
            return;
        }
        if (mouseDraggedX < mousePressedX) {
            if (mouseDraggedY < mousePressedY) {
                startY = mouseDraggedY;
                height = mousePressedY - mouseDraggedY;
            } else {
                startY = mousePressedY;
                height = mouseDraggedY - mousePressedY;
            }
            startX = mouseDraggedX;
            width = mousePressedX - mouseDraggedX;

        } else if (mouseDraggedY < mousePressedY) {
            startX = mousePressedX;
            startY = mouseDraggedY;
            width = mouseDraggedX - mousePressedX;
            height = mousePressedY - mouseDraggedY;
        } else {
            startX = mousePressedX;
            startY = mousePressedY;
            width = mouseDraggedX - mousePressedX;
            height = mouseDraggedY - mousePressedY;
        }
    }
}
