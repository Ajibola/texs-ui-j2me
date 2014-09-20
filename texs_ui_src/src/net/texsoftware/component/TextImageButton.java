package net.texsoftware.component;

import java.io.IOException;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import net.texsoftware.component.Display;
import net.texsoftware.component.Field;
import net.texsoftware.lib.StringUtils;

/**
 *
 * @author Jibola
 */
public class TextImageButton extends Field {

    String text = "";
    Image image = null;
    String imageStr = null;
    Font font = Font.getDefaultFont();
    int color = 0xffffff;
    int width = 0;
    int height = 0;
    boolean center = false;

    public TextImageButton(String text, String imageStr, int color) {
        this.text = text;
        this.color = color;
        this.width = Display.getInstance().getDisplayWidth() - 40;
        this.height = font.getHeight() + 10;
        this.imageStr = imageStr;
        try {
            image = Image.createImage(imageStr);
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        this.setHeight(height);
        this.setWidth(width);
    }

    public TextImageButton(String text, Image image, int color) {
        this.text = text;
        this.color = color;
        this.width = image.getWidth();
        this.height = image.getHeight();
        this.image = image;

        this.setHeight(height);
        this.setWidth(width);
    }

    public TextImageButton(String text, String imageStr, int color, int width, int height) {
        this.text = text;
        this.color = color;
        this.width = width;
        this.height = height;
        this.imageStr = imageStr;

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
        text = StringUtils.truncate(text, font, width - 10);

        if (center) {
            g.drawImage(image, (x + (Display.getInstance().getDisplayWidth() - image.getWidth()) / 2), y, 0);
            g.drawString(text, (x + (Display.getInstance().getDisplayWidth() - image.getWidth()) / 2) + ((image.getWidth() - font.stringWidth(text)) / 2), y + (image.getHeight() - g.getFont().getHeight()) / 2, Graphics.TOP | Graphics.LEFT);
        } else {
            g.drawImage(image, x, y, 0);
            g.drawString(text, x + (image.getWidth() - font.stringWidth(text)) / 2, y + (image.getHeight() - g.getFont().getHeight()) / 2, Graphics.TOP | Graphics.LEFT);
        }
    }
}
