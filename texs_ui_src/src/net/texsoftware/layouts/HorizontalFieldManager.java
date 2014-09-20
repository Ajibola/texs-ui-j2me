/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.texsoftware.layouts;

import net.texsoftware.component.Field;

/**
 *
 * @author Jibz7
 */
public class HorizontalFieldManager extends Manager {

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

    public void addManagerElement(Manager manager) {
        managerFields.addElement(manager);
        setAdjustedSize(manager);
    }

    /**
     * adjusted size increments the width of a vertical field manager, while the
     * height is the MAX height of any component
     *
     * @param field
     */
    public void setAdjustedSize(Field field) {

        //get max height of field relative to the screen and set yPos of manager to zero
        if (height < (field.getPreferredHeight())) {
            height = (field.getPreferredHeight());
            field.setyPos(0);
        }

        field.setxPos(width);
        field.setyPos(this.getyPos());
        width = width + field.getPreferredWidth();
    }

    public void setAdjustedSize(Manager manager) {

        //get max height of field relative to the screen and set yPos of manager to zero
        if (height < (manager.getHeight())) {
            height = manager.getHeight();
        }

        manager.setxPos(width);
        manager.setyPos(this.getyPos());
        width = width + manager.getWidth();
    }
}
