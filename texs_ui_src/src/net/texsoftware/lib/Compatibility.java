/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.texsoftware.lib;

import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Graphics;

/**
 *
 * @author Jibz7
 */
public class Compatibility {

    public static boolean touchSupported = false;
    private static Compatibility INSTANCE = null;

    private Compatibility() {
        TouchChecker checkTouch = new TouchChecker();
        touchSupported = checkTouch.touchEnabled();
    }

    public static Compatibility getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new Compatibility();
        }
        return INSTANCE;
    }

    private class TouchChecker
            extends Canvas {

        /**
         * Checks wheter pointer events are supported
         */
        public boolean touchEnabled() {
            touchSupported = this.hasPointerEvents();
            return touchSupported;
        }

        protected void paint(Graphics g) {
        }
    }
}
