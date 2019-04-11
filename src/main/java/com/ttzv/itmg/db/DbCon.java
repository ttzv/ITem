package com.ttzv.itmg.db;

import com.ttzv.itmg.ad.LDAPParser;
import com.ttzv.itmg.ad.User;
import com.ttzv.itmg.ad.UserHolder;
import com.ttzv.itmg.properties.Cfg;
import com.ttzv.itmg.pwSafe.PHolder;

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
        for (String query : statements) {
            if(!query.isEmpty()) {
                st.executeUpdate(query);
            }
        }
        st.close();
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
     * Checks if given value exists in table
     * @param table table where search will be performed
     * @param column column to search into
     * @param value value to search for
     * @return true if value exists, otherwise false
     * @throws SQLException when connection with database could not be estabilished or query was invalid
     */
    public boolean existsInDB(String table, String column, String value) throws SQLException {
        Statement st = conn.createStatement();

        String exists = PgStatement.exists(table,column,value);

        ResultSet resultSet = st.executeQuery(exists);
        resultSet.next();

        boolean result = resultSet.getBoolean(1);
        resultSet.close();
        return result;
    }

    /**
     * Updates table USERS with new values from LDAP
     * @return List of users that were added to a database during update execution
     */
    //todo: test this
    public List<User> updateUsersTable() throws SQLException {
        String table = "users";
        String column = "samaccountname";
        Statement st = conn.createStatement();

        List<User> ldapusers = ldapParser.getUsersDataList();
        List<User> newUsers = new ArrayList<>();

        for(User user : ldapusers){
            boolean exists = existsInDB(table, column, user.getSamAccountName());
            if(!exists) {

                String query = PgStatement.insert(table, User.insertColumns, PgStatement.apostrophied(user.getComplete()));
                //System.out.println(query);
                st.executeUpdate(query);
                newUsers.add(user);
            }
        }
        st.close();
        return newUsers;
    }


    public boolean getNewUsers(int count) throws SQLException {
        UserHolder.clear();
        Statement st = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        ResultSet resultSet = st.executeQuery(PgStatement.selectAscending("users,city", "*","users.city=city.id", "whencreated", false) + "limit " + count);
        while(resultSet.next()){
            String[] resultUserData = new String[User.columns.length];
            for (int i = 0; i <= User.columns.length - 1 ; i++) {
                resultUserData[i] = resultSet.getString(User.columns[i]);
            }
            User user = new User(resultUserData);
            UserHolder.addUser(user);
        }
        //System.out.println(resultSet.getRow());
        resultSet.close();
        return true;
    }

    /**
     * Performs search in Users table for inputted value
     * @param value value to search for in database
     * @param limiter maximum amount of rows returned by query, no limit of set to 0
     * @return list of Users with fitting value
     */
    public List<User> globalSearch(String value, int limiter) throws SQLException {
        ArrayList<User> foundUsers = new ArrayList<>();
        String allSearchCriterium = "(";
        for (int i = 0; i < User.columns.length; i++) {
            if(i < User.columns.length - 1) {
                allSearchCriterium += " " + User.columns[i] + "=" + PgStatement.apostrophied(value) + " OR";
            } else {
                allSearchCriterium += " " + User.columns[i] + "=" + PgStatement.apostrophied(value) + ")";
            }
        }

        allSearchCriterium += " AND (users.city=city.id)";

        String query = PgStatement.select("users,city", "*", allSearchCriterium) + " limit " + limiter;
        if(limiter == 0) {
            query = PgStatement.select("users,city", "*", allSearchCriterium);
        }

        //System.out.println(query);
        Statement st = conn.createStatement();
        ResultSet resultSet = st.executeQuery(query);


        while(resultSet.next()){
            String[] resultUserData = new String[User.columns.length];
            for (int i = 0; i <= User.columns.length - 1 ; i++) {
                resultUserData[i] = resultSet.getString(User.columns[i]);
            }
            User user = new User(resultUserData);
            foundUsers.add(user);
        }

        st.close();

        return foundUsers;
    }

    public User reloadUser (User user) throws SQLException {
        return globalSearch(user.getSamAccountName(), 1).get(0);
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