package net.texsoftware.navigation;

import javax.microedition.io.ConnectionNotFoundException;
import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;

/**
 *
 * A screen hierarchy class that pushes new screens to the foreground or removes
 * screens from the foreground
 *
 * @author Jibz7
 */
public class UiApplication {

    private static UiApplication INSTANCE = null;
    Midlet midlet = null;

    private UiApplication() {
    }

    public void setMIDlet(final Midlet midlet) {
        this.midlet = midlet;
    }

    public Midlet getMidlet() {
        return midlet;
    }

    public static UiApplication getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new UiApplication();
        }
        return INSTANCE;
    }

    /**
     * simple method to display a screen on the UI
     *
     * @param screen
     */
    public void pushScreen(final Displayable screen) {
        if (midlet != null) {
            Display.getDisplay(midlet).setCurrent(screen);
        }
    }

    public void pushScreen(final Canvas screen) {
        if (midlet != null) {
            Display.getDisplay(midlet).setCurrent(screen);
        }
    }

    /**
     * This method will display the previous screen on the UI. Not currently
     * implemented
     */
    public void popScreen() {
    }

    public boolean runUrl(final String url) {
        boolean platformRequest = false;
        if (midlet != null) {
            try {
                platformRequest = midlet.platformRequest(url);
            } catch (ConnectionNotFoundException ex) {
                ex.printStackTrace();
            }
        }
        return platformRequest;
    }

    public void pause(final long time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
    }

    public void runAndWait(final Runnable runnable) {
        if (midlet != null) {
            Display.getDisplay(midlet).callSerially(runnable);
        }
    }

    public void runLater(final Runnable runnable) {
        if (midlet != null) {
            new Thread(runnable).start();
        }
    }
}
