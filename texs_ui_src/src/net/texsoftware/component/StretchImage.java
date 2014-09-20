/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.texsoftware.component;

import java.io.IOException;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.game.Sprite;
import net.texsoftware.lib.ImageUtils;
import net.texsoftware.logger.Log;

/**
 * This class creates an image with a stretchable area based on left and top
 * padding specified. It is assumed that the image padding for all four corners
 * is identical. Theoretically the padding for each corner could be made
 * different.
 *
 * @author Jibola
 */
public class StretchImage {

    String srcImage = null;
    int width = 0;
    int height = 0;
    int leftPad = 0;
    int topPad = 0;
    Image currImage = null;

    /**
     * Create a strectchable image from a source image.
     *
     * @param srcImage original image to stretch
     * @param width width of destination image
     * @param height height of destination image
     * @param leftPad x-axis position of where left corner should start
     * @param topPad y-axis position of where top corner should start
     */
    public StretchImage(String srcImage, int width, int height, int leftPad, int topPad) {
        this.srcImage = srcImage;
        this.width = width;
        this.height = height;
        this.leftPad = leftPad;
        this.topPad = topPad;

    }

    /**
     * Create image with default color white/0xffffff and draw button on the
     * image. After drawing stretch image, change default color images to
     * transparent pixels with 00 alpha.
     *
     * @return
     */
    public Image getImage() {

        if (currImage == null) {
            try {
                currImage = Image.createImage(width, height);
                final Graphics g = currImage.getGraphics();
                final Image sourceImage = Image.createImage(srcImage);

                //draw top left and right edges
                g.drawRegion(sourceImage, 0, 0, leftPad, topPad, Sprite.TRANS_NONE, 0, 0, Graphics.TOP | Graphics.LEFT);
                g.drawRegion(sourceImage, sourceImage.getWidth() - leftPad, 0, leftPad, topPad, Sprite.TRANS_NONE, currImage.getWidth() - leftPad, 0, Graphics.TOP | Graphics.LEFT);
                //draw bottom left and right edges
                g.drawRegion(sourceImage, 0, sourceImage.getHeight() - topPad, leftPad, topPad, Sprite.TRANS_NONE, 0, currImage.getHeight() - topPad, Graphics.TOP | Graphics.LEFT);
                g.drawRegion(sourceImage, sourceImage.getWidth() - leftPad, sourceImage.getHeight() - topPad, leftPad, topPad, Sprite.TRANS_NONE, currImage.getWidth() - leftPad, currImage.getHeight() - topPad, Graphics.TOP | Graphics.LEFT);

                // the amount of space left to fill in destination image
                int xSpaceLeft = width - (leftPad * 2);
                int ySpaceLeft = height - (topPad * 2);

                //the remaining stretchable space in the source image, if it is less than void left, redraw/scale
                int xSrcWidth = sourceImage.getWidth() - (leftPad * 2);
                int ySrcHeight = sourceImage.getHeight() - (topPad * 2);

//                Log.out("ySpaceLeft -> " + ySpaceLeft);
//                Log.out("ySrcHeight -> " + ySrcHeight);

                //draw vertical voids
                Image yRedrawImg = Image.createImage(leftPad, ySrcHeight);
                yRedrawImg.getGraphics().drawRegion(sourceImage, 0, topPad, yRedrawImg.getWidth(), yRedrawImg.getHeight(), Sprite.TRANS_NONE, 0, 0, Graphics.TOP | Graphics.LEFT);

                Image tempImg = ImageUtils.scaleImage(yRedrawImg, leftPad, ySpaceLeft);
                g.drawRegion(tempImg, 0, 0, tempImg.getWidth(), tempImg.getHeight(), Sprite.TRANS_NONE, 0, topPad, Graphics.TOP | Graphics.LEFT);
                tempImg = null;
                yRedrawImg = null;

                yRedrawImg = Image.createImage(leftPad, ySrcHeight);
                yRedrawImg.getGraphics().drawRegion(sourceImage, sourceImage.getWidth() - leftPad, topPad, yRedrawImg.getWidth(), yRedrawImg.getHeight(), Sprite.TRANS_NONE, 0, 0, Graphics.TOP | Graphics.LEFT);

                tempImg = ImageUtils.scaleImage(yRedrawImg, leftPad, ySpaceLeft);
                g.drawRegion(tempImg, 0, 0, tempImg.getWidth(), tempImg.getHeight(), Sprite.TRANS_NONE, currImage.getWidth() - leftPad, topPad, Graphics.TOP | Graphics.LEFT);
                tempImg = null;
                yRedrawImg = null;

                //draw horizontal voids
                yRedrawImg = Image.createImage(xSrcWidth, topPad);
                yRedrawImg.getGraphics().drawRegion(sourceImage, leftPad, 0, yRedrawImg.getWidth(), yRedrawImg.getHeight(), Sprite.TRANS_NONE, 0, 0, Graphics.TOP | Graphics.LEFT);

                tempImg = ImageUtils.scaleImage(yRedrawImg, xSpaceLeft, topPad);
                g.drawRegion(tempImg, 0, 0, tempImg.getWidth(), tempImg.getHeight(), Sprite.TRANS_NONE, leftPad, 0, Graphics.TOP | Graphics.LEFT);
                tempImg = null;
                yRedrawImg = null;

                yRedrawImg = Image.createImage(xSrcWidth, topPad);
                yRedrawImg.getGraphics().drawRegion(sourceImage, leftPad, sourceImage.getHeight() - topPad, yRedrawImg.getWidth(), yRedrawImg.getHeight(), Sprite.TRANS_NONE, 0, 0, Graphics.TOP | Graphics.LEFT);

                tempImg = ImageUtils.scaleImage(yRedrawImg, xSpaceLeft, topPad);
                g.drawRegion(tempImg, 0, 0, tempImg.getWidth(), tempImg.getHeight(), Sprite.TRANS_NONE, leftPad, currImage.getHeight() - topPad, Graphics.TOP | Graphics.LEFT);
                tempImg = null;
                yRedrawImg = null;

                //draw middle void
                yRedrawImg = Image.createImage(xSrcWidth, ySrcHeight);
                yRedrawImg.getGraphics().drawRegion(sourceImage, leftPad, topPad, yRedrawImg.getWidth(), yRedrawImg.getHeight(), Sprite.TRANS_NONE, 0, 0, Graphics.TOP | Graphics.LEFT);

                tempImg = ImageUtils.scaleImage(yRedrawImg, xSpaceLeft, ySpaceLeft);
                g.drawRegion(tempImg, 0, 0, tempImg.getWidth(), tempImg.getHeight(), Sprite.TRANS_NONE, leftPad, topPad, Graphics.TOP | Graphics.LEFT);
                tempImg = null;
                yRedrawImg = null;

                // convert image pixels data to int array
                final int[] rgb = new int[currImage.getWidth() * currImage.getHeight()];
                currImage.getRGB(rgb, 0, currImage.getWidth(), 0, 0, currImage.getWidth(), currImage.getHeight());

                // drop alpha component (make it transparent) on pixels that are still at default color
                for (int i = 0; i < rgb.length; ++i) {
                    if (rgb[i] == 0xffffffff) {
                        rgb[i] &= 0x00ffffff;
                    }
                }

                // create a new image with the pixel data and set process alpha flag to true
                currImage = Image.createRGBImage(rgb, currImage.getWidth(), currImage.getHeight(), true);

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        return currImage;
    }
}
