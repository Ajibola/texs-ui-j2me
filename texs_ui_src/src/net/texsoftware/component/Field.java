package net.texsoftware.component;

import javax.microedition.lcdui.Graphics;
import net.texsoftware.logger.Log;

/**
 *
 * @author Jibz7
 */
public class Field {

    private int xPos = 0;
    private int yPos = 0;
    public boolean added = false; //use this flag to indicate if a field has already been added    
    public boolean painted = false;
    //paddings add to the height of the lement
    private int paddingLeft = 0;
    private int paddingRight = 0;
    private int paddingTop = 0;
    private int paddingBottom = 0;
    //margins does not affect the height of the element, but it affect the starting position
    private int marginLeft = 0;
    private int marginRight = 0;
    private int marginTop = 0;
    private int marginBottom = 0;
    private int width = 0;
    private int height = 0;
    FieldChangeListener fieldChangeListener = null;
    Graphics graphics = null;

    public Field() {
    }

    public int getPreferredWidth() {
        return width + paddingLeft + paddingRight;
    }

    public int getPreferredHeight() {
        return height + paddingTop + paddingBottom;
    }

    protected int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    protected int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getxPos() {
        return xPos;
    }

    /**
     * returns the last position on the x axis
     *
     * @return
     */
    public int getxPosLast() {
        return xPos + getPreferredWidth();
    }

    public void setxPos(int xPos) {
        this.xPos = xPos;
    }

    public int getyPos() {
        return yPos;
    }

    /**
     * returns the last position of y on the y axis
     *
     * @return
     */
    public int getyPosLast() {
        return yPos + getPreferredHeight();
    }

    public void setyPos(int yPos) {
        this.yPos = yPos;
    }

    public int getPaddingLeft() {
        return paddingLeft;
    }

    public void setPaddingLeft(int paddingLeft) {
        this.paddingLeft = paddingLeft;
    }

    public int getPaddingRight() {
        return paddingRight;
    }

    public void setPaddingRight(int paddingRight) {
        this.paddingRight = paddingRight;
    }

    public int getPaddingTop() {
        return paddingTop;
    }

    public void setPaddingTop(int paddingTop) {
        this.paddingTop = paddingTop;
    }

    public int getPaddingBottom() {
        return paddingBottom;
    }

    public void setPaddingBottom(int paddingBottom) {
        this.paddingBottom = paddingBottom;
    }

    public int getMarginLeft() {
        return marginLeft;
    }

    public void setMarginLeft(int marginLeft) {
        this.marginLeft = marginLeft;
    }

    public int getMarginRight() {
        return marginRight;
    }

    public void setMarginRight(int marginRight) {
        this.marginRight = marginRight;
    }

    public int getMarginTop() {
        return marginTop;
    }

    public void setMarginTop(int marginTop) {
        this.marginTop = marginTop;
    }

    public int getMarginBottom() {
        return marginBottom;
    }

    public void setMarginBottom(int marginBottom) {
        this.marginBottom = marginBottom;
    }

    public void setGraphics(final Graphics g) {
        graphics = null;
        graphics = g;
    }

    public void paint(Graphics g, int x, int y) {
        if (graphics == null) {
            graphics = g;
            xPos = x;
            yPos = y;
        }
        painted = true;
        paintField(g, x + getMarginLeft() + getPaddingLeft(), y + getMarginTop() + getPaddingTop());
    }

    public void paintField(Graphics g, int x, int y) {
    }

    public boolean coordinatesMatch(final int x, final int y) {
        if ((x > xPos && x < (xPos + getPreferredWidth())) && (y > yPos && y < (yPos + getPreferredHeight()))) {
            return true;
        } else {
            return false;
        }
    }

    public void invalidate() {
        paint(graphics, this.getxPos(), this.getyPos());
    }

    public void setFieldChangeListener(FieldChangeListener fieldChangeListener) {
        this.fieldChangeListener = fieldChangeListener;
    }

    public FieldChangeListener getFieldChangeListener() {
        return this.fieldChangeListener;
    }
}
