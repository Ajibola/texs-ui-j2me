package net.texsoftware.component;

import java.io.IOException;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

/**
 *
 * @author Jibz7
 */
public class BitmapField extends Field {

    Image image = null;
    boolean center = false;

    public BitmapField(String imageStr) {
        try {
            image = Image.createImage(imageStr);
            this.setHeight(image.getHeight());
            this.setWidth(image.getWidth());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public BitmapField(final Image image) {
        try {
            this.image = image;
            this.setHeight(image.getHeight());
            this.setWidth(image.getWidth());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public boolean isCenter() {
        return center;
    }

    public void setCenter(boolean center) {
        this.center = center;
    }

    public void paintField(Graphics g, int x, int y) {
        if (center) {
            g.drawImage(image, (x + (Display.getInstance().getDisplayWidth() - image.getWidth()) / 2), y, 0);
        } else {
            g.drawImage(image, x, y, 0);
        }
    }
}
