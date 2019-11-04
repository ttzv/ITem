package com.ttzv.item.dao;

import com.ttzv.item.properties.Cfg;
import com.ttzv.item.pwSafe.Crypt;
import com.ttzv.item.utility.Utility;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

//Connection closes after every query or statement
public abstract class DatabaseHandler {

    private JdbcDriverSelector jdbcDriverSelector;

    public DatabaseHandler() throws SQLException {
        //temporary //todo: decouple
        Cfg.getInstance().setProperty(Cfg.DB_DRIVER, "POSTGRES");
        try {
            Cfg.getInstance().saveFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //

        JdbcDriverSelector jdbcDriverSelector = new JdbcDriverSelector(Cfg.getInstance().retrieveProp(Cfg.DB_DRIVER),
                Cfg.getInstance().retrieveProp(Cfg.DB_URL),
                Cfg.getInstance().retrieveProp(Cfg.DB_LOGIN),
                Crypt.newCrypt("dCr").read());
    }

    public boolean executeUpdate(String sql) throws SQLException {
        Connection connection = jdbcDriverSelector.createConnection();
        if (connection != null) {
            Statement statement = null;
            try {
                statement = connection.createStatement();
                statement.executeUpdate(sql);
                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
            connection.close();
        }
        return true;
    }

    public List<List<String>> executeQuery(String query) throws SQLException {
        Connection connection = jdbcDriverSelector.createConnection();
        ResultSet resultSet = null;
        Statement statement = null;
        List<List<String>> resultList = new ArrayList<>();
        List<String> innerlist = null;
        if (connection != null) {
            try {
                statement = connection.createStatement();
                resultSet = statement.executeQuery(query);
                ResultSetMetaData rsmd = resultSet.getMetaData();
                while (resultSet.next()){
                    innerlist = new ArrayList<>();
                    for (int i = 1; i <= rsmd.getColumnCount() ; i++) {
                        innerlist.add(rsmd.getColumnLabel(i) + Utility.DEFAULT_ENTITY_SEPARATOR + resultSet.getString(i));
                    }
                    resultList.add(innerlist);
                }
                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            connection.close();
        }
        return resultList;
    }


    /**
     * Generates UPDATE SQL statement on specific record in database, can update multiple cells simultaneously if multiple columns are passed to entityPairs parameter
     */
    public String updateSql (String table, List<String> entityPairs, String criterium) {
        String statement = "";
        if(!table.isEmpty() && !criterium.isEmpty() && entityPairs.size()>0){
            statement = "UPDATE " + table + " SET " ;
            int cnt = 0;
            while (cnt < entityPairs.size()){
                statement = statement.concat(entityPairs.get(cnt));
                if(entityPairs.size()>1 && cnt != entityPairs.size()-1) {
                    statement = statement.concat(",");
                } else {
                    statement = statement.concat(" ");
                }
                cnt++;
            }
            statement = statement.concat("WHERE " + criterium);
        } else {
            System.err.println("DbCon: One or more values are empty");
        }
        //System.out.println(statement);
        return statement;
    }

    public List<String> getTableColumns(String table) throws SQLException {
        Connection connection = jdbcDriverSelector.createConnection();
        List<String> columnsList = new ArrayList<>();
        String sql = "SELECT * FROM " + table + " LIMIT 1";
        try {
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(sql);
            ResultSetMetaData rsmd = rs.getMetaData();
            for (int i = 1; i <= rsmd.getColumnCount(); i++) {
                columnsList.add(rsmd.getColumnName(i));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        connection.close();
        return columnsList;
    }
}
