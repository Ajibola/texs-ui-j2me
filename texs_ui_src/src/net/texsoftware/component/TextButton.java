package net.texsoftware.component;

import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import net.texsoftware.lib.StringUtils;

/**
 *
 * @author Jibola
 */
public class TextButton extends Field {

    String text = "";
    Font font = Font.getDefaultFont();
    int color = 0xffffff;
    int width = 0;
    int height = 0;
    int leftMargin = 10;
    boolean center = false;

    public TextButton(String text, int color) {
        this.text = text;
        this.color = color;
        this.width = Display.getInstance().getDisplayWidth() - 40;
        this.height = font.getHeight() + 10;

        this.setHeight(height);
        this.setWidth(width);
    }

    public TextButton(String text, int color, int width, int height, int leftMargin) {
        this.text = text;
        this.color = color;
        this.width = width;
        this.height = height;
        this.leftMargin = leftMargin;

        this.setHeight(height);
        this.setWidth(width);
    }

    public boolean isCenter() {
        return center;
    }

    public void setCenter(boolean center) {
        this.center = center;
    }

    public Font getFont() {
        return font;
    }

    public void setFont(Font font) {
        this.font = font;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public void paintField(Graphics g, int x, int y) {

        g.setColor(color);
        g.setFont(font);

        //truncate the text to fit inside the button
        text = StringUtils.truncate(text, font, width - leftMargin);

        if (center) {          
            g.drawString(text, x + (width - font.stringWidth(text)) / 2, y + (height - g.getFont().getHeight()) / 2, Graphics.TOP | Graphics.LEFT);
        }
        else {
            g.drawString(text, x + leftMargin, y + (height - g.getFont().getHeight()) / 2, Graphics.TOP | Graphics.LEFT);
        }
    }
}
