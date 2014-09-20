/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.texsoftware.component;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.game.GameCanvas;
import javax.microedition.lcdui.game.Sprite;
import net.texsoftware.layouts.AbsoluteFieldManager;
import net.texsoftware.layouts.Manager;
import net.texsoftware.layouts.VerticalFieldManager;
import net.texsoftware.logger.Log;
import net.texsoftware.navigation.UiApplication;

/**
 * Screen class that represents the objects on the screen, either managers or
 * fields.
 *
 * @author Jibz7
 */
public class Screen extends GameCanvas {

    Screen parent = null;
    int pointerX = 0;
    int pointerY = 0;
    public int screenWidth = 0;
    public int screenHeight = 0;
    public int bannerHeight = 0;
    VerticalFieldManager screenManager = null;
    Image offScreenBuffer = null;
    private Graphics graphics = null;
    public static int BG_COLOR = 0xffffff;
    Vector fieldCoordMatch = null;
    boolean paintScroll = false;
    boolean scrollable = false;
    private final int TOUCH_POINTS = 1;
    int previousX = -1;
    int previousY = -1;
    private boolean visible = false;
    // variables to hold the position of the cursor
    int cursorPointerX = 0;
    int cursorPointerY = 0;
    boolean drawCursor = false;

    public Screen(Screen parent) {
        super(false);
        this.parent = parent;

        screenManager = new VerticalFieldManager();
        createOffScreenBuffer(Display.getInstance().getDisplayWidth(), Display.getInstance().getDisplayHeight());
    }

    public Screen(Screen parent, int width, int height, Image baseImage, boolean paintBg) {
        super(false);

        this.parent = parent;

        screenManager = new VerticalFieldManager();

        screenWidth = width;
        screenHeight = height;

        offScreenBuffer = baseImage;
        graphics = offScreenBuffer.getGraphics();

        if (paintBg) {
            paintBackground();
        }

    }

    public void createOffScreenBuffer(int width, int height) {
        screenWidth = width;
        screenHeight = height;

        if (offScreenBuffer != null) {
            offScreenBuffer = null;
            //graphics = null;
        }
        offScreenBuffer = Image.createImage(width, height);
        graphics = offScreenBuffer.getGraphics();

        screenManager.setGraphics(graphics);
        paintBackground();
    }

    public boolean isScrollable() {
        return scrollable;
    }

    public void setScrollable(boolean scrollable) {
        this.scrollable = scrollable;
    }

    public void setBanner(Field field) {
        bannerHeight = field.getHeight();
        add(field);
    }

    public void setBanner(VerticalFieldManager manager) {
        bannerHeight = manager.getHeight();
        add(manager);
    }

    public Image getOffScreenBuffer() {
        return offScreenBuffer;
    }

    public void setOffScreenBuffer(Image offScreenBuffer) {
        this.offScreenBuffer = offScreenBuffer;
    }

    public Screen getParent() {
        return parent;
    }

    public Graphics getGraphics() {
        return graphics;
    }

    public Manager getScreenManager() {
        return screenManager;
    }

    public void setGraphics(Graphics graphics) {
        this.graphics = graphics;
    }

    public void paintBackground() {
        if (graphics != null) {
            graphics.setColor(BG_COLOR);
            graphics.fillRect(0, 0, screenWidth, screenHeight);
        }
    }
    Image cursorImage = null;

