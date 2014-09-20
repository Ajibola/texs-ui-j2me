/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.texsoftware.data;

import java.util.Vector;
import javax.microedition.rms.RecordComparator;
import javax.microedition.rms.RecordEnumeration;
import javax.microedition.rms.RecordFilter;
import javax.microedition.rms.RecordStore;
import javax.microedition.rms.RecordStoreException;

/**
 *
 * @author Jibz
 */
public class DataObjectManager {

    public String recordStore = null;
    public Class objectClass = null;  

    /**
     * Create the object manager with a record store and the class of the records
     * 
     * @param recordStore
     * @param objectClass 
     */
    public DataObjectManager(String recordStore, Class objectClass) throws Exception {
        if (recordStore == null || objectClass == null) { 
            //if either parameter is null, throw an exception
            throw new Exception();
        } else {
            this.recordStore = recordStore;
            this.objectClass = objectClass;
        }
    }

    /**
     * Save the dataObject into the record store as a byte array.
     * 
     * @param dataObject
     * @return 
     */
    public boolean saveDataObject(DataObject dataObject) {
        try {
            RecordStore rs = null;
            rs = RecordStore.openRecordStore(recordStore, true);

            if (findDataObject(dataObject) == null) {
                byte[] data = dataObject.toByteArray();
                rs.addRecord(data, 0, data.length);
                return true;
            }
            rs.closeRecordStore();
            rs = null;
        } catch (RecordStoreException e) {
            e.printStackTrace();
            try {
                RecordStore.deleteRecordStore(recordStore);
            } catch (RecordStoreException re) {
                re.printStackTrace();
            }
        }
        return false;
    }
    
    /**
     * A simple helper method, which allows a manager only have one object in the record store. It simply deletes all other objects before saving
     * 
     * @param dataObject 
     */
    public void saveOnlyDataObject(DataObject dataObject) {
        deleteAllDataObjects();
        saveDataObject(dataObject);
    }
    
