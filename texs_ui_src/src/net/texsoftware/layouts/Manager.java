package net.texsoftware.layouts;

import java.util.Vector;
import javax.microedition.lcdui.Graphics;
import net.texsoftware.component.Field;
import net.texsoftware.exception.ComponentExistsException;

/**
 *
 * @author Jibz7
 */
public abstract class Manager {

    Vector managerFields = new Vector();
    int width = 0;
    int height = 0;
    public boolean added = false; //use this flag to indicate if a manager has already been added   
    private int xPos = 0;
    private int yPos = 0;

    /**
     * Add a field to the current manager. Field is only allowed to be added
     * once, after which a ComponentExistsException will be thrown.
     *
     * @param field
     */
    public void addField(Field field) {
        if (!field.added) {
            field.added = true;
            addFieldElement(field);

        } else {
            try {
                throw new ComponentExistsException();
            } catch (ComponentExistsException ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
     * Add a manager to the current manager, all managers added to the screen
     * must pass through this method. If manager already added previously, throw
     * ComponentExistsException().
     *
     * @param manager
     */
    public void addManager(Manager manager) {
        if (!manager.added) {
            manager.added = true;
            //set the x & y positions of the new manager from current x&y pos of current manager
            manager.setxPos(this.getxPos());
            manager.setyPos(this.getyPos());
            addManagerElement(manager);
        } else {
            try {
                throw new ComponentExistsException();
            } catch (ComponentExistsException ex) {
                ex.printStackTrace();
            }
        }
    }

    public void addFieldElement(Field field) {
        addFieldElement(field);
    }

    public void addManagerElement(Manager manager) {
        addManagerElement(manager);
    }

    public void delete(Field field) {
        for (int i = 0; i < managerFields.size(); i++) {
            if (managerFields.elementAt(i) == field) {
                managerFields.removeElementAt(i);
                field.added = false;
                height = height - field.getPreferredHeight();
                yPos = yPos - field.getPreferredHeight();
            }
        }
    }

    public void delete(Manager manager) {
        for (int i = 0; i < managerFields.size(); i++) {
            if (managerFields.elementAt(i) == manager) {
                managerFields.removeElementAt(i);
                manager.added = false;
                height = height - manager.getHeight();
                yPos = yPos - manager.getHeight();
            }
        }
    }

    /**
     * Update each field and manager with the reference of the current screen
     * graphics object
     *
     * @param g
     */
    public void setGraphics(final Graphics g) {
        for (int i = 0; i < managerFields.size(); i++) {
            if (managerFields.elementAt(i) instanceof Field) {
                final Field f = (Field) managerFields.elementAt(i);
                f.setGraphics(g);
            } else if (managerFields.elementAt(i) instanceof Manager) {
                final Manager m = (Manager) managerFields.elementAt(i);
                m.setGraphics(g);
            }
        }
    }

    /**
     * Refresh the components and size in this manager
     */
    public void invalidate() {
        for (int i = 0; i < managerFields.size(); i++) {
            if (managerFields.elementAt(i) instanceof Field) {
                final Field f = (Field) managerFields.elementAt(i);
                f.invalidate();
            } else if (managerFields.elementAt(i) instanceof Manager) {
                final Manager m = (Manager) managerFields.elementAt(i);
                m.invalidate();
            }
        }
    }

    public int getxPos() {
        return xPos;
    }

    public void setxPos(int xPos) {
        this.xPos = xPos;
    }

    public int getyPos() {
        return yPos;
    }

    public void setyPos(int yPos) {
        this.yPos = yPos;
    }

    /**
     * adjust the width & height of the manager relative to the recently added
     * field
     */
    private void setAdjustedSize(Field field) {
    }

    /**
     * adjust the width & height of the manager relative to the recently added
     * manager
     */
    private void setAdjustedSize(Manager manager) {
    }

    public Vector getFields() {
        return managerFields;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int currWidth) {
        this.width = currWidth;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int currHeight) {
        this.height = currHeight;
    }
}
