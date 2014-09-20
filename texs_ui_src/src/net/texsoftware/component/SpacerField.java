package net.texsoftware.component;

import javax.microedition.lcdui.Graphics;

/**
 *
 * @author Jibz7
 */
public class SpacerField extends Field {

    public SpacerField(int width, int height) {
        try {
            this.setHeight(height);
            this.setWidth(width);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void paintField(Graphics g, int x, int y) {
    }
}
