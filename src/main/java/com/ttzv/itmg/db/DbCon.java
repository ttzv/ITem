package com.ttzv.itmg.db;

import com.ttzv.itmg.ad.LDAPParser;
import com.ttzv.itmg.ad.User;
import com.ttzv.itmg.ad.UserData;
import com.ttzv.itmg.ad.UserHolder;
import com.ttzv.itmg.properties.Cfg;
import com.ttzv.itmg.pwSafe.PHolder;
import com.ttzv.itmg.utility.Utility;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class DbCon {

    private Connection conn;
    private String dbUrl;
    private String dbUser;
    private char[] dbPass;

    private LDAPParser ldapParser;

    public DbCon(LDAPParser ldapParser) {
        this.ldapParser = ldapParser;
        this.ldapParser.queryLdap();
    }

    public DbCon(){}

    public void loadCfgCredentials(){
        this.setDbUrl(Cfg.getInstance().retrieveProp(Cfg.DB_URL));
        this.setDbUser(Cfg.getInstance().retrieveProp(Cfg.DB_LOGIN));
        this.setDbPass(PHolder.db);
    }

    public boolean initConnection() throws SQLException{
        String dbUrl = "jdbc:postgresql://" + this.dbUrl;

        Properties props = new Properties();
        props.setProperty("user", dbUser);
        props.setProperty("password", new String(dbPass));

        this.conn = DriverManager.getConnection(dbUrl, props);

        return true;
    }

    /**
     * Executes custom statements, mltiple supported
     * @param statements one or more statements
     * @throws SQLException
     */
    public void customStatement(String... statements) throws SQLException {
        Statement st = conn.createStatement();
        for (String statement : statements) {
            if(!statement.isEmpty()) {
                st.executeUpdate(statement);
            }
        }
        st.close();
    }

    public int delete(String table, String criterium) throws SQLException {
        Statement st = conn.createStatement();
        String statement = PgStatement.delete(table, criterium);
        int cnt = st.executeUpdate(statement);

        st.close();

        return cnt;
    }

    /**
     * Performs UPDATE on specific record in database, can update multiple cells simultaneously if multiple columns are passed to columsToUpdate parameter
     */
    public void update (String table, String criterium, String... columnsToUpdate) throws SQLException {
        String statement = "";
        if(!table.isEmpty() && !criterium.isEmpty() && columnsToUpdate.length>0){
            statement = "UPDATE " + table + " SET " ;
            int cnt = 0;
            while (cnt < columnsToUpdate.length){
                    statement = statement.concat(columnsToUpdate[cnt]);
                    if(columnsToUpdate.length>1 && cnt != columnsToUpdate.length-1) {
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
        customStatement(statement);
    }

    public void addGUID() throws SQLException {
        List<User> ldapusers = ldapParser.getUsersDataList();
        for (User u :
                ldapusers) {
            update("users", "userguid="+PgStatement.apostrophied(u.getUserGUID()), "userGUID="+PgStatement.apostrophied(u.getUserGUID()));
            System.out.println("Updated GUID " + u);
        }
    }

    public void ldapToDb() throws SQLException {
        Statement st = conn.createStatement();

        List<User> ldapusers = ldapParser.getUsersDataList();
        String[] columns = User.columns;
        for (User u : ldapusers) {
            //String qpart ="(" +"'"+u.getSamAccountName()+"'" +","+ "'"+u.getGivenName()+"'" +","+ "'"+u.getSn()+"'" +","+ "'"+u.getDisplayName()+"'" +","+ "'"+u.getUserAccountControl()+"'"+")";
            String statement = PgStatement.insert("users", columns, PgStatement.apostrophied(u.getComplete())); //hopefully this works in the same way
            st.executeUpdate(statement);
        }

        st.close();
    }

    /**
     * Checks if given value exists in selected column
     * @param table table where search will be performed
     * @param column column to search into
     * @param value value to search for
     * @return true if value exists, otherwise false
     * @throws SQLException when connection with database could not be established or query was invalid
     */
    public boolean exists(String table, String column, String value) throws SQLException {
        Statement st = conn.createStatement();

        String exists = PgStatement.exists(table,column,value);

        ResultSet resultSet = st.executeQuery(exists);
        resultSet.next();

        boolean result = resultSet.getBoolean(1);
        resultSet.close();
        return result;
    }


    /*public ResultSet select(String identifier) throws SQLException {
        Statement st = conn.createStatement();

        ResultSet resultSet = st.executeQuery(PgStatement.select("users", "*", "userguid=" + identifier));

     }*/

    /**
     * Updates table USERS with new values from LDAP
     * @return List of users that were added to a database during update execution
     */
    public List<User> updateUsersTable() throws SQLException {
        String table = "users";
        String column = "userguid";
        Statement st = conn.createStatement();

        List<User> ldapusers = ldapParser.getUsersDataList();
        List<User> newUsers = new ArrayList<>();

        //addGUID();

        for(User user : ldapusers){
            boolean exists = exists(table, column, user.getUserGUID());
            if(!exists) {

                String query = PgStatement.insert(table, User.insertColumns, PgStatement.apostrophied(user.getComplete()));
                System.out.println(query);
                st.executeUpdate(query);
                newUsers.add(user);
            }
        }
        st.close();
        return newUsers;
    }

    /**
     * @param count number of users to retrieve from database
     * @return true if method performed correctly
     * @throws SQLException database connection not estabilished
     */
    public boolean getNewUsers(int count) throws SQLException {
        UserHolder.clear();
        Statement st = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        ResultSet resultSet = st.executeQuery(PgStatement.selectAscending("users,city", "*","users.city=city.id", "whencreated", false) + "limit " + count);
        while(resultSet.next()){
            User user = buildUserFromDB(resultSet);
            /*String[] resultUserData = new String[User.columns.length];
            for (int i = 0; i <= User.columns.length - 1 ; i++) {
                resultUserData[i] = resultSet.getString(User.columns[i]);
            }
            User user = new User(resultUserData);*/
            UserHolder.addUser(user);
        }
        //System.out.println(resultSet.getRow());
        resultSet.close();
        return true;
    }

    public User buildUserFromDB(ResultSet resultSet) throws SQLException {
        String[] resultUserData = new String[User.columns.length];
        for (int i = 0; i <= User.columns.length - 1 ; i++) {
            resultUserData[i] = resultSet.getString(User.columns[i]);
        }
        return new User(resultUserData);
    }

    /**
     * Performs search in Users table for inputted value
     * @param value value to search for in database
     * @param limiter maximum amount of rows returned by query, no limit of set to 0
     * @return list of Users with fitting value
     */
    //todo: make search case insensitive and support substring searches
    public List<User> globalSearch(String value, int limiter) throws SQLException {
        if(Utility.restrictedSymbols.contains(value)){
            value="";
        }

        ArrayList<User> foundUsers = new ArrayList<>();

        if(!value.isEmpty()) {
            StringBuilder allSearchCriterium = new StringBuilder("(");
            for (int i = 0; i < User.columns.length; i++) {
                if (i < User.columns.length - 1) {
                    allSearchCriterium.append(" ").append(User.columns[i]).append(" ILIKE ").append(PgStatement.apostrophied("%" + value + "%")).append(" OR");
                } else {
                    allSearchCriterium.append(" ").append(User.columns[i]).append(" ILIKE ").append(PgStatement.apostrophied("%" + value + "%")).append(")");
                }
            }

            allSearchCriterium.append(" AND (users.city=city.id)");

            String query = PgStatement.select("users,city", "*", allSearchCriterium.toString()) + " limit " + limiter;
            if (limiter == 0) {
                query = PgStatement.select("users,city", "*", allSearchCriterium.toString());
            }

            //System.out.println(query);
            Statement st = conn.createStatement();
            ResultSet resultSet = st.executeQuery(query);


            while (resultSet.next()) {
                String[] resultUserData = new String[User.columns.length];
                for (int i = 0; i <= User.columns.length - 1; i++) {
                    resultUserData[i] = resultSet.getString(User.columns[i]);
                }
                User user = new User(resultUserData);
                foundUsers.add(user);
            }

            st.close();
        }

        return foundUsers;
    }

    public User reloadUser (User user) throws SQLException {
        return globalSearch(user.getUserGUID(), 1).get(0);
    }



    public String getDbUrl() {
        return dbUrl;
    }

    public void setDbUrl(String dbUrl) {
        this.dbUrl = dbUrl;
    }

    public String getDbUser() {
        return dbUser;
    }

    public void setDbUser(String dbUser) {
        this.dbUser = dbUser;
    }

    public char[] getDbPass() {
        return dbPass;
    }

    public void setDbPass(char[] dbPass) {
        this.dbPass = dbPass;
    }
}