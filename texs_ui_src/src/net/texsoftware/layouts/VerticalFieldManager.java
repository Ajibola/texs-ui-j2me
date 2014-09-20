/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.texsoftware.layouts;

import net.texsoftware.component.Field;
import net.texsoftware.logger.Log;

/**
 *
 * @author Jibz7
 */
public class VerticalFieldManager extends Manager {

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
        //get max width of field and set xPos to 0 for VFM
        if (width < field.getxPosLast()) {
            width = field.getxPosLast();
        }

        //update field starting yPos with initial height
        field.setxPos(this.getxPos());
        field.setyPos(height);
        //update manager with new height
        height = height + field.getPreferredHeight();
    }

    private void setAdjustedSize(Manager manager) {
        //get max width of field and set xPos to 0 for VFM
        if (width < manager.getWidth()) {
            width = manager.getWidth();
        }

        manager.setxPos(this.getxPos());
        manager.setyPos(height);
        height = height + manager.getHeight();
    }
}