    public void paintCursor(Graphics g, int xPosition, int yPosition) {
        if (cursorImage == null) {
            try {
                cursorImage = Image.createImage("/cursor.png");
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        if (xPosition < 0) {
            xPosition = 0;
        } else if (xPosition > Display.getInstance().getDisplayWidth()) {
            xPosition = Display.getInstance().getDisplayWidth();
        }

        if (yPosition < 0) {
            yPosition = 0;
        } else if (yPosition > Display.getInstance().getDisplayHeight()) {
            yPosition = Display.getInstance().getDisplayHeight();
        }

        g.drawImage(cursorImage, xPosition, yPosition, Graphics.TOP | Graphics.LEFT);
    }

    public void paintScrollCircle(int yPosition, int height) {
        graphics.setColor(0xd9201f);
        if (height < 8) {
            height = 8;
        }
        graphics.fillRoundRect(screenWidth - 6, yPosition, 4, height, 15, 5);
    }

    public void paintScrollUpTriangle(Graphics graphics, int yPosition) {
        graphics.setColor(0xd9201f);
        graphics.fillTriangle(screenWidth - 16, yPosition, screenWidth - 4, yPosition, screenWidth - 10, yPosition - 8);
        graphics.fillTriangle(screenWidth - 16, yPosition, screenWidth - 4, yPosition, screenWidth - 10, yPosition - 8);
    }

    public void paintScrollDownTriangle(Graphics graphics, int yPosition) {
        graphics.setColor(0xd9201f);
        graphics.fillTriangle(screenWidth - 16, yPosition, screenWidth - 4, yPosition, screenWidth - 10, yPosition + 8);
        graphics.fillTriangle(screenWidth - 16, yPosition, screenWidth - 4, yPosition, screenWidth - 10, yPosition + 8);
    }

    public void add(Field field) {
        screenManager.addField(field);
        drawField(field, screenManager);
    }

    public void add(Manager manager) {
        //set the start point for the manager
        manager.setxPos(screenManager.getxPos());
        manager.setyPos(screenManager.getyPos());

        screenManager.addManager(manager);
        drawFieldManager(manager);
    }

    public void delete(Field field) {
        screenManager.delete(field);
        redraw(Display.getInstance().getDisplayWidth(), Display.getInstance().getDisplayHeight());
    }

    public void delete(Manager manager) {
        screenManager.delete(manager);
        redraw(Display.getInstance().getDisplayWidth(), Display.getInstance().getDisplayHeight());
    }

    public void deleteAllFields() {
        screenManager = null;
        screenManager = new VerticalFieldManager();
        redraw(Display.getInstance().getDisplayWidth(), Display.getInstance().getDisplayHeight());
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visibility) {
        visible = visibility;
    }
    Timer mTimer = null;

    /**
     * Called when showing canvas.
     *
     * @see javax.microedition.lcdui.Canvas#showNotify()
     */
    protected void showNotify() {
        visible = true;
        mTimer = new Timer();
        // Periodically update draw area graphics.
        TimerTask ui = new TimerTask() {
            public void run() {
                refresh();
//                flushGraphics();
            }
        };
        mTimer.schedule(ui, 0, 80);
    }

    /**
     * Called when hiding the canvas.
     *
     * @see javax.microedition.lcdui.Canvas#hideNotify()
     */
    protected void hideNotify() {
        visible = false;
        if (mTimer != null) {
            mTimer.cancel();
        }
    }

    public void invalidate() {
        paintBackground();
        screenManager.invalidate();
        refresh();
    }

    public void refresh() {
        repaint();
    }

    /**
     * Draw all field on the screen
     */
    private void drawFields() {
        if (offScreenBuffer != null) {
            for (int i = 0; i < screenManager.getFields().size(); i++) {
                if (screenManager.getFields().elementAt(i) instanceof Field) {
                    Field f = (Field) screenManager.getFields().elementAt(i);
                    drawField(f, screenManager);
                    f = null;
                } else if (screenManager.getFields().elementAt(i) instanceof Manager) {
                    Manager m = (Manager) screenManager.getFields().elementAt(i);
                    drawFieldManager(m);
                    m = null;
                }
            }
        }
    }

    private void drawFieldManager(Manager manager) {
        if (offScreenBuffer != null) {
            final int tempHeight = 0;
            for (int i = 0; i < manager.getFields().size(); i++) {
                if (manager.getFields().elementAt(i) instanceof Field) {
                    Field f = (Field) manager.getFields().elementAt(i);
                    drawField(f, manager);
                } else if (manager.getFields().elementAt(i) instanceof Manager) {
                    Manager m = (Manager) manager.getFields().elementAt(i);
                    m.setxPos(manager.getxPos() + m.getxPos());
                    m.setyPos(manager.getyPos() + m.getyPos());
                    drawFieldManager(m);
                }
            }
        }
    }

    private void drawField(final Field field, Manager manager) {
        try {
            if (offScreenBuffer != null) {
                //after first painting a field you cannot change the x & y positions
                if (manager instanceof AbsoluteFieldManager) {
                    //leave x & y pos of the field as is
                } else {
                    // change yPos to curr height of manager
                    if (!field.painted) {
                        field.setxPos(manager.getxPos() + field.getxPos());
                        field.setyPos(manager.getyPos() + field.getyPos());
                    }
                }

                field.paint(graphics, field.getxPos(), field.getyPos());

                int availableSpace = (screenHeight - screenManager.getyPos());

                //if the field's height is more than the remaining available space, redraw the current screen buffer
                if (screenHeight < field.getyPosLast()) {
                    // if field exceeds remaining height
                    paintScroll = true;
                    int newHeight = field.getyPosLast();
                    Log.out("new hieght is : " + newHeight);
                    // refresh the current offscreen buffer with new dimensions 
                    if (!outOfMemory) {
                        redraw(screenWidth, newHeight);
                    }
                }
            }
        } catch (OutOfMemoryError ex) {
            outOfMemory = true;
            Log.out("Ooops we are out of memory");
            Log.out(ex.getMessage());
            Log.showFreeMemory();

            offScreenBuffer = null;
            graphics = null;

            redraw(screenWidth, Display.getInstance().getDisplayHeight());
        }
    }
    boolean outOfMemory = false;

    public void redraw() {
        redraw(screenWidth, screenHeight);
    }

    public void redraw(int width, int height) {
        createOffScreenBuffer(width, height);

        //refresh the current offscreen buffer with new dimensions
        paintBackground();
        drawFields();
        refresh();
    }

    /**
     * recursively find all fields that match the coordinates given.
     *
     * @param manager
     * @param x
     * @param y
     */
    private void findFieldByCoordinates(final Manager manager, final int x, final int y) {
        if (manager != null && !manager.getFields().isEmpty()) { // if screen objects are not empty            
            for (int i = 0; i < manager.getFields().size(); i++) {
                if (manager.getFields().elementAt(i) instanceof Field) {
                    Field tempField = (Field) manager.getFields().elementAt(i);
                    if (tempField.coordinatesMatch(x, y)) {
                        fieldCoordMatch.addElement(tempField);
                    }
                    tempField = null;
                } else if (manager.getFields().elementAt(i) instanceof Manager) {
                    Manager tempManager = (Manager) manager.getFields().elementAt(i);
                    findFieldByCoordinates(tempManager, x, y);
                    tempManager = null;
                }
            }
        }
    }
    int paintX = 0;
    int paintY = 0;
    int screenXDiff = 0;
    int screenYDiff = 0;
    boolean scrollEnd = false;

    public void resetScrollPosition(boolean begin) {
        if (begin) {
            paintX = 0;
            paintY = 0;
            screenYDiff = 0;
        } else {
            paintX = 0;
            paintY = screenHeight;
            screenYDiff = screenHeight;
        }
        repaint();
    }

    // y coord of currently painted area
    public int getPaintY() {
        return paintY;
    }

    public void paint(Graphics g) {
        if (offScreenBuffer != null) {
            paintY = paintY + screenYDiff;
            if (paintY < 0) {
                paintY = 0;
            } else if (paintY > (screenHeight - Display.getInstance().getDisplayHeight())) { //show height up to the display height of the screen.
                paintY = screenHeight - Display.getInstance().getDisplayHeight();
                if (scrollEnd == false) {
                    //if first time scroll ends run onScrollEnd
                    onScrollEnd();
                }
                scrollEnd = true;
            } else {
                scrollEnd = false;
            }

            g.drawImage(offScreenBuffer, -paintX, -paintY, Graphics.LEFT | Graphics.TOP);

            if (bannerHeight > 0) {
                //draw banner
                g.drawRegion(offScreenBuffer, 0, 0, screenWidth, bannerHeight, Sprite.TRANS_NONE, 0, 0, Graphics.TOP | Graphics.LEFT);
            }

            if (paintScroll && scrollable) {
                //paint scrollup triangle if the screen has been scrolled below
                if (paintY > 0) {
                    paintScrollUpTriangle(g, Display.getInstance().getDisplayHeight() - 33);
                }

                //paint scroll down if scroll not ended
                if (!scrollEnd) {
                    paintScrollDownTriangle(g, Display.getInstance().getDisplayHeight() - 30);
                }
            }

            if (drawCursor) {
                paintCursor(g, cursorPointerX, cursorPointerY);
            }
        }
    }

    public void setFullScreen(boolean value) {
        setFullScreenMode(value);
    }

    /**
     * Called when the screen is shown from a previous screen
     */
    public void onShow() {
    }

    /**
     * Called when the page reaches the end of scroll.
     */
    public void onScrollEnd() {
    }

    /**
     * When the back button of a screen is clicked, it displays the previous
     * screen and calls its onShow method. If the parent is null, it exits the
     * app.
     */
    public void goBack() {
        try {
            if (parent == null) {
                UiApplication.getInstance().getMidlet().quitApp();
            } else {
                UiApplication.getInstance().pushScreen(parent);
                parent.onShow();
                parent = null;
                screenManager = null;
                offScreenBuffer = null;
                graphics = null;
                fieldCoordMatch = null;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    protected void pointerPressed(int x, int y) {
//        Log.out("Pointer Pressed : " + x + "-> " + y);
        pointerX = x;
        pointerY = y;

        cursorPointerX = x;
        cursorPointerY = y;
    }

    protected void pointerReleased(int x, int y) {
        if (absDiff(x, pointerX) < 5 && absDiff(y, pointerY) < 5) {
            onClick(x, y);
        }
//        Log.out("Pointer Released : " + x + "-> " + y);
    }

    protected void pointerDragged(int x, int y) {
//        Log.out("Pointer Dragged : " + x + "-> " + y);
        screenYDiff = (pointerY - y) / 6; //reduce scroll speed by int to reduce scroll speed

        cursorPointerX = x;
        cursorPointerY = y;
    }

    protected void keyPressed(int keyCode) {
        drawCursor = true;

        if (keyCode == -1 || keyCode == GameCanvas.UP_PRESSED) { //-1
            screenYDiff = -1;
            cursorPointerY = cursorPointerY - 1;
        } else if (keyCode == -2 || keyCode == GameCanvas.DOWN_PRESSED) {  //-2
            screenYDiff = 1;
            cursorPointerY = cursorPointerY + 1;
        } else if (keyCode == -3 || keyCode == GameCanvas.LEFT_PRESSED) { //-3
            screenXDiff = -1;
            cursorPointerX = cursorPointerX - 1;
        } else if (keyCode == -4 || keyCode == GameCanvas.RIGHT_PRESSED) { //-4
            screenXDiff = 1;
            cursorPointerX = cursorPointerX + 1;
        } else if (keyCode == -5 || keyCode == GameCanvas.FIRE_PRESSED) { // -5
            onClick(cursorPointerX, cursorPointerY);
        }

        //normalize the cursor pointer position withing screen bounds
        if (cursorPointerX < 0) {
            cursorPointerX = 0;
        } else if (cursorPointerX > screenWidth) {
            cursorPointerX = screenWidth;
        }

        if (cursorPointerY < 0) {
            cursorPointerY = 0;
        } else if (cursorPointerY > screenHeight) {
            cursorPointerY = screenHeight;
        }
        keyTimeLapse = System.currentTimeMillis();
        refresh();
    }

    protected void keyReleased(int keyCode) {
        refresh();
        increment = 1;
        keyTimeLapse = 0L;
    }
    int increment = 1; //make for fast scrolling
    long keyTimeLapse = 0L;

    /**
     * Move mouse pointer 1 pixels in either directions
     *
     * @param keyCode
     */
    protected void keyRepeated(int keyCode) {
        int timeDiff = (int) ((System.currentTimeMillis() - keyTimeLapse) / 100);
        increment = timeDiff;

        if (keyCode == -1 || keyCode == GameCanvas.UP_PRESSED) {
            screenYDiff = -increment;
            cursorPointerY = cursorPointerY - increment;
        } else if (keyCode == -2 || keyCode == GameCanvas.DOWN_PRESSED) {
            screenYDiff = increment;
            cursorPointerY = cursorPointerY + increment;
        } else if (keyCode == -3 || keyCode == GameCanvas.LEFT_PRESSED) {
            screenXDiff = -increment;
            cursorPointerX = cursorPointerX - increment;
        } else if (keyCode == -4 || keyCode == GameCanvas.RIGHT_PRESSED) {
            screenXDiff = increment;
            cursorPointerX = cursorPointerX + increment;
        }

        //normalize the cursor pointer position withing screen bounds
        if (cursorPointerX < 0) {
            cursorPointerX = 0;
        } else if (cursorPointerX > screenWidth) {
            cursorPointerX = screenWidth;
        }

        if (cursorPointerY < 0) {
            cursorPointerY = 0;
        } else if (cursorPointerY > screenHeight) {
            cursorPointerY = screenHeight;
        }
        refresh();
    }

    /**
     * A click has been detected, find the matching field and invoke its
     * fieldChanged listener method
     */
    public void onClick(final int x, final int y) {
        //reset the list of field matched found and get new list
        fieldCoordMatch = null;
        fieldCoordMatch = new Vector();

        //if a banner is available and the click occurs within the banner, use unadjusted y coord
        if (bannerHeight > 0 && y <= bannerHeight) {
            findFieldByCoordinates(screenManager, paintX + x, y);
        } else {
            findFieldByCoordinates(screenManager, paintX + x, paintY + y);
        }

        if (fieldCoordMatch != null && !fieldCoordMatch.isEmpty()) { //if a field was found
            if (fieldCoordMatch.size() == 1) {
                final Field tempField = (Field) fieldCoordMatch.elementAt(0);

                if (tempField.getFieldChangeListener() != null) {
                    tempField.getFieldChangeListener().fieldChanged(tempField);
                }

            } else {
                Log.out("TODO : oh oh, too many fields match. Resolve now!");
                //resolve multiple field coordinate match. Start from last added, if change listener exists, assign as match
                boolean foundFieldMatch = false;
                int fieldCount = fieldCoordMatch.size() - 1;
                while (fieldCount >= 0 && !foundFieldMatch) {
                    final Field tempField = (Field) fieldCoordMatch.elementAt(fieldCount);

                    if (tempField.getFieldChangeListener() != null) {
                        tempField.getFieldChangeListener().fieldChanged(tempField);
                        foundFieldMatch = true;
                    } else {
                        fieldCount = fieldCount - 1;
                    }
                }

                if (!foundFieldMatch) {
                    Log.out("TODO : oh oh, too many fields match. No Match found!");
                }
            }

            fieldCoordMatch = null;
        }
    }

    /**
     * Get the absolute difference between two numbers
     *
     * @param a1
     * @param a2
     */
    public int absDiff(final int a1, final int a2) {
        return Math.abs(a1 - a2);
    }
}
