/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.texsoftware.component;

import java.util.Vector;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import net.texsoftware.lib.StringUtils;
import net.texsoftware.logger.Log;

/**
 *
 * @author Jibola
 */
public class LabelField extends Field {

    String label = "";
    Font font = Font.getDefaultFont();
    int color = 0x000000;
    Vector lines = null;
    boolean center = false;
    int displayWidth = Display.getInstance().getDisplayWidth();

    public LabelField(String label) {
        this.label = label;
        this.setWidth(displayWidth - (this.getxPos() + getMarginLeft() + getPaddingLeft()));

        String[] newlineArray = StringUtils.split(label, "\n");

        if (newlineArray.length > 0) {
            lines = StringUtils.splitToLines(newlineArray[0], font, this.getPreferredWidth());

            if (newlineArray.length > 1) {
                for (int i = 1; i < newlineArray.length; i++) {
                    StringUtils.appendSplitToLines(lines, newlineArray[i], font, this.getPreferredWidth());
                }
            }
        }

        newlineArray = null;

        int fieldHeight = ((lines.size()) * font.getHeight());
        this.setHeight(fieldHeight);
    }

    public LabelField(String label, int displayWidth) {
        this.label = label;
        this.setWidth(displayWidth - (getMarginLeft() + getPaddingLeft()));

        String[] newlineArray = StringUtils.split(label, "\n");

        if (newlineArray.length > 0) {
            lines = StringUtils.splitToLines(newlineArray[0], font, this.getPreferredWidth());

            if (newlineArray.length > 1) {
                for (int i = 1; i < newlineArray.length; i++) {
//                    lines.addElement(" ");
                    StringUtils.appendSplitToLines(lines, newlineArray[i], font, this.getPreferredWidth());
                }
            }
        }

        newlineArray = null;

        int fieldHeight = ((lines.size()) * font.getHeight());
        this.setHeight(fieldHeight);
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

    public boolean isCenter() {
        return center;
    }

    public void setCenter(boolean center) {
        this.center = center;
    }

    public void paintField(Graphics g, int x, int y) {
        g.setColor(color);
        g.setFont(font);

        if (lines != null && lines.size() == 1) {
            if (center) {
                g.drawString(label, x + ((displayWidth - font.stringWidth(label)) / 2), y, Graphics.TOP | Graphics.LEFT);
            } else {
                g.drawString(label, x, y, Graphics.TOP | Graphics.LEFT);
            }
        } else if (lines != null && lines.size() > 1) {
            // label extends past one line
            int tempY = 0;
            for (int i = 0; i < lines.size(); i++) {
                tempY = y + (i * g.getFont().getHeight());
                String tempStr = lines.elementAt(i).toString();
                if (center) {
                    g.drawString(tempStr, x + ((displayWidth - font.stringWidth(tempStr)) / 2), tempY, 0);
                } else {
                    g.drawString(tempStr, x, tempY, 0);
                }
                tempStr = null;
            }
            //add last row drawn
            tempY = tempY + font.getHeight();
        }
    }
}
