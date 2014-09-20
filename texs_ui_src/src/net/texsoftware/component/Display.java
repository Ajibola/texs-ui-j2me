package net.texsoftware.component;

import net.texsoftware.navigation.UiApplication;
import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Graphics;
import net.texsoftware.lib.Compatibility;
import net.texsoftware.logger.Log;

/**
 *
 * This class contains the width and hieght dimensions for the phone screen to
 * the highest possible accuracy. It is only called once for each execution via
 * a singleton class.
 *
 * @author Jibz7
 */
public class Display {

    private static int displayWidth = 1;
    private static int displayHeight = 1;
    private static int phoneWidth = 1;
    private static int phoneHeight = 1;
    private static Display INSTANCE = null;

    private Display() {
        try {
            final DisplayCanvas display = new DisplayCanvas();
            UiApplication.getInstance().pushScreen(display);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static Display getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new Display();
        }
        return INSTANCE;
    }

    public int getDisplayWidth() {
        return displayWidth;
    }

    public int getDisplayHeight() {
        return displayHeight;
    }

    class DisplayCanvas extends Canvas {

        protected void paint(Graphics g) {
            this.setFullScreenMode(true);

            displayWidth = g.getClipWidth();
            displayHeight = g.getClipHeight();
            Log.out("Height is : "+displayHeight);

            phoneWidth = displayWidth;
            phoneHeight = displayHeight;
            g.fillRect(0, 0, displayWidth, displayHeight);
        }

        protected void pointerPressed(int x, int y) {
        }
    }
}
