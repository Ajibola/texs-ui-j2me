/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.texsoftware.navigation;

import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;

/**
 * Midlet class extends Sun's MIDlet to embed a custom method to quitApp
 *
 * @author Jibola
 */
public class Midlet extends MIDlet {

    protected void startApp() throws MIDletStateChangeException {
    }

    protected void pauseApp() {
    }

    protected void destroyApp(boolean unconditional) throws MIDletStateChangeException {
    }

    public void quitApp() {
        try {
            this.destroyApp(true);
            this.notifyDestroyed();
        } catch (MIDletStateChangeException ex) {
            ex.printStackTrace();
        }
    }
}
