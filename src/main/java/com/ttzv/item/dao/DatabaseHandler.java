package com.ttzv.item.dao;

import com.ttzv.item.entity.DynamicEntity;
import com.ttzv.item.entity.KeyMapper;
import com.ttzv.item.properties.Cfg;
import com.ttzv.item.pwSafe.Crypt;
import com.ttzv.item.utility.Utility;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

//Connection closes after every query or statement
public abstract class DatabaseHandler {

    private JdbcDriverSelector jdbcDriverSelector;

    public DatabaseHandler() throws SQLException, IOException, GeneralSecurityException {
        //temporary //todo: decouple
        Cfg.getInstance().setProperty(Cfg.DB_DRIVER, "POSTGRES");
        try {
            Cfg.getInstance().saveFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //
        jdbcDriverSelector = new JdbcDriverSelector(Cfg.getInstance().retrieveProp(Cfg.DB_DRIVER),
                Cfg.getInstance().retrieveProp(Cfg.DB_URL),
                Cfg.getInstance().retrieveProp(Cfg.DB_LOGIN),
                Crypt.newCrypt("dCr").read());
    }

    public abstract void createTables() throws SQLException;

    public Connection getConnection() throws SQLException {
        return jdbcDriverSelector.createConnection();
    }

    public boolean tablesReady(String tableName) throws SQLException {
        Connection connection = getConnection();
        DatabaseMetaData dbmd = connection.getMetaData();
        ResultSet rs = dbmd.getTables(null, null, "%", null);
        List<String> tables = new ArrayList<>();
        while (rs.next()){
            tables.add(rs.getString(3));
        }
        rs.close();
        connection.close();
        return tables.contains(tableName);

    }

    public boolean tableReady(String tableName) throws SQLException { //todo: unfinished
        String query = "SELECT\n" +
                "   *\n" +
                "FROM\n" +
                "   pg_catalog.pg_tables\n" +
                "WHERE\n" +
                "   schemaname != 'pg_catalog'\n" +
                "AND schemaname != 'information_schema';";
        String tableNameKey = "tablename";
        List<List<String>> resultsList = executeQuery(query);
        for (List<String> result : resultsList) {
            System.out.println(result.get(1));
        }
        return false;
    }

    public boolean executeUpdate(PreparedStatement preparedStatement) throws SQLException {
        System.out.println("UPDATE: " + preparedStatement.toString());
        int changedRows = preparedStatement.executeUpdate();
        preparedStatement.close();
        return changedRows > 0;
    }

    public List<List<String>> executeQuery(String query) throws SQLException {
        System.out.println("QUERY: " + query);
        Connection connection = getConnection();
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
                        String colLabel = rsmd.getColumnLabel(i);
                        if(!colLabel.equals("id")) //Do not pull primary key to results list todo: move to configuration file
                        {
                            String val = resultSet.getString(i);
                            if(val == null) val = "";
                            innerlist.add(colLabel + Utility.DEFAULT_ENTITY_SEPARATOR + val);
                        }
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
     * Generates UPDATE SQL statement on specific record in database, can update multiple cells simultaneously if multiple columns are passed to list parameter
     */
    public String listToString(List<String> list) { //todo move to utility class
        return list.toString().replaceAll("\\[|\\]","");
    }

    public List<String> getTableColumns(String table) throws SQLException {
        Connection connection = getConnection();
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

    boolean update (String table, KeyMapper keyMapper, DynamicEntity entity, String uniqueID) throws SQLException {
        String sql = "UPDATE " + table + " SET ";
        StringBuilder statement = new StringBuilder(sql);
        List<String> dbKeys = keyMapper.getAllMappingsOf(KeyMapper.DBKEY);
        for (String dbKey : dbKeys) {
            statement.append(dbKey).append("=?,");
        }
        //remove last comma from string
        statement.deleteCharAt(statement.length() - 1);
        statement.append(" WHERE ").append(uniqueID).append("=?;");
        PreparedStatement preparedStatement = getConnection().prepareStatement(statement.toString());
        int currentIndex = 1;
        for (String dbKey : dbKeys) {
            preparedStatement.setString(currentIndex, entity.getValue(dbKey));
            currentIndex++;
        }
        preparedStatement.setString(currentIndex, entity.getValue(uniqueID));
        return executeUpdate(preparedStatement);
    }


    /**
     * Convenience method used for building parametrized PostgreSQL INSERT statement
     * @param table - name of table where data will be inserted
     * @param columns - list of column names where data will be inserted
     * @return build PostgreSQL INSERT statement ready for query
     */ //todo: optimize sql - generate columns once with multiple value lists
    String insertSql(String table, List<String> columns){ //todo move to utility class
        String insertStatement = "INSERT INTO " + table + " ";
        StringBuilder sb = new StringBuilder(insertStatement);
        //building column data
        sb.append("(");
        for (String s : columns){
            sb.append(s);
            sb.append(",");
        }
        sb.deleteCharAt(sb.length()-1);
        sb.append(") ");
        sb.append("\nVALUES (");
        for (int i = 0; i < columns.size(); i++) {
            sb.append("?");
            sb.append(",");
            }
        sb.deleteCharAt(sb.length() - 1);
        sb.append(");\n");
        return sb.toString();
    }

    protected void insert(String table, KeyMapper keyMapper, DynamicEntity entity) throws SQLException {
        List<String> dbKeys = keyMapper.getAllMappingsOf(KeyMapper.DBKEY);
        String sql = insertSql(table, keyMapper.getAllMappingsOf(KeyMapper.DBKEY));
        PreparedStatement preparedStatement = getConnection().prepareStatement(sql);
        int currentIndex = 1;
        for (String dbKey : dbKeys) {
            preparedStatement.setString(currentIndex, entity.getValue(dbKey));
            currentIndex++;
        }
        executeUpdate(preparedStatement);
    }
}
