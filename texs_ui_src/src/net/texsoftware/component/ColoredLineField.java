package net.texsoftware.component;

import javax.microedition.lcdui.Graphics;

/**
 *
 * @author Jibz7
 */
public class ColoredLineField extends Field {

    int color = 0x000000;
    boolean center = false;

    public ColoredLineField() {
        this.setHeight(1);
        this.setWidth(Display.getInstance().getDisplayWidth());
    }

    public ColoredLineField(int color, int width, int height, boolean center) {
        try {
            this.color = color;
            this.center = center;
            this.setHeight(height);
            this.setWidth(width);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public boolean isCenter() {
        return center;
    }

    public void setCenter(boolean center) {
        this.center = center;
    }

    public void paintField(Graphics g, int x, int y) {
        g.setColor(color);

        if (center) {
            g.fillRect(x + (Display.getInstance().getDisplayWidth() - this.getWidth()) / 2, y, this.getWidth(), this.getHeight());
        } else {
            g.fillRect(x, y, this.getWidth(), this.getHeight());
        }
    }
}
