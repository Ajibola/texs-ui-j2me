/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.texsoftware.layouts;

import net.texsoftware.component.Display;
import net.texsoftware.component.Field;

/**
 *
 * @author Jibz7
 */
public class GridFieldManager extends Manager {

    /**
     * Add a new field to the current manager and change the height and width of
     * the manager to reflect the field dimensions.
     *
     * @param field
     */
    public void addFieldElement(Field field) {
        managerFields.addElement(field);
        setAdjustedSize(field);
    }

    /**
     * Add a manager to the list of fields for the current manager. Set the
     * starting X and Y positions of the new manager relative to the current
     * positions of the existing manager
     *
     * @param manager
     */
    public void addManagerElement(Manager manager) {
        managerFields.addElement(manager);
        setAdjustedSize(manager);
    }

    /**
     * adjusted size increments the height of a vertical field manager, while
     * the width is the MAX width of any component
     *
     * @param field
     */
    private void setAdjustedSize(Field field) {

        //update field starting yPos with initial height

        field.setxPos(width);
        field.setyPos(height);

        width = width + field.getPreferredWidth();

        if (width > Display.getInstance().getDisplayWidth()) {
            width = 0;
            height = height + field.getPreferredHeight();
        }
    }

    private void setAdjustedSize(Manager manager) {
        manager.setxPos(width);
        manager.setyPos(height);

        width = width + manager.getWidth();

        if (width > Display.getInstance().getDisplayWidth()) {
            width = 0;
            height = height + manager.getHeight();
        }
    }
}
