/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.texsoftware.component;

import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import net.texsoftware.lib.Util;
import net.texsoftware.logger.Log;

/**
 * Generate an image with alpha from the parameters sent
 *
 * @author Jibola
 */
public class AlphaImage {

    int width = 0;
    int height = 0;
    int alphaColor = 0;
    Image currImage = null;

    /**
     * Create an image with alpha.
     *
     */
    public AlphaImage(int width, int height, int alphaColor) {
        this.width = width;
        this.height = height;
        this.alphaColor = alphaColor;
    }

    public Image getImage() {

        if (currImage == null) {
            currImage = Image.createImage(width, height);

            Graphics g = currImage.getGraphics();

            g.setColor(alphaColor);
            g.fillRect(0, 0, width, height);

            // convert image pixels data to int array
            final int[] rgb = new int[currImage.getWidth() * currImage.getHeight()];
            currImage.getRGB(rgb, 0, currImage.getWidth(), 0, 0, currImage.getWidth(), currImage.getHeight());

            // drop alpha component (make it transparent) on pixels that are still at default color
            for (int i = 0; i < rgb.length; ++i) {
                rgb[i] &= alphaColor;
            }

            // create a new image with the pixel data and set process alpha flag to true
            currImage = Image.createRGBImage(rgb, currImage.getWidth(), currImage.getHeight(), true);

        }

        return currImage;
    }

    public static Image makeTransparent(Image image, int color) {
        // convert image pixels data to int array
        final int[] rgb = new int[image.getWidth() * image.getHeight()];
        image.getRGB(rgb, 0, image.getWidth(), 0, 0, image.getWidth(), image.getHeight());

        // drop alpha component (make it transparent) on pixels that are still at default color
        for (int i = 0; i < rgb.length; ++i) {
            rgb[i] &= color;
        }

        // create a new image with the pixel data and set process alpha flag to true
        image = Image.createRGBImage(rgb, image.getWidth(), image.getHeight(), true);
        
        return image;
    }
}
