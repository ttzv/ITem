package com.ttzv.itmg.ad;

import com.ttzv.itmg.db.DbCon;
import com.ttzv.itmg.db.PgStatement;
import javafx.scene.control.TextField;

import java.sql.SQLException;

/**
 * Class containing all required parameters to modify a specific cell in database
 * Cell is identified by table, column and criterium specific to each cell.
 *
 */
public class DbCell {

    private String table;
    private String column;
    private String criterium;
    private DbCon dbCon;

    /**
     * Creates a DbCell corresponding to a single cell in a single columns in a single table
     * @param table table to edit
     * @param column column to edit
     * @param criterium value that allows to determine exactly one record in database (Use primary key or other unique value)
     */
    public DbCell(String table, String column, String criterium, DbCon dbCon) {
        super();
        this.table = table;
        this.column = column;
        this.criterium = criterium;
        this.dbCon = dbCon;
    }

    public void clearCell() throws SQLException {
        String valToSetIn = column  + "=" + "DEFAULT";
        dbCon.update(table, criterium, valToSetIn );
    }

    public void update(String value) throws SQLException {
        if(value.equals("NULL")){
             clearCell();
        } else {
            String valToSetIn = column + "=" + PgStatement.apostrophied(value);
            dbCon.update(table, criterium, valToSetIn);
        }
    }


}
