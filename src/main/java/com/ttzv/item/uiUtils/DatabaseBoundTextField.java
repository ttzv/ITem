package com.ttzv.item.uiUtils;

import javafx.scene.control.TextField;

/**
 * TextField that allows to use DbCell elements and call DBCell methods from its reference
 * Before using any of DbCell methods register appropriate DbCell object to this field
 */
public class DatabaseBoundTextField<T> extends TextField {

    private T dbCell;

    public void setDbCell(T dbCell) {
        this.dbCell = dbCell;
    }

    public T getDbCell() {
        if(dbCell!=null) {
            return dbCell;
        } else {
            System.err.println("DatabaseBoundTextField: DbCell Object not registered, register first by using setDbCell() method");
            throw new NullPointerException("DatabaseBoundTextField: DbCell Object not registered, register first");
        }
    }

}
