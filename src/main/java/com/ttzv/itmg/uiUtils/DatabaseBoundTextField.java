package com.ttzv.itmg.uiUtils;

import com.ttzv.itmg.ad.DbCell;
import javafx.scene.control.TextField;

/**
 * TextField that allows to use DbCell elements and call DBCell methods form its reference
 * Before using any of DbCell methods register appropriate DbCell object to this field
 */
public class DatabaseBoundTextField extends TextField {

    private DbCell dbCell;

    public void setDbCell(DbCell dbCell) {
        this.dbCell = dbCell;
    }

    public DbCell getDbCell() {
        if(dbCell!=null) {
            return dbCell;
        } else {
            System.err.println("DatabaseBoundTextField: DbCell Object not registered, register first");
            throw new NullPointerException("DatabaseBoundTextField: DbCell Object not registered, register first");
        }
    }
}