    /**
     * A helper method which gets the only data object in the record store. If there are more than one elements, it gets the first only.
     * 
     * @return 
     */
    public DataObject getOnlyDataObject() {
        try {
            RecordStore rs = null;
            rs = RecordStore.openRecordStore(recordStore, true);

            if (rs.getNumRecords() > 0) {
                RecordEnumeration e = rs.enumerateRecords(null, null, true);
                while (e.hasNextElement()) {
                    DataObject tempDataObject = (DataObject) objectClass.newInstance();
                    tempDataObject.fromByteArray(e.nextRecord());
                    return tempDataObject;
                }
            }
            rs.closeRecordStore();
            rs = null;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        
        return null;
    }

    /**
     * Find a data object that is equal to the dataObject param
     * 
     * @param dataObject
     * @return 
     */
    public DataObject findDataObject(DataObject dataObject) {
        boolean found = false;
        try {
            RecordStore rs = null;

            int nextRecordId = 0;

            rs = RecordStore.openRecordStore(recordStore, true);

            if (rs.getNumRecords() > 0) {
                RecordEnumeration e = rs.enumerateRecords(null, null, true);
                while (e.hasNextElement() && !found) {
                    nextRecordId = e.nextRecordId();
                    
                    DataObject tempDataObject = (DataObject) objectClass.newInstance();
                    tempDataObject.fromByteArray(rs.getRecord(nextRecordId));

                    //if all the fields in both categories are the same then delete it
                    if (tempDataObject.equals(dataObject)) {
                        found = true;
                        return tempDataObject;
                    }

                    tempDataObject = null;
                }
                e.destroy();
            } else {
            }
            rs.closeRecordStore();
            rs = null;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    /**
     * Find a data object that has its id parameter equal to the param.
     * 
     * @param dataObjectId
     * @return 
     */
    public DataObject findDataObjectById(String dataObjectId) {
        boolean found = false;

        try {
            RecordStore rs = null;

            int nextRecordId = 0;

            rs = RecordStore.openRecordStore(recordStore, true);

            if (rs.getNumRecords() > 0) {
                RecordEnumeration e = rs.enumerateRecords(null, null, true);
                while (e.hasNextElement() && !found) {
                    nextRecordId = e.nextRecordId();
                    DataObject tempDataObject = (DataObject) objectClass.newInstance();
                    tempDataObject.fromByteArray(rs.getRecord(nextRecordId));

                    //if all the fields in both categories are the same then delete it
                    if (tempDataObject.getId().equalsIgnoreCase(dataObjectId)) {
                        found = true;
                        return tempDataObject;
                    }

                    tempDataObject = null;
                }
                e.destroy();
            }
            rs.closeRecordStore();
            rs = null;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    /**
     * Get the list of all data objects saved in the record store into a vector.
     * @return 
     */
    public Vector getDataObjectList(RecordComparator compare, RecordFilter filter) {
        Vector tempDataObjects = new Vector();

        try {
            RecordStore rs = null;
            rs = RecordStore.openRecordStore(recordStore, true);

            if (rs.getNumRecords() > 0) {
                RecordEnumeration e = rs.enumerateRecords(filter, compare, true);
                while (e.hasNextElement()) {
                    DataObject tempDataObject = (DataObject) objectClass.newInstance();
                    tempDataObject.fromByteArray(e.nextRecord());
                    tempDataObjects.addElement(tempDataObject);
                    tempDataObject = null;
                }
            }
            rs.closeRecordStore();
            rs = null;
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return tempDataObjects;
    }

    /**
     * Delete the first occurrence of a dataObject from the recordStore.
     * 
     * @param dataObject
     * @return 
     */
    public boolean deleteDataObject(DataObject dataObject) {
        boolean delete = false;

        try {
            RecordStore rs = null;
            int nextRecordId = 0;

            rs = RecordStore.openRecordStore(recordStore, true);

            if (rs.getNumRecords() > 0) {
                RecordEnumeration e = rs.enumerateRecords(null, null, true);
                while (e.hasNextElement() && !delete) {
                    nextRecordId = e.nextRecordId();
                    DataObject tempDataObject = (DataObject) objectClass.newInstance();
                    tempDataObject.fromByteArray(rs.getRecord(nextRecordId));

                    //if all the fields in both categories are the same then delete it
                    if (tempDataObject.equals(dataObject)) {
                        rs.deleteRecord(nextRecordId);
                        delete = true;
                    }
                    tempDataObject = null;
                }
                e.destroy();
            }
            rs.closeRecordStore();
            rs = null;
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return delete;
    }

    /**
     * 
     * Delete each record of the record store by 
     * iterating through all the objects in the store.
     * 
     * @return 
     */
    public boolean deleteAllDataObjects() {
        try {
            RecordStore rs = null;
            rs = RecordStore.openRecordStore(recordStore, true);

            if (rs.getNumRecords() > 0) {
                RecordEnumeration e = rs.enumerateRecords(null, null, true);
                while (e.hasNextElement()) {
                    rs.deleteRecord(e.nextRecordId());
                }
            }
            rs.closeRecordStore();
            rs = null;
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }

    /**
     * Permanently delete the record store.
     */
    public void deleteRecordStore() {
        try {
            RecordStore.deleteRecordStore(recordStore);
        } catch (RecordStoreException ex) {
            ex.printStackTrace();
        }
    }
	
	public Object[] getArrayFromVector(Vector myVector) {
		int vectorSize = myVector.size();
		Object[] result = new Object[vectorSize];

		for (int i = 0; i < vectorSize; i++) {
			result[i] = myVector.elementAt(i);
		}

		return result;
	}
	
	public String[] getStringArrayFromVector(Vector myVector) {
		int vectorSize = myVector.size();
		String[] result = new String[vectorSize];

		for (int i = 0; i < vectorSize; i++) {
			result[i] = myVector.elementAt(i).toString();
		}

		return result;
	}
}
